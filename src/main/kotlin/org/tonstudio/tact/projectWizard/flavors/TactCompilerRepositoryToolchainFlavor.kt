package org.tonstudio.tact.projectWizard.flavors

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import org.tonstudio.tact.projectWizard.TactToolchainFlavor
import org.tonstudio.tact.utils.toPath
import java.nio.file.Path
import kotlin.io.path.isDirectory

class TactCompilerRepositoryToolchainFlavor : TactToolchainFlavor() {
    override fun getHomePathCandidates(project: Project): Sequence<Path> {
        val projectDir = project.guessProjectDir() ?: return emptySequence()
        return sequenceOf(projectDir.path)
            .map { it.toPath() }
            .filter { it.isDirectory() }
    }
}
