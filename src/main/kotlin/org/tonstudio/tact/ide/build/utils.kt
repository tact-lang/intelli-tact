package org.tonstudio.tact.ide.build

import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx
import com.intellij.openapi.fileEditor.impl.EditorWindow
import com.intellij.openapi.fileEditor.impl.LoadTextUtil
import com.intellij.openapi.fileEditor.impl.NonProjectFileWritingAccessProvider
import com.intellij.openapi.fileEditor.impl.text.PsiAwareTextEditorImpl
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import org.tonstudio.tact.notifications.TactErrorNotification
import org.tonstudio.tact.utils.pathAsPath
import java.io.File
import java.util.function.BiConsumer
import javax.swing.SwingConstants
import kotlin.io.path.Path
import kotlin.io.path.pathString

fun tryRelativizePath(project: Project, path: String): Pair<String, String?> {
    val projectDir = project.guessProjectDir() ?: return path to null
    return projectDir.pathAsPath.relativize(Path(path)).pathString to projectDir.path
}

inline fun runBackground(
    project: Project,
    @NlsContexts.ProgressTitle title: String,
    crossinline task: (indicator: ProgressIndicator) -> Unit,
) {
    ProgressManager.getInstance().run(object : Task.Backgroundable(project, title, true) {
        override fun run(indicator: ProgressIndicator) {
            indicator.isIndeterminate = true
            task(indicator)
        }
    })
}

/**
 * Opens the passed file in a separate window to the right of the current one.
 * And then calls the passed [onReady] callback function.
 *
 * @param file File to open
 */
fun openFileInRightTab(project: Project, file: File, onReady: BiConsumer<EditorWindow, VirtualFile>?) {
    val virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file)
    if (virtualFile == null) {
        TactErrorNotification("${file.absolutePath} does not exists!").show()
        return
    }

    NonProjectFileWritingAccessProvider.allowWriting(listOf(virtualFile))
    invokeLater {
        val rightWindow = splitCurrentVertically(project, virtualFile) ?: return@invokeLater

        applyEachEditor<PsiAwareTextEditorImpl>(rightWindow) { fileEditor ->
            fileEditor.editor.settings.isUseSoftWraps = true

            scrollToLine(fileEditor) { line ->
                line.contains(" f$")
            }
        }

        onReady?.accept(rightWindow, virtualFile)
    }
}

/**
 * Opens the passed file in a separate window to the right of the current one.
 * And then calls the passed [onReady] callback function.
 *
 * @param file File to open
 */
fun openFileInRightTab(project: Project, file: VirtualFile, onReady: BiConsumer<EditorWindow, VirtualFile>?) {
    NonProjectFileWritingAccessProvider.allowWriting(listOf(file))
    invokeLater {
        val rightWindow = splitCurrentVertically(project, file) ?: return@invokeLater

        applyEachEditor<PsiAwareTextEditorImpl>(rightWindow) { fileEditor ->
            fileEditor.editor.settings.isUseSoftWraps = true
        }

        onReady?.accept(rightWindow, file)
    }
}

/**
 * Splits the window vertically into two and opens the passed file in the right.
 *
 * @param virtualFile file to be opened in the right editor
 * @return [EditorWindow] that represents the right editor or null if an error occurs
 */
private fun splitCurrentVertically(project: Project, virtualFile: VirtualFile): EditorWindow? {
    val fileEditorManager = FileEditorManagerEx.getInstanceEx(project)
    val curWindow = fileEditorManager.currentWindow ?: return null
    return curWindow.split(SwingConstants.VERTICAL, false, virtualFile, true)
}

/**
 * Applies the passed function for each editor within the passed
 * window that is an editor of the specified template type [T].
 *
 * For example:
 *
 *    MyEditorUtil.applyEachEditor<PsiAwareTextEditorImpl>(editorWindow) {}
 *
 * Applies a function to each text editor in the passed window.
 */
private inline fun <reified T> applyEachEditor(editorWindow: EditorWindow, cb: (T) -> Unit) {
    editorWindow.allComposites.forEach { editorComposite ->
        editorComposite.allEditors.forEach { editor ->
            if (editor is T) {
                cb(editor)
            }
        }
    }
}

/**
 * Scrolls the passed editor to the line that first matches the passed callback.
 */
private fun scrollToLine(fileEditor: PsiAwareTextEditorImpl, isNeedLine: (String) -> Boolean) {
    scrollToLineWithShift(fileEditor, 0, isNeedLine)
}

/**
 * Scrolls the passed editor to the line that first matches the passed callback + shift.
 */
fun scrollToLineWithShift(fileEditor: PsiAwareTextEditorImpl, shift: Int, isNeedLine: (String) -> Boolean) {
    val editor = fileEditor.editor
    val text = LoadTextUtil.loadText(fileEditor.file)

    var needLineIndex = 0

    text.split("\n").forEachIndexed { index, line ->
        if (needLineIndex == 0 && isNeedLine(line)) {
            needLineIndex = index
            return@forEachIndexed
        }
    }

    editor.caretModel.moveToLogicalPosition(
        LogicalPosition(needLineIndex + shift, 0)
    )

    val scrollingModel = editor.scrollingModel
    scrollingModel.disableAnimation()
    scrollingModel.scrollToCaret(ScrollType.CENTER_UP)
    scrollingModel.enableAnimation()
}
