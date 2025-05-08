package org.tonstudio.tact.boc

import com.intellij.lang.Language

object TonBocLanguage : Language("TonBoc") {
    private fun readResolve(): Any = TonBocLanguage
    override fun getDisplayName() = "TON BoC"
}
