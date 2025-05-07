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

class TactParameterNameHintsProvider : InlayParameterHintsProvider {
    override fun getHintInfo(element: PsiElement): HintInfo? {
        if (element is TactCallExpr) {
            val (signature, resolved) = element.resolveSignature() ?: return null

            val parameters = signature.parameters.paramDefinitionList.map { it.name ?: "_" }
            return createMethodInfo(resolved as TactNamedElement, parameters)
        }

        if (element is TactInitOfExpr) {
            val fields = element.resolve() ?: return null
            val fieldNames = fields.map { it.identifier?.text ?: "unnamed" }
            return HintInfo.MethodInfo("init", fieldNames, TactLanguage)
        }

        return null
    }

    private fun createMethodInfo(function: TactNamedElement, parameters: List<String>): HintInfo.MethodInfo? {
        val name = function.name ?: return null
        return HintInfo.MethodInfo(name, parameters, TactLanguage)
    }

    override fun getParameterHints(element: PsiElement, file: PsiFile): MutableList<InlayInfo> {
        if (element is TactCallExpr) {
            return handleCallExpr(element)
        }
        if (element is TactInitOfExpr) {
            return handleInitOfExpr(element)
        }
        return mutableListOf()
    }

    private fun handleCallExpr(element: TactCallExpr): MutableList<InlayInfo> {
        val hints = mutableListOf<InlayInfo>()
        val (signature, resolved) = element.resolveSignature() ?: return hints
        val expression = element.expression ?: return hints
        val skipSelf = needSkipSelf(expression, signature)

        val parameters = resolved.getSignature()?.parameters ?: return hints
        val params = parameters.paramDefinitionList

        val args = element.argumentList.expressionList

        if (skipSelf && params.isNotEmpty()) {
            params.removeFirst()
        }

        val fromStubs = (resolved.containingFile as? TactFile)?.fromStubs() ?: false
        if (params.size == 1 && fromStubs) {
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

    private fun handleInitOfExpr(element: TactInitOfExpr): MutableList<InlayInfo> {
        val hints = mutableListOf<InlayInfo>()
        val params = element.resolve() ?: return hints

        val args = element.argumentList?.expressionList ?: emptyList()

        for (i in 0 until min(params.size, args.size)) {
            val parameter = params[i]
            val name = parameter.identifier?.text ?: continue
            val arg = args[i]

            val argResolved = arg.reference?.resolve()
            if (argResolved is TactNamedElement) {
                // don't show hints for obvious cases
                if (argResolved.name == name) continue
            }

            val offset = arg.startOffset
            val inlayInfo = InlayInfo(name, offset)
            hints.add(inlayInfo)
        }

        return hints
    }

    override fun getDefaultBlackList() = setOf<String>()

    override fun getBlacklistExplanationHTML(): String = ""
}
