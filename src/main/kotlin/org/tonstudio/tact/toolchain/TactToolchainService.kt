package org.tonstudio.tact.toolchain

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil
import com.intellij.util.xmlb.annotations.Attribute
import org.tonstudio.tact.configurations.TactLibrariesUtil

@State(
    name = "TactToolchain",
    storages = [Storage(StoragePathMacros.WORKSPACE_FILE)]
)
@Service(Service.Level.PROJECT)
class TactToolchainService(private val project: Project) : PersistentStateComponent<TactToolchainService.ToolchainState?> {
    private var state = ToolchainState()
    val toolchainLocation: String
        get() = state.toolchainLocation

    @Volatile
    private var toolchain: TactToolchain = TactToolchain.NULL

    fun setToolchain(project: Project, newToolchain: TactToolchain) {
        toolchain = newToolchain
        state.toolchainLocation = newToolchain.homePath()
        TactLibrariesUtil.updateLibraries(project)
    }

    fun toolchain(): TactToolchain {
        val currentLocation = state.toolchainLocation
        if (toolchain == TactToolchain.NULL && currentLocation.isNotEmpty()) {
            setToolchain(project, TactToolchain.fromPath(currentLocation))
        }
        return toolchain
    }

    fun isNotSet(): Boolean = toolchain == TactToolchain.NULL

    override fun getState() = state

    override fun loadState(state: ToolchainState) {
        XmlSerializerUtil.copyBean(state, this.state)
    }

    companion object {
        val Project.toolchainSettings
            get() = service<TactToolchainService>()
    }

    class ToolchainState {
        @Attribute("url")
        var toolchainLocation: String = ""
    }
}
