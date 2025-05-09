package org.tonstudio.tact.asm

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import org.tonstudio.tact.toolchain.TactCapturingProcessHandler
import org.tonstudio.tact.toolchain.TactToolchainService.Companion.toolchainSettings
import org.tonstudio.tact.utils.runProcessWithGlobalProgress
import java.nio.charset.StandardCharsets
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit

class TonBocDisassembler(private val project: Project) {
    fun disassemble(path: String): String? {
        val unbocPath = project.toolchainSettings.toolchain().rootDir()?.findChild("bin")?.findChild("unboc.js") ?:
            return null

        val commandLine = GeneralCommandLine()
            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            .withExePath(unbocPath.path)
            .withParameters(path)

        val handler = TactCapturingProcessHandler(commandLine.withCharset(StandardCharsets.UTF_8))

        val future = ApplicationManager.getApplication().executeOnPooledThread(Callable {
            handler.runProcessWithGlobalProgress(timeoutInMilliseconds = 5000)
        })

        val output = future.get(5, TimeUnit.SECONDS)
        if (output.isCancelled) {
            return null
        }
        if (output.exitCode != 0 && output.stderr.trim().isNotEmpty()) {
            return null
        }

        return output.stdout
    }
}
