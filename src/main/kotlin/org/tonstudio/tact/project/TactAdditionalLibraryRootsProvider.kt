package org.tonstudio.tact.project

import com.intellij.icons.AllIcons
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.AdditionalLibraryRootsProvider
import com.intellij.openapi.roots.SyntheticLibrary
import com.intellij.openapi.vfs.VirtualFile
import org.tonstudio.tact.configurations.TactConfiguration
import org.tonstudio.tact.ide.ui.Icons
import org.tonstudio.tact.toolchain.TactToolchain
import org.tonstudio.tact.toolchain.TactToolchainService.Companion.toolchainSettings
import javax.swing.Icon

class TactAdditionalLibraryRootsProvider : AdditionalLibraryRootsProvider() {
    open class LibraryBase(private val name: String, private val sourceRoot: VirtualFile?, private val icon: Icon = Icons.Tact) :
        SyntheticLibrary(), ItemPresentation {
        override fun getSourceRoots() = if (sourceRoot == null) emptyList() else listOf(sourceRoot)

        override fun getPresentableText() = name

        override fun getIcon(unused: Boolean) = icon

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as LibraryBase

            if (name != other.name) return false
            return sourceRoot == other.sourceRoot
        }

        override fun hashCode() = sourceRoot.hashCode()
    }

    class StandardLibrary(toolchain: TactToolchain) :
        LibraryBase(toolchain.name(), toolchain.stdlibDir())

    class Stubs(sourceRoot: VirtualFile) : LibraryBase("Tact Stubs", sourceRoot, AllIcons.Nodes.PpLibFolder)

    override fun getAdditionalProjectLibraries(project: Project): Collection<SyntheticLibrary> {
        val result = mutableListOf<SyntheticLibrary>()

        val toolchain = project.toolchainSettings.toolchain()
        result.add(StandardLibrary(toolchain))
        val stubs = getStubs(project)
        if (stubs != null) {
            result.add(stubs)
        }

        return result
    }

    private fun getStubs(project: Project): Stubs? {
        val path = TactConfiguration.getInstance(project).stubsLocation ?: return null
        return Stubs(path)
    }

    override fun getRootsToWatch(project: Project): Collection<VirtualFile> {
        if (ApplicationManager.getApplication().isUnitTestMode) return emptySet()

        val result = mutableListOf<VirtualFile>()

        val toolchain = project.toolchainSettings.toolchain()
        val sourceRoot = toolchain.stdlibDir()
        if (sourceRoot != null) {
            result.add(sourceRoot)
        }

        val stubs = getStubs(project)
        if (stubs != null) {
            result.add(stubs.sourceRoots.first())
        }

        return result
    }
}
