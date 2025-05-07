package org.tonstudio.tact.configurations

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile

@Service(Service.Level.PROJECT)
class TactConfiguration {
    companion object {
        fun getInstance(project: Project) = project.service<TactConfiguration>()
    }

    private var stubs: VirtualFile? = null

    val stubsLocation: VirtualFile?
        get() {
            if (stubs == null) {
                stubs = getStubs()
            }
            return stubs
        }

    private fun getStubs(): VirtualFile? {
        val url = this::class.java.classLoader.getResource("stubs") ?: return null
        val root = VfsUtil.findFileByURL(url) ?: return null
        return root
    }
}
