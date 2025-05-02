package org.tonstudio.tact.project

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.EditorNotificationPanel
import com.intellij.ui.EditorNotificationProvider
import com.intellij.ui.EditorNotifications
import java.util.function.Function

abstract class TactEditorNotificationProviderBase(private val project: Project) : EditorNotificationProvider, DumbAware {
    abstract val disablingKey: String

    final override fun collectNotificationData(
        project: Project,
        file: VirtualFile,
    ): Function<in FileEditor, out EditorNotificationPanel?> {
        return Function { editor -> createNotificationPanel(file, editor, project) }
    }

    protected abstract fun createNotificationPanel(
        file: VirtualFile,
        editor: FileEditor,
        project: Project,
    ): EditorNotificationPanel?

    fun update(project: Project, file: VirtualFile) {
        EditorNotifications.getInstance(project).updateNotifications(file)
    }

    fun updateAllNotifications() {
        EditorNotifications.getInstance(project).updateAllNotifications()
    }

    fun disableNotification() {
        PropertiesComponent.getInstance(project).setValue(disablingKey, true)
    }

    protected fun isNotificationDisabled(): Boolean =
        PropertiesComponent.getInstance(project).getBoolean(disablingKey)
}
