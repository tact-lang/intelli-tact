package org.tonstudio.tact.ide.hints

import com.intellij.codeInsight.codeVision.CodeVisionAnchorKind
import com.intellij.codeInsight.codeVision.CodeVisionRelativeOrdering
import com.intellij.codeInsight.hints.codeVision.ReferencesCodeVisionProvider
import com.intellij.codeInsight.navigation.actions.GotoDeclarationAction
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.ui.awt.RelativePoint
import com.intellij.util.Processor
import org.tonstudio.tact.lang.psi.*
import java.awt.event.MouseEvent
import java.util.concurrent.atomic.AtomicInteger

class TactUsagesCodeVisionProvider : ReferencesCodeVisionProvider() {
    companion object {
        private const val ID = "TactUsagesCodeVisionProvider"
    }

    override val defaultAnchor: CodeVisionAnchorKind = CodeVisionAnchorKind.Right
    override val id: String = ID
    override val name: String = "TactUsagesCodeVisionProvider"
    override val relativeOrderings = listOf(CodeVisionRelativeOrdering.CodeVisionRelativeOrderingFirst)

    override fun acceptsFile(file: PsiFile): Boolean = file is TactFile

    override fun acceptsElement(element: PsiElement): Boolean {
        return element is TactFunctionDeclaration ||
                element is TactAsmFunctionDeclaration ||
                element is TactNativeFunctionDeclaration ||
                element is TactStructDeclaration ||
                element is TactTraitDeclaration ||
                element is TactMessageDeclaration ||
                element is TactConstDeclaration
    }

    override fun getHint(element: PsiElement, file: PsiFile): String {
        val usagesCount = AtomicInteger()
        ReferencesSearch.search(element, element.useScope).allowParallelProcessing()
            .forEach(Processor { usagesCount.incrementAndGet() <= 500 })

        return when (val result = usagesCount.get()) {
            0    -> " no usages"
            1    -> " 1 usage"
            else -> " $result usages"
        }
    }

    override fun handleClick(editor: Editor, element: PsiElement, event: MouseEvent?) {
        GotoDeclarationAction.startFindUsages(
            editor, element.project,
            element, if (event == null) null else RelativePoint(event)
        )
    }
}
