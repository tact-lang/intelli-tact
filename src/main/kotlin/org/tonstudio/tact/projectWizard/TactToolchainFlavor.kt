package org.tonstudio.tact.projectWizard

import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.openapi.project.Project
import java.nio.file.Path
import kotlin.io.path.isDirectory

abstract class TactToolchainFlavor {

    fun suggestHomePaths(project: Project): Sequence<Path> = getHomePathCandidates(project).filter { isValidToolchainPath(it) }

    protected abstract fun getHomePathCandidates(project: Project): Sequence<Path>

    /**
     * Flavor is added to result in [getApplicableFlavors] if this method returns true.
     * @return whether this flavor is applicable.
     */
    protected open fun isApplicable(): Boolean = true

    /**
     * Checks if the path is the name of a Tact toolchain of this flavor.
     *
     * @param path path to check.
     * @return true if paths point to a valid home.
     */
    protected open fun isValidToolchainPath(path: Path): Boolean {
        return path.isDirectory() && hasExecutable(path, "bin/tact.js") && path.resolve("dist/stdlib/stdlib").isDirectory()
    }

    protected open fun hasExecutable(path: Path, toolName: String): Boolean = path.isJavaScriptExecutable(toolName)

    companion object {
        private val EP_NAME: ExtensionPointName<TactToolchainFlavor> =
            ExtensionPointName.create("org.tonstudio.tact.toolchainFlavor")

        fun getApplicableFlavors(): List<TactToolchainFlavor> =
            EP_NAME.extensionList.filter { it.isApplicable() }
    }
}
