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
import org.tonstudio.tact.lang.completion.TactCompletionUtil.toTactLookupElement
import org.tonstudio.tact.lang.completion.TactLookupElementProperties
import org.tonstudio.tact.lang.completion.TactStructLiteralCompletion
import org.tonstudio.tact.lang.psi.*
import org.tonstudio.tact.lang.psi.impl.*
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
            val variants = TactStructLiteralCompletion.allowedVariants(refExpression, element)

            fillStructFieldNameVariants(parameters, set, variants, refExpression)

            if (variants != TactStructLiteralCompletion.Variants.FIELD_NAME_ONLY) {
                ref.processResolveVariants(MyScopeProcessor(parameters, set, ref.forTypes))
            }
        } else if (ref is TactCachedReference<*>) {
            ref.processResolveVariants(MyScopeProcessor(parameters, set, false))
        }
    }

    private fun fillStructFieldNameVariants(
        parameters: CompletionParameters,
        result: CompletionResultSet,
        variants: TactStructLiteralCompletion.Variants,
        refExpression: TactReferenceExpression?,
    ) {
        if (refExpression == null ||
            variants !== TactStructLiteralCompletion.Variants.FIELD_NAME_ONLY &&
            variants !== TactStructLiteralCompletion.Variants.BOTH
        ) {
            return
        }

        val possiblyLiteralValueExpression = refExpression.parentOfType<TactLiteralValueExpression>() ?: return

        val fields = mutableSetOf<Pair<String, TactTypeEx?>>()
        val elementList = possiblyLiteralValueExpression.elementList

        val alreadyAssignedFields = TactStructLiteralCompletion.alreadyAssignedFields(elementList)

        TactFieldNameReference(refExpression).processResolveVariants(object : MyScopeProcessor(parameters, result, false) {
            override fun execute(element: PsiElement, state: ResolveState): Boolean {
                val structFieldName =
                    when (element) {
                        is TactFieldDefinition -> element.name
                        else                   -> null
                    }

                val structFieldType =
                    when (element) {
                        is TactFieldDefinition -> element.getType(null)
                        else                   -> null
                    }

                if (structFieldName != null) {
                    fields.add(structFieldName to structFieldType)
                }

                if (structFieldName != null && alreadyAssignedFields.contains(structFieldName)) {
                    return true
                }

                return super.execute(element, state)
            }
        })

        val remainingFields = fields.filter { !alreadyAssignedFields.contains(it.first) }
        if (remainingFields.size > 1) {
            val element = LookupElementBuilder.create("")
                .withPresentableText("Fill all fields…")
                .withIcon(AllIcons.Actions.RealIntentionBulb)
                .withInsertHandler(StructFieldsInsertHandler(remainingFields))

            result.addElement(element)
        }
    }

    class StructFieldsInsertHandler(private val fields: List<Pair<String, TactTypeEx?>>) : InsertHandler<LookupElement> {
        override fun handleInsert(context: InsertionContext, item: LookupElement) {
            val project = context.project
            val offset = context.editor.caretModel.offset
            val element = context.file.findElementAt(offset) ?: return
            val prevElement = element.prevSibling

            val before = if (prevElement.elementType == TactTypes.LBRACE) "\n" else ""
            val after = if (element.elementType == TactTypes.RBRACE) "\n" else ""

            val templateText = fields.joinToString("\n", before, after) {
                it.first + ": \$field_${it.first}$,"
            }

            val template = TemplateManager.getInstance(project)
                .createTemplate("closures", "tact", templateText)
            template.isToReformat = true

            fields.forEach {
                template.addVariable(
                    "field_${it.first}",
                    ConstantNode(TactLangUtil.getDefaultValue(element, it.second)),
                    true
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

        private fun accept(e: PsiElement, forTypes: Boolean): Boolean {
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

        val contextFunction = context.parentOfType<TactFunctionOrMethodDeclaration>()
        val elementFunction = element.parentOfType<TactFunctionOrMethodDeclaration>()
        val isLocal = contextFunction == elementFunction

        val kind = when (element) {
            is TactPrimitiveDeclaration -> TactLookupElementProperties.ElementKind.PRIMITIVE
            is TactFunctionDeclaration  -> TactLookupElementProperties.ElementKind.FUNCTION
            is TactStructDeclaration    -> TactLookupElementProperties.ElementKind.STRUCT
            is TactContractDeclaration  -> TactLookupElementProperties.ElementKind.CONTRACT
            is TactTraitDeclaration     -> TactLookupElementProperties.ElementKind.TRAIT
            is TactMessageDeclaration   -> TactLookupElementProperties.ElementKind.MESSAGE
            is TactConstDefinition      -> TactLookupElementProperties.ElementKind.CONSTANT
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
            is TactConstDefinition           -> TactCompletionUtil.createConstantLookupElement(element)
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
