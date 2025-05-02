package org.tonstudio.tact.toolchain

import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessHandler
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project
import com.intellij.util.DocumentUtil
import com.intellij.util.execution.ParametersListUtil
import com.intellij.util.io.BaseOutputReader
import org.tonstudio.tact.configurations.TactConfigurationUtil
import org.tonstudio.tact.configurations.TactFmtSettingsState.Companion.tactfmtSettings
import org.tonstudio.tact.notifications.TactErrorNotification
import org.tonstudio.tact.toolchain.TactToolchainService.Companion.toolchainSettings
import org.tonstudio.tact.utils.isNotTactFile
import org.tonstudio.tact.utils.runProcessWithGlobalProgress
import org.tonstudio.tact.utils.virtualFile
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit

object TactFmt {
    fun reformatDocument(project: Project, document: Document, onFail: Runnable? = null) {
        ApplicationManager.getApplication().assertIsDispatchThread()
        if (!document.isWritable) return
        val formattedText = reformatDocumentTextOrNull(project, document, onFail) ?: return
        DocumentUtil.writeInRunUndoTransparentAction { document.setText(formattedText) }
    }

    private fun reformatDocumentTextOrNull(project: Project, document: Document, onFail: Runnable? = null): String? {
        val cmd = createCommandLine(project, document) ?: return null
        return runProcess(cmd, document, project, onFail)
    }

    private fun runProcess(
        cmd: GeneralCommandLine,
        document: Document,
        project: Project,
        onFail: Runnable?,
    ): String? {
        val processOutput = StringBuilder()
        try {
            val handler = TactCapturingProcessHandler(cmd)

            handler.processInput.write(document.text.toByteArray())
            handler.processInput.flush()
            handler.processInput.close()

            val future = ApplicationManager.getApplication().executeOnPooledThread(Callable {
                handler.runProcessWithGlobalProgress(timeoutInMilliseconds = 5000)
            })

            val output = future.get(5, TimeUnit.SECONDS)
            if (output.isCancelled) {
                return null
            }
            if (output.exitCode != 0 && output.stderr.trim().isNotEmpty()) {
                TactErrorNotification(output.stderr)
                    .withTitle("Can't reformat")
                    .show(project)
                onFail?.run()
                return null
            }

            processOutput.append(output.stdout)
        } catch (e: ExecutionException) {
            TactErrorNotification(e.message ?: "")
                .withTitle("Can't reformat")
                .show(project)
            onFail?.run()
            return null
        }

        return processOutput.toString()
    }

    private fun createCommandLine(project: Project, document: Document): GeneralCommandLine? {
        val file = document.virtualFile ?: return null
        if (file.isNotTactFile || !file.isValid) return null

        val settingsState = project.tactfmtSettings
        val additionalArguments = ParametersListUtil.parse(settingsState.additionalArguments)
        val arguments = mutableListOf<String>()
        arguments.add(file.path)
        arguments.addAll(additionalArguments)

        val root = project.toolchainSettings.toolchain().rootDir()
        if (root == null) {
            TactErrorNotification(TactConfigurationUtil.TOOLCHAIN_NOT_SETUP)
                .withTitle("Can't reformat")
                .show(project)
            return null
        }

        val tactFmt = root.findChild("bin")?.findChild("tact-fmt.js")
        if (tactFmt == null) {
            TactErrorNotification("Looks like you are using too old Tact version without Tact Formatter")
                .withTitle("Can't reformat")
                .show(project)
            return null
        }

        return GeneralCommandLine()
            .withExePath(tactFmt.path)
            .withParameters(arguments)
            .withEnvironment(settingsState.envs)
            .withCharset(Charsets.UTF_8)
    }
}

class TactCapturingProcessHandler(commandLine: GeneralCommandLine) : CapturingProcessHandler(commandLine) {
    override fun readerOptions(): BaseOutputReader.Options = BaseOutputReader.Options.BLOCKING
}
