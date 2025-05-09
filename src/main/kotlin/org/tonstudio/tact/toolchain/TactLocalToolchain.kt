package org.tonstudio.tact.toolchain

import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.resolveFromRootOrRelative
import org.tonstudio.tact.configurations.TactConfigurationUtil

class TactLocalToolchain(
    private val version: String,
    private val rootDir: VirtualFile,
) : TactToolchain {
    private val homePath = rootDir.path
    private val executable = rootDir.resolveFromRootOrRelative(TactConfigurationUtil.STANDARD_TACT_COMPILER)
    private val libDir = rootDir.resolveFromRootOrRelative(TactConfigurationUtil.STANDARD_LIB_PATH)

    override fun name(): String = version

    override fun version(): String = version

    override fun compiler(): VirtualFile? = executable

    override fun stdlibDir(): VirtualFile? {
        if (rootDir.path.contains("node_modules")) {
            return libDir
        }

        // most likely compiler repo
        return rootDir.resolveFromRootOrRelative(TactConfigurationUtil.COMPILER_REPO_STANDARD_LIB_PATH) ?: libDir
    }

    override fun rootDir(): VirtualFile = rootDir

    override fun homePath(): String = homePath

    override fun isValid(): Boolean {
        val dir = rootDir()
        return dir.isValid && dir.isInLocalFileSystem && dir.isDirectory
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TactLocalToolchain

        return FileUtil.comparePaths(other.homePath(), homePath()) == 0
    }

    override fun hashCode(): Int = homePath.hashCode()
}
