package org.tonstudio.tact.ide.build

import com.intellij.execution.lineMarker.ExecutorAction
import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.icons.AllIcons
import com.intellij.json.JsonElementTypes
import com.intellij.json.psi.JsonProperty
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType

class TactBuildProjectLineMarkerProvider : RunLineMarkerContributor() {
    private val contextActions = ExecutorAction.Companion.getActions(0)

    override fun getInfo(element: PsiElement): Info? {
        if (element.elementType != JsonElementTypes.DOUBLE_QUOTED_STRING && element.elementType != JsonElementTypes.SINGLE_QUOTED_STRING)
            return null

        val elementContent = element.text.slice(1..element.text.lastIndex - 1)
        if (elementContent != "name") return null

        val parent = element.parent.parent
        if (parent is JsonProperty) {
            if (parent.name == "name") {
                val projectName = parent.value as? JsonStringLiteral ?: return null
                return Info(AllIcons.Toolwindows.ToolWindowBuild, contextActions) { "Build project \"${projectName.value}\"" }
            }
        }

        return null
    }
}
