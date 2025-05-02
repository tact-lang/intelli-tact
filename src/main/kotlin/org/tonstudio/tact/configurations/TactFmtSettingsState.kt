package org.tonstudio.tact.configurations

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "TactFmtSettingsState",
    storages = [Storage(StoragePathMacros.WORKSPACE_FILE)]
)
@Service(Service.Level.PROJECT)
class TactFmtSettingsState : PersistentStateComponent<TactFmtSettingsState?> {
    companion object {
        val Project.tactfmtSettings
            get() = service<TactFmtSettingsState>()
    }

    var additionalArguments: String = ""
    var envs: Map<String, String> = emptyMap()
    var runTactfmtOnSave: Boolean = false

    override fun getState() = this

    override fun loadState(state: TactFmtSettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }
}
