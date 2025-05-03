package org.tonstudio.tact.lang.imports

import com.intellij.codeInsight.daemon.ReferenceImporter
import com.intellij.codeInsight.daemon.impl.CollectHighlightsUtil
import com.intellij.codeInsight.daemon.impl.DaemonListeners
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import com.intellij.util.ThreeState
import org.tonstudio.tact.lang.TactLanguage
import org.tonstudio.tact.lang.psi.TactCompositeElement

class TactReferenceImporter : ReferenceImporter {
    override fun autoImportReferenceAtCursor(editor: Editor, file: PsiFile): Boolean {
        if (
            !file.viewProvider.languages.contains(TactLanguage) ||
            !DaemonListeners.canChangeFileSilently(file, true, ThreeState.UNSURE)
        ) {
            return false
        }

        val caretOffset = editor.caretModel.offset
        val document = editor.document
        val lineNumber = document.getLineNumber(caretOffset)
        val startOffset = document.getLineStartOffset(lineNumber)
        val endOffset = document.getLineEndOffset(lineNumber)

        val elements = CollectHighlightsUtil.getElementsInRange(file, startOffset, endOffset)
        for (element in elements) {
            if (element !is TactCompositeElement) continue

            for (reference in element.references) {
                val fix = TactImportFileQuickFix(reference)
                if (fix.doAutoImportOrShowHint(editor, true)) {
                    return true
                }
            }
        }

        return false
    }

    override fun isAddUnambiguousImportsOnTheFlyEnabled(file: PsiFile): Boolean = true
}
