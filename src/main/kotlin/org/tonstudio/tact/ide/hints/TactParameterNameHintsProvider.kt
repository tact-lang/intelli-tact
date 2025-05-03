package org.tonstudio.tact.ide.hints

import com.intellij.codeInsight.hints.HintInfo
import com.intellij.codeInsight.hints.InlayInfo
import com.intellij.codeInsight.hints.InlayParameterHintsProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.startOffset
import org.tonstudio.tact.lang.TactLanguage
import org.tonstudio.tact.lang.psi.*
import kotlin.math.min

@Suppress("UnstableApiUsage")
class TactParameterNameHintsProvider : InlayParameterHintsProvider {
    override fun getHintInfo(element: PsiElement): HintInfo? {
        if (element !is TactCallExpr) return null

        val (signature, resolved) = element.resolveSignature() ?: return null

        val parameters = signature.parameters.paramDefinitionList.map { it.name ?: "_" }
        return createMethodInfo(resolved as TactNamedElement, parameters)
    }

    private fun createMethodInfo(function: TactNamedElement, parameters: List<String>): HintInfo.MethodInfo? {
        val qualifiedName = function.getQualifiedName() ?: return null
        return HintInfo.MethodInfo(qualifiedName, parameters, TactLanguage)
    }

    override fun getParameterHints(element: PsiElement, file: PsiFile): MutableList<InlayInfo> {
        if (element !is TactCallExpr) return mutableListOf()
        return handleCallExpr(element)
    }

    private fun handleCallExpr(element: TactCallExpr): MutableList<InlayInfo> {
        val hints = mutableListOf<InlayInfo>()
        val (signature, resolved) = element.resolveSignature() ?: return hints
        val expression = element.expression ?: return hints
        val skipSelf = needSkipSelf(expression, signature)

        val parameters = resolved.getSignature()?.parameters ?: return hints
        val params = parameters.paramDefinitionList

        val argsList = element.argumentList.elementList
        val args = argsList
            .filter { it.key == null && it.value != null } // don't show any hint for 'key: value' arguments
            .mapNotNull { it.value?.expression }

        if (skipSelf && params.isNotEmpty()) {
            params.removeFirst()
        }

        val fromStubs = (resolved.containingFile as? TactFile)?.fromStubs() ?: false
        if (args.size == 1 && fromStubs) {
            // don't show any hints for unary functions from stubs like `ton()`
            return hints
        }

        for (i in 0 until min(params.size, args.size)) {
            val parameter = params[i] ?: continue
            val name = parameter.name ?: continue
            val arg = args[i]

            val argResolved = arg.reference?.resolve()
            if (argResolved is TactNamedElement) {
                // don't show hints for obvious cases
                if (argResolved.name == parameter.name) continue
            }

            val offset = arg.startOffset
            val inlayInfo = InlayInfo(name, offset)
            hints.add(inlayInfo)
        }

        return hints
    }

    private fun needSkipSelf(expression: TactExpression, signature: TactSignature): Boolean {
        if (expression !is TactReferenceExpression) return false
        expression.getQualifier() ?: return false
        return signature.withSelf()
    }

    override fun getDefaultBlackList() = setOf<String>()

    override fun getBlacklistExplanationHTML(): String = ""
}
