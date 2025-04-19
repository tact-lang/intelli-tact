package org.tonstudio.tact.ide

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.startup.ProjectActivity
import org.tonstudio.tact.toolchain.TactKnownToolchainsState
import org.tonstudio.tact.toolchain.TactToolchain
import org.tonstudio.tact.toolchain.TactToolchainService.Companion.toolchainSettings
import kotlin.io.path.Path
import kotlin.io.path.exists

class TactPostStartupActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        val knownToolchains = TactKnownToolchainsState.getInstance().knownToolchains
        val toolchainSettings = project.toolchainSettings

        val needFindToolchains = knownToolchains.isEmpty()
        if (needFindToolchains) {
            val toolchainCandidates = Util.setupToolchainCandidates(project)
            if (toolchainCandidates.isEmpty()) {
                return
            }

            toolchainSettings.setToolchain(project, TactToolchain.fromPath(toolchainCandidates.first()))
            return
        }

        if (toolchainSettings.isNotSet() && knownToolchains.isNotEmpty()) {
            toolchainSettings.setToolchain(project, TactToolchain.fromPath(knownToolchains.first()))
        }
    }

    object Util {
        fun setupToolchainCandidates(project: Project): List<String> {
            val projectDir = project.guessProjectDir() ?: return emptyList()

            val toolchainCandidates = listOf(
                projectDir.path + "/node_modules/@tact-lang/compiler",
                projectDir.path + "/" // compiler repo
            ).filter { path -> Path(path).exists() }

            if (toolchainCandidates.isEmpty()) {
                return emptyList()
            }

            TactKnownToolchainsState.getInstance().knownToolchains = toolchainCandidates.toSet()
            return toolchainCandidates
        }
    }
}
