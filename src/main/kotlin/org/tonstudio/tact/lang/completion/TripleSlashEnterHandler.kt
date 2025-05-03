package org.tonstudio.tact.lang.completion

import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate.Result
import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegateAdapter
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.psi.PsiFile
import com.intellij.util.text.CharArrayUtil
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.util.Ref
import com.intellij.psi.util.startOffset
import org.tonstudio.tact.lang.doc.psi.TactDocComment
import org.tonstudio.tact.utils.inside

class TripleSlashEnterHandler : EnterHandlerDelegateAdapter() {
    override fun preprocessEnter(
        file: PsiFile,
        editor: Editor,
        caretOffset: Ref<Int>,
        caretAdvance: Ref<Int>,
        dataContext: DataContext,
        originalHandler: EditorActionHandler?,
    ): Result {
        val project = file.project
        val manager = InjectedLanguageManager.getInstance(project)

        val (originalFile, shift) = if (manager.isInjectedFragment(file)) {
            val injectedHost = manager.getInjectionHost(file)
            (injectedHost?.containingFile ?: file) to (injectedHost?.startOffset ?: 0)
        } else {
            file to 0
        }

        val injected = shift > 0
        val doc: Document = editor.document
        val caret = editor.caretModel.currentCaret
        val offset: Int = shift + caret.offset

        val element = originalFile.findElementAt(offset - 1) ?: return Result.Continue
        if (!element.inside<TactDocComment>()) {
            return Result.Continue
        }

        val lineStart = doc.getLineStartOffset(doc.getLineNumber(offset))
        val indentSize = CharArrayUtil.shiftForward(doc.charsSequence, lineStart, " \t")
        val indent = doc.charsSequence.subSequence(lineStart, indentSize).toString()

        originalHandler?.execute(editor, caret, dataContext)

        val insert = if (injected) "$indent/// " else "/// "
        WriteCommandAction.runWriteCommandAction(project) {
            doc.insertString(caret.offset, insert)
        }
        caret.moveToOffset(caret.offset + insert.length)

        return Result.Stop
    }

    override fun postProcessEnter(p0: PsiFile, p1: Editor, p2: DataContext): Result {
        return Result.Continue
    }
}
