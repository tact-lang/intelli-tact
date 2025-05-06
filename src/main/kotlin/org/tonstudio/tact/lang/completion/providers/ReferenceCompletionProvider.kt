package org.tonstudio.tact.lang.completion.providers

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.util.elementType
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import org.tonstudio.tact.lang.TactTypes
import org.tonstudio.tact.lang.completion.TactCompletionUtil
import org.tonstudio.tact.lang.completion.TactCompletionUtil.KEYWORD_PRIORITY
import org.tonstudio.tact.lang.completion.TactCompletionUtil.toTactLookupElement
import org.tonstudio.tact.lang.completion.TactCompletionUtil.withPriority
import org.tonstudio.tact.lang.completion.TactLookupElementProperties
import org.tonstudio.tact.lang.completion.TactStructLiteralCompletion
import org.tonstudio.tact.lang.psi.*
import org.tonstudio.tact.lang.psi.impl.*
import org.tonstudio.tact.lang.psi.types.TactOptionTypeEx
import org.tonstudio.tact.lang.psi.types.TactTypeEx

object ReferenceCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet,
    ) {
        val element = parameters.position
        val set = TactCompletionUtil.withCamelHumpPrefixMatcher(result)

        val expression = element.parentOfType<TactReferenceExpressionBase>() ?: return
        if (expression.parent is TactTlb) return

        val ref = expression.reference
        if (ref is TactReference) {
            val refExpression = ref.element as? TactReferenceExpression
            val expectKey = TactStructLiteralCompletion.expectKeyInInstanceExpression(refExpression, element)

            fillStructFieldNameVariants(parameters, set, expectKey, refExpression)

            if (!expectKey) {
                ref.processResolveVariants(MyScopeProcessor(parameters, set, ref.forTypes))
            }
        } else if (ref is TactCachedReference<*>) {
            ref.processResolveVariants(MyScopeProcessor(parameters, set, false))
        }
    }

    private fun fillStructFieldNameVariants(
        parameters: CompletionParameters,
        result: CompletionResultSet,
        expectKey: Boolean,
        refExpression: TactReferenceExpression?,
    ) {
        if (refExpression == null || !expectKey) {
            return
        }

        val possiblyLiteralValueExpression = refExpression.parentOfType<TactLiteralValueExpression>() ?: return

        val allFields = mutableSetOf<Pair<String, TactTypeEx?>>()
        val requiredFields = mutableSetOf<Pair<String, TactTypeEx?>>()

        val arguments = possiblyLiteralValueExpression.instanceArguments
        val alreadyAssignedFields = TactStructLiteralCompletion.alreadyAssignedFields(arguments)

        TactFieldNameReference(refExpression).processResolveVariants(object : MyScopeProcessor(parameters, result, false) {
            override fun execute(element: PsiElement, state: ResolveState): Boolean {
                val field = element as? TactFieldDefinition ?: return false
                val decl = field.parent as? TactFieldDeclaration ?: return false

                val type = field.getType(null)
                val name = field.name ?: return false
                val canBeOmitted = decl.defaultFieldValue != null || type is TactOptionTypeEx

                if (alreadyAssignedFields.contains(name)) {
                    return true
                }

                allFields.add(name to type)

                if (!canBeOmitted) {
                    requiredFields.add(name to type)
                }

                return super.execute(element, state)
            }
        })

        TactReference(refExpression).processBlock(object : MyScopeProcessor(parameters, result, false) {
            override fun accept(e: PsiElement, forTypes: Boolean): Boolean {
                val named = e as? TactNamedElement ?: return true
                return allFields.any { (name) -> named.name == name }
            }
        }, ResolveState.initial(), false)

        if (allFields.size > 1) {
            val fields = allFields.toList()
            val element =
                LookupElementBuilder.create("")
                    .withPresentableText("Fill all fields…")
                    .withIcon(AllIcons.Actions.RealIntentionBulb)
                    .withInsertHandler(StructFieldsInsertHandler(fields, false))

            result.addElement(element)
        }

        if (requiredFields.size > 0) {
            val fields = requiredFields.toList()
            val element =
                LookupElementBuilder.create("_")
                    .withPresentableText("Fill required fields…")
                    .withIcon(AllIcons.Actions.RealIntentionBulb)
                    .withInsertHandler(StructFieldsInsertHandler(fields, true))
                    .withPriority(KEYWORD_PRIORITY)

            result.addElement(element)
        }
    }

    class StructFieldsInsertHandler(
        private val fields: List<Pair<String, TactTypeEx?>>,
        private val second: Boolean,
    ) : InsertHandler<LookupElement> {
        override fun handleInsert(context: InsertionContext, item: LookupElement) {
            val project = context.project
            val offset = context.editor.caretModel.offset
            val element = context.file.findElementAt(offset) ?: return
            var prevElement = element.prevSibling

            // remove "1" for the second option
            val doc = context.document
            val start = context.startOffset
            if (second) {
                doc.deleteString(start, start + 1)
                prevElement = prevElement?.prevSibling
            }

            val before = if (prevElement.elementType == TactTypes.LBRACE) "\n" else ""
            val after = if (element.elementType == TactTypes.RBRACE) "\n" else ""

            val templateText = fields.joinToString("\n", before, after) { (name) ->
                "$name: \$field_${name}$,"
            }

            val template = TemplateManager.getInstance(project).createTemplate("closures", "tact", templateText)
            template.isToReformat = true

            for ((name, type) in fields) {
                template.addVariable(
                    "field_${name}", ConstantNode(TactLangUtil.getDefaultValue(element, type)), true
                )
            }

            TemplateManager.getInstance(project).startTemplate(context.editor, template)
        }
    }

    open class MyScopeProcessor(
        private val parameters: CompletionParameters,
        private val result: CompletionResultSet,
        private val forTypes: Boolean,
    ) : TactScopeProcessor() {

        private val processedNames = mutableSetOf<String>()

        override fun execute(element: PsiElement, state: ResolveState): Boolean {
            if (accept(element, forTypes)) {
                addElement(
                    element,
                    state,
                    forTypes,
                    processedNames,
                    result,
                    parameters,
                )
            }
            return true
        }

        protected open fun accept(e: PsiElement, forTypes: Boolean): Boolean {
            if (forTypes) {
                if (e !is TactNamedElement) return false
                if (e.isBlank()) {
                    return false
                }

                return e is TactStructDeclaration ||
                        e is TactMessageDeclaration ||
                        e is TactPrimitiveDeclaration ||
                        e is TactContractDeclaration ||
                        e is TactTraitDeclaration
            }

            if (e is TactFile) {
                return true
            }

            if (e is TactNamedElement) {
                if (e.isBlank()) {
                    return false
                }

                if ((e is TactFieldDefinition)) {
                    return true
                }

                return true
            }

            return false
        }

        override fun isCompletion(): Boolean = true

        open fun addElement(
            o: PsiElement,
            state: ResolveState,
            forTypes: Boolean,
            processedNames: MutableSet<String>,
            set: CompletionResultSet,
            parameters: CompletionParameters,
        ) {
            val lookup = createLookupElement(o, forTypes, parameters)
            if (lookup != null) {
                val key = lookup.lookupString + o.javaClass
                if (!processedNames.contains(key)) {
                    set.addElement(lookup)
                    processedNames.add(key)
                }
            }
        }
    }

    private fun createLookupElement(
        element: PsiElement,
        forTypes: Boolean,
        parameters: CompletionParameters,
    ): LookupElement? {
        val context = parameters.position

        val contextFunction = context.parentOfType<TactFunctionDeclaration>()
        val elementFunction = element.parentOfType<TactFunctionDeclaration>()
        val isLocal = contextFunction == elementFunction

        val kind = when (element) {
            is TactPrimitiveDeclaration -> TactLookupElementProperties.ElementKind.PRIMITIVE
            is TactFunctionDeclaration  -> TactLookupElementProperties.ElementKind.FUNCTION
            is TactStructDeclaration    -> TactLookupElementProperties.ElementKind.STRUCT
            is TactContractDeclaration  -> TactLookupElementProperties.ElementKind.CONTRACT
            is TactTraitDeclaration     -> TactLookupElementProperties.ElementKind.TRAIT
            is TactMessageDeclaration   -> TactLookupElementProperties.ElementKind.MESSAGE
            is TactConstDeclaration      -> TactLookupElementProperties.ElementKind.CONSTANT
            is TactFieldDefinition      -> TactLookupElementProperties.ElementKind.FIELD
            is TactNamedElement         -> TactLookupElementProperties.ElementKind.OTHER
            else                        -> return null
        }

        val lookupElement = when (element) {
            is TactPrimitiveDeclaration      -> TactCompletionUtil.createPrimitiveLookupElement(element)
            is TactFunctionDeclaration       -> TactCompletionUtil.createFunctionLookupElement(element)
            is TactAsmFunctionDeclaration    -> TactCompletionUtil.createAsmFunctionLookupElement(element)
            is TactNativeFunctionDeclaration -> TactCompletionUtil.createNativeFunctionLookupElement(element)
            is TactStructDeclaration         -> TactCompletionUtil.createStructLookupElement(element, !forTypes)
            is TactMessageDeclaration        -> TactCompletionUtil.createMessageLookupElement(element, !forTypes)
            is TactContractDeclaration       -> TactCompletionUtil.createContractLookupElement(element)
            is TactTraitDeclaration          -> TactCompletionUtil.createTraitLookupElement(element)
            is TactFieldDefinition           -> TactCompletionUtil.createFieldLookupElement(element)
            is TactConstDeclaration           -> TactCompletionUtil.createConstantLookupElement(element)
            is TactParamDefinition           -> TactCompletionUtil.createParamLookupElement(element)
            is TactNamedElement              -> TactCompletionUtil.createVariableLikeLookupElement(element)
            else                             -> null
        }

        var isContextElement = false
        if (lookupElement is PrioritizedLookupElement<*>) {
            isContextElement = lookupElement.priority.toInt() == TactCompletionUtil.CONTEXT_COMPLETION_PRIORITY
        }

        return lookupElement?.toTactLookupElement(
            TactLookupElementProperties(
                isLocal = isLocal,
                elementKind = kind,
                isContextElement = isContextElement,
            )
        )
    }
}
