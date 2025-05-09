package org.tonstudio.tact.ide.build.single

import com.intellij.execution.ExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessListener
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.findPsiFile
import org.tonstudio.tact.configurations.TactConfigurationUtil
import org.tonstudio.tact.ide.build.TactProcessHandler
import org.tonstudio.tact.ide.build.tryRelativizePath
import org.tonstudio.tact.lang.psi.TactFile
import org.tonstudio.tact.toolchain.TactToolchainService.Companion.toolchainSettings
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.io.path.Path

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

                val handler = TactProcessHandler(commandLine)

                handler.addProcessListener(object : ProcessListener {
                    override fun processTerminated(event: ProcessEvent) {
                        moveBuildArtifacts()
                    }

                    private fun moveBuildArtifacts() {
                        val root = Path(conf.fileName).parent.toString()
                        val filename = Path(conf.fileName).fileName.toString()

                        val extensions = "(abi|code\\.boc|fc|fif|md|pkg|ts)"
                        val contracts = runReadAction { definedContracts(conf.fileName) }
                        val contractsRe = if (contracts.isNotEmpty()) contracts.joinToString("|", "(", ")") else ".*?"
                        val regex = Regex("${filename}_$contractsRe\\.$extensions")

                        val srcDir = Path(root)
                        val outDir = srcDir.resolve("output")
                        Files.createDirectories(outDir)

                        Files.list(srcDir).use { stream ->
                            stream.filter { Files.isRegularFile(it) }
                                .filter { regex.matches(it.fileName.toString()) }
                                .forEach { file ->
                                    Files.move(
                                        file,
                                        outDir.resolve(file.fileName),
                                        StandardCopyOption.REPLACE_EXISTING
                                    )
                                }
                        }
                    }
                })


                return handler
            }

            private fun definedContracts(filepath: String): List<String> {
                val file = VfsUtil.findFile(Path(filepath), true) ?: return emptyList()
                val psiFile = file.findPsiFile(env.project) as? TactFile ?: return emptyList()
                val contracts = psiFile.getContracts()
                return contracts.map { it.name }
            }
        }

        return state.execute(executor, runner)
    }
}
