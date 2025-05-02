package org.tonstudio.tact.project

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.EditorNotificationPanel
import org.tonstudio.tact.configurations.TactProjectSettingsConfigurable
import org.tonstudio.tact.toolchain.TactToolchain
import org.tonstudio.tact.toolchain.TactToolchainService.Companion.toolchainSettings
import org.tonstudio.tact.utils.isNotTactFile
import org.tonstudio.tact.utils.toPath
import java.nio.file.Files

class TactMissingToolchainNotificationProvider(project: Project) : TactEditorNotificationProviderBase(project) {
    override val disablingKey: String get() = NOTIFICATION_STATUS_KEY

    override fun createNotificationPanel(file: VirtualFile, editor: FileEditor, project: Project): EditorNotificationPanel? {
        if (file.isNotTactFile || isNotificationDisabled()) {
            return null
        }

        val toolchain = project.toolchainSettings.toolchain()
        if (toolchain == TactToolchain.NULL) {
            return createToolchainNotification("Tact toolchain is not defined, try to install dependencies with npm/yarn and then setup", project, file)
        }

        val homePath = toolchain.homePath()
        if (!Files.exists(homePath.toPath())) {
            return createToolchainNotification("Tact toolchain folder $homePath is not exist", project, file)
        }

        return null
    }

    private fun createToolchainNotification(
        message: String,
        project: Project,
        file: VirtualFile,
    ): EditorNotificationPanel =
        EditorNotificationPanel().apply {
            text = message
            createActionLabel("Setup Tact toolchain") {
                TactProjectSettingsConfigurable.show(project)
                update(project, file)
            }
            createActionLabel("Don't show again") {
                disableNotification()
                updateAllNotifications()
            }
        }

    companion object {
        private const val NOTIFICATION_STATUS_KEY = "org.tonstudio.tact.hideToolchainNotifications"
    }
}
