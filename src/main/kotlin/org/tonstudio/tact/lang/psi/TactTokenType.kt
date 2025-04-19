package org.tonstudio.tact.lang.psi

import com.intellij.psi.tree.IElementType
import org.tonstudio.tact.lang.TactLanguage

open class TactTokenType(debugName: String) : IElementType(debugName, TactLanguage) {
    override fun toString() = "TactTokenType." + super.toString()
}
