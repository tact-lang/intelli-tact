package org.tonstudio.tact.ide.build

import com.intellij.execution.lineMarker.ExecutorAction
import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import org.tonstudio.tact.lang.TactTypes
import org.tonstudio.tact.lang.psi.TactContractType

class TactBuildLineMarkerProvider : RunLineMarkerContributor() {
    private val contextActions = ExecutorAction.Companion.getActions(0)

    override fun getInfo(element: PsiElement): Info? {
        if (element.elementType == TactTypes.IDENTIFIER) {
            val parent = element.parent
            if (parent is TactContractType) {
                return Info(AllIcons.Toolwindows.ToolWindowBuild, contextActions)
            }

            return null
        }

        return null
    }
}
