package org.tonstudio.tact.lang.doc.psi

import com.intellij.psi.impl.source.tree.CompositeElement
import com.intellij.psi.tree.ICompositeElementType
import com.intellij.psi.tree.IElementType

class TactDocCompositeTokenType(
    debugName: String,
    private val astFactory: (IElementType) -> CompositeElement
) : TactDocTokenType(debugName), ICompositeElementType {
    override fun createCompositeNode(): CompositeElement = astFactory(this)
}
