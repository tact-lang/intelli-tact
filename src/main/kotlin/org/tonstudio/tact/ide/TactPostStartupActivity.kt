package org.tonstudio.tact.ide

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import org.tonstudio.tact.projectWizard.TactToolchainFlavor
import org.tonstudio.tact.toolchain.TactKnownToolchainsState
import org.tonstudio.tact.toolchain.TactToolchain
import org.tonstudio.tact.toolchain.TactToolchainService.Companion.toolchainSettings

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
            val toolchainCandidates = TactToolchainFlavor.getApplicableFlavors()
                .flatMap { it.suggestHomePaths(project) }
                .distinct()
                .map { it.toString() }

            if (toolchainCandidates.isEmpty()) {
                return emptyList()
            }

            TactKnownToolchainsState.getInstance().knownToolchains = toolchainCandidates.toSet()
            return toolchainCandidates
        }
    }
}
