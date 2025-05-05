package org.tonstudio.tact.ide.build.single

import com.intellij.execution.ExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner
import org.tonstudio.tact.configurations.TactConfigurationUtil
import org.tonstudio.tact.ide.build.TactProcessHandler
import org.tonstudio.tact.ide.build.tryRelativizePath
import org.tonstudio.tact.toolchain.TactToolchainService.Companion.toolchainSettings

class TactBuildConfigurationRunState(
    val env: ExecutionEnvironment,
    val conf: TactBuildConfiguration,
) : RunProfileState {

    override fun execute(executor: Executor, runner: ProgramRunner<*>): ExecutionResult? {
        val state = object : CommandLineState(env) {
            override fun startProcess(): ProcessHandler {
                val project = env.project

                val root = project.toolchainSettings.toolchain().rootDir()
                if (root == null) {
                    throw Error("Can't build: ${TactConfigurationUtil.TOOLCHAIN_NOT_SETUP}")
                }

                val tactCompiler = root.findChild("bin")?.findChild("tact.js")
                if (tactCompiler == null) {
                    throw Error("Can't build: Cannot find Tact compiler executable")
                }

                val (contractPath, projectRoot) = tryRelativizePath(project, conf.fileName)

                val commandLine = GeneralCommandLine()
                    .withExePath(tactCompiler.path)
                    .withWorkDirectory(projectRoot)
                    .withCharset(Charsets.UTF_8)
                    .withParameters(contractPath)
                    .withRedirectErrorStream(true)

                return TactProcessHandler(commandLine)
            }
        }

        return state.execute(executor, runner)
    }
}
