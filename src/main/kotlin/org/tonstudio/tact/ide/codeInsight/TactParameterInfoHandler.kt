package org.tonstudio.tact.ide.codeInsight

import com.intellij.codeInsight.CodeInsightBundle
import com.intellij.lang.parameterInfo.*
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.parentOfType
import org.tonstudio.tact.lang.TactTypes
import org.tonstudio.tact.lang.psi.*

class TactParameterInfoHandler : ParameterInfoHandlerWithTabActionSupport<TactArgumentList, List<TactFieldOrParameter>, TactExpression> {
    override fun getActualParameters(list: TactArgumentList) = list.expressionList.toTypedArray()
    override fun getActualParameterDelimiterType(): IElementType = TactTypes.COMMA
    override fun getActualParametersRBraceType(): IElementType = TactTypes.RPAREN
    override fun getArgumentListAllowedParentClasses() = setOf<Class<*>>()
    override fun getArgListStopSearchClasses() = setOf<Class<*>>()
    override fun getArgumentListClass() = TactArgumentList::class.java
    override fun findElementForParameterInfo(context: CreateParameterInfoContext) = getArgumentList(context)
    override fun findElementForUpdatingParameterInfo(context: UpdateParameterInfoContext) = getArgumentList(context)

    override fun updateParameterInfo(list: TactArgumentList, context: UpdateParameterInfoContext) {
        context.setCurrentParameter(ParameterInfoUtils.getCurrentParameterIndex(list.node, context.offset, TactTypes.COMMA))
    }

    override fun updateUI(parameters: List<TactFieldOrParameter>, context: ParameterInfoUIContext) {
        updatePresentation(parameters, context)
    }

    override fun showParameterInfo(argList: TactArgumentList, context: CreateParameterInfoContext) {
        val expr = argList.parent
        if (expr is TactCallExpr) {
            val (signature, _) = expr.resolveSignature() ?: return
            val parameters = signature.parameters.paramDefinitionList
            context.itemsToShow = arrayOf(parameters)
        }
        if (expr is TactInitOfExpr) {
            val resolved = expr.resolve() ?: return
            context.itemsToShow = arrayOf(resolved)
        }
        context.showHint(argList, argList.textRange.startOffset, this)
    }

    private fun getArgumentList(context: ParameterInfoContext): TactArgumentList? {
        return context.file.findElementAt(context.offset)?.parentOfType()
    }

    companion object {
        fun updatePresentation(parameters: List<TactFieldOrParameter>, context: ParameterInfoUIContext): String? {
            val isSelfMethod = parameters.firstOrNull()?.identifier?.textMatches("self") ?: false

            val parametersPresentations = getParameterPresentations(parameters, isSelfMethod)
            val builder = StringBuilder()

            var startHighlighting = 0
            var endHighlighting = 0

            if (parametersPresentations.isNotEmpty()) {
                val selected = context.currentParameterIndex

                for (i in parametersPresentations.indices) {
                    if (i != 0) {
                        builder.append(", ")
                    }
                    if (i == selected) {
                        startHighlighting = builder.length
                    }
                    builder.append(parametersPresentations[i])
                    if (i == selected) {
                        endHighlighting = builder.length
                    }
                }
            } else {
                builder.append(CodeInsightBundle.message("parameter.info.no.parameters"))
            }

            return context.setupUIComponentPresentation(
                builder.toString(), startHighlighting, endHighlighting,
                false, false,
                false, context.defaultParameterColor,
            )
        }

        private fun getParameterPresentations(parameters: List<TactFieldOrParameter>, isSelfMethod: Boolean): List<String> {
            val startIndex = if (isSelfMethod) 1 else 0

            val presentations = mutableListOf<String>()
            for (param in parameters.slice(startIndex..parameters.lastIndex)) {
                val name = param.identifier?.text ?: "unnamed"
                val type = param.type?.text ?: "unknown"
                presentations.add("$name: $type")
            }
            return presentations
        }
    }
}
