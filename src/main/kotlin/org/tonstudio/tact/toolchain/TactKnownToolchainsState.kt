package org.tonstudio.tact.toolchain

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "TactToolchains",
    storages = [Storage("TactToolchains.xml")]
)
class TactKnownToolchainsState : PersistentStateComponent<TactKnownToolchainsState?> {
    companion object {
        fun getInstance() = service<TactKnownToolchainsState>()
    }

    var knownToolchains: Set<String> = emptySet()

    fun isKnown(homePath: String): Boolean {
        return knownToolchains.contains(homePath)
    }

    fun add(toolchain: TactToolchain) {
        knownToolchains = knownToolchains + toolchain.homePath()
    }

    override fun getState() = this

    override fun loadState(state: TactKnownToolchainsState) {
        XmlSerializerUtil.copyBean(state, this)
    }
}
