package org.tonstudio.tact.lang.psi.impl

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubBase
import org.tonstudio.tact.lang.psi.TactCompositeElement
import org.tonstudio.tact.lang.stubs.TextHolder

abstract class TactStubbedElementImpl<T : StubBase<*>> : StubBasedPsiElementBase<T>, TactCompositeElement {
    constructor(stub: T, nodeType: IStubElementType<*, *>) : super(stub, nodeType)
    constructor(node: ASTNode) : super(node)

    override fun toString() = elementType.toString()

    override fun getText(): String? {
        val stub = stub
        if (stub is TextHolder) {
            val text = (stub as? TextHolder)?.getText()
            if (text != null) return text
        }
        return super.getText()
    }

    override fun getParent(): PsiElement = parentByStub

    override fun processDeclarations(
        processor: PsiScopeProcessor,
        state: ResolveState,
        lastParent: PsiElement?,
        place: PsiElement
    ): Boolean {
        return TactCompositeElementImpl.processDeclarationsDefault(this, processor, state, lastParent, place)
    }
}
