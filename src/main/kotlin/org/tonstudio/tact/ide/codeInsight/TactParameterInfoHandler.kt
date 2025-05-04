package org.tonstudio.tact.ide.codeInsight

import com.intellij.codeInsight.CodeInsightBundle
import com.intellij.lang.parameterInfo.*
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.parentOfType
import org.tonstudio.tact.lang.TactTypes
import org.tonstudio.tact.lang.psi.*
import org.tonstudio.tact.lang.psi.types.TactFunctionTypeEx
import org.tonstudio.tact.lang.psi.types.TactTypeEx

class TactParameterInfoHandler : ParameterInfoHandlerWithTabActionSupport<TactArgumentList, TactTypeEx?, TactExpression> {
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

    override fun updateUI(type: TactTypeEx?, context: ParameterInfoUIContext) {
        updatePresentation(type, context)
    }

    override fun showParameterInfo(argList: TactArgumentList, context: CreateParameterInfoContext) {
        val parent = argList.parent as? TactCallExpr ?: return
        val type = parent.expression!!.getType(null) as? TactFunctionTypeEx ?: return

        context.itemsToShow = arrayOf(type)
        context.showHint(argList, argList.textRange.startOffset, this)
    }

    private fun getArgumentList(context: ParameterInfoContext): TactArgumentList? {
        return context.file.findElementAt(context.offset)?.parentOfType()
    }

    companion object {
        fun updatePresentation(type: TactTypeEx?, context: ParameterInfoUIContext): String? {
            if (type == null) {
                context.isUIComponentEnabled = false
                return null
            }

            val signature = if (type is TactFunctionTypeEx) type.signature else null
            if (signature == null) {
                context.isUIComponentEnabled = false
                return null
            }

            val isSelfMethod = if (type is TactFunctionTypeEx) type.isSelfMethod() else false
            val parameters = signature.parameters

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

        private fun getParameterPresentations(parameters: TactParameters, isSelfMethod: Boolean): List<String> {
            val startIndex = if (isSelfMethod) 1 else 0
            val paramDeclarations = parameters.paramDefinitionList

            val paramPresentations = mutableListOf<String>()
            for (paramDefinition in paramDeclarations.slice(startIndex..paramDeclarations.lastIndex)) {
                paramPresentations.add(paramDefinition.name + ": " + paramDefinition.type.text)
            }
            return paramPresentations
        }
    }
}
