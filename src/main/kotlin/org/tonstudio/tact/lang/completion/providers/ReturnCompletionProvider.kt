package org.tonstudio.tact.lang.completion.providers

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import org.tonstudio.tact.lang.completion.TactCompletionUtil
import org.tonstudio.tact.lang.completion.TactCompletionUtil.KEYWORD_PRIORITY
import org.tonstudio.tact.lang.completion.TactCompletionUtil.withPriority
import org.tonstudio.tact.lang.psi.TactContractInitDeclaration
import org.tonstudio.tact.lang.psi.TactFunctionDeclaration
import org.tonstudio.tact.lang.psi.TactMessageFunctionDeclaration
import org.tonstudio.tact.lang.psi.impl.TactLangUtil
import org.tonstudio.tact.lang.psi.types.TactBaseTypeEx.Companion.toEx
import org.tonstudio.tact.lang.psi.types.TactTypeEx

object ReturnCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        val (returnType, inFunction) = outerFunctionReturnType(parameters.position)
        if (!inFunction) return

        if (returnType == null) {
            val voidReturn = LookupElementBuilder.create("return;")
                .bold()
                .withPriority(KEYWORD_PRIORITY)
            result.addElement(voidReturn)
            return
        }

        val exprReturn = LookupElementBuilder.create("return")
            .bold()
            .withTailText(" <expr>;")
            .withInsertHandler(
                TactCompletionUtil.TemplateStringInsertHandler(
                    " \$expr$;\$END$",
                    true,
                    "expr" to ConstantNode(TactLangUtil.getDefaultValue(returnType)),
                )
            )
            .withPriority(KEYWORD_PRIORITY)
        result.addElement(exprReturn)
    }

    private fun outerFunctionReturnType(anchor: PsiElement): Pair<TactTypeEx?, Boolean> {
        val outerFunction = anchor.parentOfType<TactFunctionDeclaration>()
        if (outerFunction != null) {
            return outerFunction.getSignature().result?.getType().toEx() to true
        }

        val outerReceiver = anchor.parentOfType<TactMessageFunctionDeclaration>()
        if (outerReceiver != null) {
            return null to true
        }

        val outerInit = anchor.parentOfType<TactContractInitDeclaration>()
        if (outerInit != null) {
            return null to true
        }

        return null to false
    }
}
