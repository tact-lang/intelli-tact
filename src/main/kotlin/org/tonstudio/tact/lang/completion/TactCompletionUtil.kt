package org.tonstudio.tact.lang.completion

import com.intellij.codeInsight.AutoPopupController
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.completion.impl.CamelHumpMatcher
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.lookup.LookupElementPresentation
import com.intellij.codeInsight.lookup.LookupElementRenderer
import com.intellij.codeInsight.template.Expression
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import org.tonstudio.tact.ide.ui.Icons
import org.tonstudio.tact.lang.psi.*
import org.tonstudio.tact.lang.psi.impl.TactLangUtil
import javax.swing.Icon

object TactCompletionUtil {
    const val KEYWORD_PRIORITY = 10
    const val CONTEXT_COMPLETION_PRIORITY = 20

    fun withCamelHumpPrefixMatcher(resultSet: CompletionResultSet): CompletionResultSet {
        return resultSet.withPrefixMatcher(createPrefixMatcher(resultSet.prefixMatcher.prefix))
    }

    private fun createPrefixMatcher(prefix: String) = CamelHumpMatcher(prefix, false)

    fun LookupElementBuilder.withPriority(priority: Int): LookupElement {
        return PrioritizedLookupElement.withPriority(this, priority.toDouble())
    }

    fun LookupElement.toTactLookupElement(properties: TactLookupElementProperties): LookupElement {
        return TactLookupElement(this, properties)
    }

    fun createVariableLikeLookupElement(element: TactNamedElement): LookupElement? {
        val name = element.name
        if (name.isNullOrEmpty()) {
            return null
        }
        return PrioritizedLookupElement.withPriority(
            LookupElementBuilder.createWithSmartPointer(name, element)
                .withRenderer(VARIABLE_RENDERER)
                .withInsertHandler(null),
            0.0,
        )
    }

    fun createParamLookupElement(element: TactParamDefinition): LookupElement? {
        val name = element.name
        if (name.isNullOrEmpty()) {
            return null
        }
        return PrioritizedLookupElement.withPriority(
            LookupElementBuilder.createWithSmartPointer(name, element)
                .withRenderer(ParameterRenderer())
                .withInsertHandler(null),
            0.0,
        )
    }

    fun createFunctionLookupElement(element: TactFunctionDeclaration): LookupElement? {
        val name = element.name
        if (name.isEmpty()) {
            return null
        }

        return createFunctionLookupElement(
            element, name,
            insertHandler = FunctionInsertHandler(element),
        )
    }

    fun createAsmFunctionLookupElement(element: TactAsmFunctionDeclaration): LookupElement? {
        val name = element.name
        if (name.isNullOrEmpty()) {
            return null
        }

        return createFunctionLookupElement(
            element, name,
            insertHandler = FunctionInsertHandler(element),
        )
    }

    fun createNativeFunctionLookupElement(element: TactNativeFunctionDeclaration): LookupElement? {
        val name = element.name
        if (name.isNullOrEmpty()) {
            return null
        }

        return createFunctionLookupElement(
            element, name,
            insertHandler = FunctionInsertHandler(element),
        )
    }

    private fun createFunctionLookupElement(
        element: TactNamedElement, lookupString: String,
        insertHandler: InsertHandler<LookupElement>? = null,
    ): LookupElement {
        return PrioritizedLookupElement.withPriority(
            LookupElementBuilder.createWithSmartPointer(lookupString, element)
                .withRenderer(FunctionRenderer())
                .withInsertHandler(insertHandler),
            0.0,
        )
    }

    fun createConstantLookupElement(element: TactNamedElement): LookupElement? {
        val name = element.name
        if (name.isNullOrEmpty()) {
            return null
        }

        return PrioritizedLookupElement.withPriority(
            LookupElementBuilder.createWithSmartPointer(name, element)
                .withRenderer(ConstantRenderer())
                .withInsertHandler(ConstantInsertHandler()),
            0.0,
        )
    }

    fun createFieldLookupElement(element: TactNamedElement): LookupElement? {
        val name = element.name
        if (name.isNullOrEmpty()) {
            return null
        }

        return PrioritizedLookupElement.withPriority(
            LookupElementBuilder.createWithSmartPointer(name, element)
                .withRenderer(FIELD_RENDERER)
                .withInsertHandler(FieldInsertHandler()),
            0.0,
        )
    }

    fun createStructLookupElement(element: TactStructDeclaration, needBrackets: Boolean): LookupElement? {
        val name = element.name
        if (name.isEmpty()) {
            return null
        }

        return PrioritizedLookupElement.withPriority(
            LookupElementBuilder.createWithSmartPointer(name, element)
                .withRenderer(ClassLikeRenderer(Icons.Struct, needBrackets))
                .withInsertHandler(StructInsertHandler(needBrackets)),
            0.0,
        )
    }

    fun createMessageLookupElement(element: TactMessageDeclaration, needBrackets: Boolean): LookupElement? {
        val name = element.name
        if (name.isEmpty()) {
            return null
        }

        return PrioritizedLookupElement.withPriority(
            LookupElementBuilder.createWithSmartPointer(name, element)
                .withRenderer(ClassLikeRenderer(Icons.Message, needBrackets))
                .withInsertHandler(StructInsertHandler(needBrackets)),
            0.0,
        )
    }

    fun createContractLookupElement(element: TactContractDeclaration): LookupElement? {
        val name = element.name
        if (name.isEmpty()) {
            return null
        }

        return createClassLikeLookupElement(element, name, Icons.Contract)
    }

    fun createTraitLookupElement(element: TactTraitDeclaration): LookupElement? {
        val name = element.name
        if (name.isEmpty()) {
            return null
        }

        return createClassLikeLookupElement(element, name, Icons.Trait)
    }

    fun createPrimitiveLookupElement(element: TactPrimitiveDeclaration): LookupElement? {
        val name = element.name
        if (name.isEmpty()) {
            return null
        }

        return createClassLikeLookupElement(element, name, Icons.Primitive)
    }

    private fun createClassLikeLookupElement(
        element: TactNamedElement, lookupString: String, icon: Icon,
    ): LookupElement {
        return PrioritizedLookupElement.withPriority(
            LookupElementBuilder.createWithSmartPointer(lookupString, element)
                .withRenderer(ClassLikeRenderer(icon, false))
                .withInsertHandler(ClassLikeInsertHandler(false)),
            0.0,
        )
    }

    fun showCompletion(editor: Editor) {
        AutoPopupController.getInstance(editor.project!!).autoPopupMemberLookup(editor, null)
    }

    abstract class ElementInsertHandler : InsertHandler<LookupElement> {
        open fun handleInsertion(context: InsertionContext, item: LookupElement) {}

        final override fun handleInsert(context: InsertionContext, item: LookupElement) {
            context.commitDocument()
            handleInsertion(context, item)
            context.commitDocument()
        }
    }

    open class FunctionInsertHandler(
        private val function: TactSignatureOwner,
    ) : ElementInsertHandler() {
        override fun handleInsertion(context: InsertionContext, item: LookupElement) {
            val caretOffset = context.editor.caretModel.offset
            val element = context.file.findElementAt(caretOffset - 1)

            val parent = element?.parent as? TactReferenceExpression
            val takeZeroArguments = TactLangUtil.takeZeroArguments(function)
            val methodCallWithNoArgs = isMethodCallWithNoArgs(parent)
            val cursorAfterParens = takeZeroArguments || methodCallWithNoArgs

            val prevChar = context.document.charsSequence.getOrNull(caretOffset)
            val withParenAfterCursor = prevChar == '('

            if (!withParenAfterCursor) {
                try {
                    context.document.insertString(caretOffset, "()")
                } catch (e: Exception) {
                    return
                }
            }

            if (cursorAfterParens) {
                // move after ()
                context.editor.caretModel.moveToOffset(caretOffset + 2)
                return
            }
            context.editor.caretModel.moveToOffset(caretOffset + 1)

            // invoke parameter info to automatically show parameter info popup
            AutoPopupController.getInstance(context.project).autoPopupParameterInfo(context.editor, null)
        }

        private fun isMethodCallWithNoArgs(parent: TactReferenceExpression?): Boolean {
            if (parent?.getQualifier() == null) return false
            val withSelf = function.getSignature()?.withSelf() ?: false
            if (!withSelf) return false
            return TactLangUtil.takeSingleArgument(function) // only self parameter
        }
    }

    class ConstantInsertHandler : ElementInsertHandler()

    class StructInsertHandler(
        private val needBrackets: Boolean = true,
    ) : ElementInsertHandler() {

        override fun handleInsertion(context: InsertionContext, item: LookupElement) {
            val caretOffset = context.editor.caretModel.offset

            if (needBrackets) {
                context.document.insertString(caretOffset, " {}")
                context.editor.caretModel.moveToOffset(caretOffset + 2)

                showCompletion(context.editor)
                return
            }

            val file = context.file as? TactFile ?: return
            val offset = context.startOffset
            val at = file.findElementAt(offset) ?: return
            handleIncompleteParameter(context, at)
        }
    }

    class ClassLikeInsertHandler(private val needBrackets: Boolean) : ElementInsertHandler() {
        override fun handleInsertion(context: InsertionContext, item: LookupElement) {
            val file = context.file as? TactFile ?: return
            context.commitDocument()

            val caretOffset = context.editor.caretModel.offset

            if (needBrackets) {
                context.document.insertString(caretOffset, " {}")
                context.editor.caretModel.moveToOffset(caretOffset + 2)

                showCompletion(context.editor)
                return
            }

            val offset = context.startOffset
            val at = file.findElementAt(offset) ?: return
            handleIncompleteParameter(context, at)
        }
    }

    fun handleIncompleteParameter(context: InsertionContext, element: PsiElement) {
        val paramDefinition = element.parentOfType<TactParamDefinition>() ?: return
        val name = paramDefinition.name
        if (name == null) {
            // run only for parameters without a name
            val caretOffset = paramDefinition.type.textOffset
            context.editor.caretModel.moveToOffset(caretOffset)

            val template = TemplateManager.getInstance(context.project)
                .createTemplate("templateInsertHandler", "tact", "\$expr\$\$END\$ ")

            template.addVariable("expr", ConstantNode("param"), true)

            TemplateManager.getInstance(context.project).startTemplate(context.editor, template)
        }
    }

    class StringInsertHandler(val string: String, private val shift: Int, private val enable: Boolean = true) :
        InsertHandler<LookupElement> {
        override fun handleInsert(context: InsertionContext, item: LookupElement) {
            if (!enable) return

            val caretOffset = context.editor.caretModel.offset

            context.document.insertString(caretOffset, string)
            context.editor.caretModel.moveToOffset(caretOffset + shift)
        }
    }

    class TemplateStringInsertHandler(
        private val string: String,
        private val reformat: Boolean = true,
        private vararg val variables: Pair<String, Expression>,
    ) : InsertHandler<LookupElement> {
        override fun handleInsert(context: InsertionContext, item: LookupElement) {
            val template = TemplateManager.getInstance(context.project)
                .createTemplate("templateInsertHandler", "tact", string)
            template.isToReformat = reformat

            variables.forEach { (name, expression) ->
                template.addVariable(name, expression, true)
            }

            TemplateManager.getInstance(context.project).startTemplate(context.editor, template)
        }
    }

    open class FieldInsertHandler : SingleCharInsertHandler(':', needSpace = true) {
        override fun handleInsert(context: InsertionContext, item: LookupElement) {
            val file = context.file as? TactFile ?: return
            context.commitDocument()

            val offset = context.startOffset
            val at = file.findElementAt(offset)

            val instanceArgument = at?.parentOfType<TactInstanceArgument>()
            if (instanceArgument?.instanceArgumentShort != null) {
                super.handleInsert(context, item)
            }
        }
    }

    private val VARIABLE_RENDERER = object : LookupElementRenderer<LookupElement>() {
        override fun renderElement(element: LookupElement, p: LookupElementPresentation) {
            val elem = element.psiElement as? TactNamedElement ?: return
            val type = elem.getType(null)?.readableName(elem)
            val icon = when (elem) {
                is TactVarDefinition   -> Icons.Variable
                is TactParamDefinition -> Icons.Parameter
                else                   -> null
            }

            p.icon = icon
            p.typeText = type
            p.isTypeGrayed = true
            p.itemText = element.lookupString
        }
    }

    class ParameterRenderer : LookupElementRenderer<LookupElement>() {
        override fun renderElement(element: LookupElement, p: LookupElementPresentation) {
            val elem = element.psiElement as? TactParamDefinition ?: return


            p.icon = Icons.Parameter
            p.typeText = elem.getType(null)?.readableName(elem)
            p.isTypeGrayed = true
            p.itemText = element.lookupString
        }
    }

    private val FIELD_RENDERER = object : LookupElementRenderer<LookupElement>() {
        override fun renderElement(element: LookupElement, p: LookupElementPresentation) {
            val elem = element.psiElement as? TactNamedElement ?: return
            val type = elem.getType(null)?.readableName(elem)
            val icon = Icons.Field

            val parent = elem.parentOfType<TactNamedElement>()
            p.tailText = " of " + parent?.name

            p.icon = icon
            p.typeText = type
            p.isTypeGrayed = true
            p.itemText = element.lookupString
        }
    }

    class ClassLikeRenderer(private val icon: Icon, private val needBraces: Boolean) : ElementRenderer() {
        override fun render(element: LookupElement, p: LookupElementPresentation) {
            p.icon = icon
            p.itemText = element.lookupString

            if (icon == Icons.Struct || icon == Icons.Message) {
                p.tailText = if (needBraces) " {}" else ""
            }
        }
    }

    class FunctionRenderer : ElementRenderer() {
        override fun render(element: LookupElement, p: LookupElementPresentation) {
            val elem = element.psiElement as? TactSignatureOwner ?: return
            p.icon = Icons.Function

            val signature = elem.getSignature()
            val parameters = signature?.parameters?.text ?: ""
            val result = signature?.result?.type?.text

            p.tailText = parameters
            p.itemText = element.lookupString
            p.typeText = result
        }
    }

    class ConstantRenderer : ElementRenderer() {
        override fun render(element: LookupElement, p: LookupElementPresentation) {
            val elem = element.psiElement as? TactConstDeclaration ?: return
            p.icon = Icons.Constant
            p.itemText = element.lookupString

            val valueText = elem.expressionText
            p.tailText = " = $valueText"
            p.typeText = elem.expressionType
        }
    }

    abstract class ElementRenderer : LookupElementRenderer<LookupElement>() {
        abstract fun render(element: LookupElement, p: LookupElementPresentation)

        override fun renderElement(element: LookupElement, p: LookupElementPresentation) {
            render(element, p)
        }
    }

    fun shouldSuppressCompletion(element: PsiElement): Boolean {
        val parent = element.parent
        val grand = parent.parent
        if (grand is TactVarDeclaration && PsiTreeUtil.isAncestor(grand, element, false)) {
            return true
        }

        if (parent is TactFunctionDeclaration) {
            return true
        }

        return parent is TactStructType ||
                parent is TactMessageType ||
                parent is TactTraitType ||
                parent is TactContractType ||
                parent is TactPrimitiveDeclaration
    }
}
