package org.tonstudio.tact.lang.search

import com.intellij.lang.LanguageCodeInsightActionHandler
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.tonstudio.tact.lang.psi.TactConstDeclaration
import org.tonstudio.tact.lang.psi.TactFile
import org.tonstudio.tact.lang.psi.TactFunctionDeclaration

class TactGotoSuperHandler : LanguageCodeInsightActionHandler {
    override fun startInWriteAction() = false

    override fun isValidFor(editor: Editor, file: PsiFile?) = file is TactFile

    override fun invoke(project: Project, editor: Editor, file: PsiFile) {
        val element = file.findElementAt(editor.caretModel.offset) ?: return
        showPopup(element)
    }

    private fun showPopup(element: PsiElement) {
        val parent = element.parent ?: return
        if (parent is TactFunctionDeclaration) {
            showSuperMethodPopup(null, parent)
        }
        if (parent is TactConstDeclaration) {
            showSuperConstantPopup(null, parent)
        }
    }
}
