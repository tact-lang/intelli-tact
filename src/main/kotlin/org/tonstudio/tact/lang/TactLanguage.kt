package org.tonstudio.tact.lang

import com.intellij.lang.Language

object TactLanguage : Language("tact") {
    override fun isCaseSensitive() = true
    override fun getDisplayName() = "Tact"
    private fun readResolve(): Any = TactLanguage
}
