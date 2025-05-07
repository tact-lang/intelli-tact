package org.tonstudio.tact.configurations

import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessAdapter
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.logger
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlin.io.path.exists

object TactConfigurationUtil {
    private val LOG = logger<TactConfigurationUtil>()

    const val TOOLCHAIN_NOT_SETUP = "Tact executable not found, toolchain not setup correctly?"
    const val UNDEFINED_VERSION = "N/A"
    const val STANDARD_LIB_PATH = "dist/stdlib/stdlib"
    const val COMPILER_REPO_STANDARD_LIB_PATH = "src/stdlib/stdlib"
    const val STANDARD_TACT_COMPILER = "./bin/tact.js"

    fun getStdlibLocation(path: String): String? {
        if (path.isBlank()) {
            return null
        }
        return Path.of(path, STANDARD_LIB_PATH).toString()
    }

    fun guessToolchainVersion(path: String): String {
        if (path.isBlank()) {
            return UNDEFINED_VERSION
        }

        val binPath = Path.of(path, STANDARD_TACT_COMPILER)
        if (!binPath.exists()) {
            return UNDEFINED_VERSION
        }

        val cmd = GeneralCommandLine()
            .withExePath(binPath.toAbsolutePath().toString())
            .withParameters("-v")
            .withCharset(StandardCharsets.UTF_8)

        val processOutput = StringBuilder()
        try {
            val handler = OSProcessHandler(cmd)
            handler.addProcessListener(object : CapturingProcessAdapter() {
                override fun processTerminated(event: ProcessEvent) {
                    if (event.exitCode != 0) {
                        LOG.warn("Couldn't get Tact toolchain version: " + output.stderr)
                    } else {
                        processOutput.append(output.stdout)
                    }
                }
            })
            handler.startNotify()
            val future = ApplicationManager.getApplication().executeOnPooledThread {
                handler.waitFor()
            }
            future.get(300, TimeUnit.MILLISECONDS)
        } catch (e: ExecutionException) {
            LOG.warn("Can't execute command for getting Tact toolchain version", e)
        } catch (e: TimeoutException) {
            LOG.warn("Can't execute command for getting Tact toolchain version", e)
        }
        val result = processOutput.toString()

        val version = result.trim()
        return "Tact " + version.ifEmpty { UNDEFINED_VERSION }
    }
}
