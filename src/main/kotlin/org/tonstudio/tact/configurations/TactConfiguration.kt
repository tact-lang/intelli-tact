package org.tonstudio.tact.configurations

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager

@Service(Service.Level.PROJECT)
class TactConfiguration(private val project: Project) {
    companion object {
        fun getInstance(project: Project) = project.service<TactConfiguration>()
    }

    private var stubs: VirtualFile? = null

    private val toolchain
        get() = null // TODO project.toolchainSettings.toolchain()

    val stdlibLocation: VirtualFile?
        get() = findFileByUrl("node_modules/@tact-lang/compiler/stdlib/std")

    val builtinLocation: VirtualFile?
        get() = null // TODO

    val srcLocation: VirtualFile?
        get() = findFileInProject("src")

    val localModulesLocation: VirtualFile?
        get() = findFileByUrl("node_modules/@tact-lang/compiler/stdlib/libs")

    val stubsLocation: VirtualFile?
        get() {
            if (stubs == null) {
                stubs = getStubs()
            }
            return stubs
        }

    private fun findFileByUrl(url: String): VirtualFile? {
        if (url.isEmpty()) return null
        return VirtualFileManager.getInstance().findFileByUrl(url)
    }

    private fun findFile(path: String): VirtualFile? {
        if (path.isEmpty()) return null
        return VirtualFileManager.getInstance().findFileByUrl(VfsUtilCore.pathToUrl(path))
    }

    private fun findFileInProject(path: String): VirtualFile? {
        val projectDir = project.guessProjectDir() ?: return null
        return projectDir.findFileByRelativePath(path)
    }

    private fun getStubs(): VirtualFile? {
        val url = this::class.java.classLoader.getResource("stubs")
        if (url != null) {
            val root = VfsUtil.findFileByURL(url)
            if (root != null) {
                return root
            }
        }

        return null
    }
}
