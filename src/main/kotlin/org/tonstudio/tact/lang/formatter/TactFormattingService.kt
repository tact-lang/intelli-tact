package org.tonstudio.tact.lang.formatter

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessAdapter
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessEvent
import com.intellij.formatting.service.AsyncDocumentFormattingService
import com.intellij.formatting.service.AsyncFormattingRequest
import com.intellij.formatting.service.FormattingService
import com.intellij.psi.PsiFile
import org.tonstudio.tact.configurations.TactConfigurationUtil
import org.tonstudio.tact.lang.psi.TactFile
import org.tonstudio.tact.toolchain.TactToolchainService.Companion.toolchainSettings
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.util.*
import java.util.concurrent.ExecutionException

class TactFormattingService : AsyncDocumentFormattingService() {
    override fun getFeatures(): MutableSet<FormattingService.Feature> = EnumSet.noneOf(FormattingService.Feature::class.java)

    override fun canFormat(file: PsiFile) = file is TactFile

    override fun getNotificationGroupId() = "TactNotifications"

    override fun getName() = "tactfmt"

    override fun getTimeout(): Duration = Duration.ofSeconds(5)

    override fun createFormattingTask(request: AsyncFormattingRequest): FormattingTask? {
        val ioFile = request.ioFile ?: return null
        val params = mutableListOf<String>()
        params.add(ioFile.path)

        return try {
            val root = request.context.project.toolchainSettings.toolchain().rootDir()
            if (root == null) {
                request.onError("Can't format", TactConfigurationUtil.TOOLCHAIN_NOT_SETUP)
                return null
            }

            val tactFmt = root.findChild("bin")?.findChild("tact-fmt.js")
            if (tactFmt == null) {
                request.onError("Can't format", "Looks like you are using too old Tact version without Tact Formatter")
                return null
            }

            val commandLine = GeneralCommandLine()
                .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
                .withExePath(tactFmt.path)
                .withParameters(params)

            val handler = OSProcessHandler(commandLine.withCharset(StandardCharsets.UTF_8))

            object : FormattingTask {
                override fun run() {
                    handler.addProcessListener(object : CapturingProcessAdapter() {
                        override fun processTerminated(event: ProcessEvent) {
                            val exitCode = event.exitCode
                            if (exitCode == 0) {
                                request.onTextReady(output.stdout.removeSuffix("\n"))
                            } else {
                                request.onError("Can't format", output.stderr)
                            }
                        }
                    })
                    handler.startNotify()
                }

                override fun cancel(): Boolean {
                    handler.destroyProcess()
                    return true
                }

                override fun isRunUnderProgress(): Boolean = true
            }
        } catch (e: ExecutionException) {
            request.onError("Can't format", e.message ?: "")
            null
        }
    }
}
