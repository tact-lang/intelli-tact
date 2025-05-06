package org.tonstudio.tact.lang.stubs.types

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubIndexKey
import org.tonstudio.tact.lang.psi.TactNamedElement
import org.tonstudio.tact.lang.psi.TactPsiTreeUtil.parentStubOfType
import org.tonstudio.tact.lang.stubs.TactFieldDefinitionStub
import org.tonstudio.tact.lang.stubs.TactFileStub
import org.tonstudio.tact.lang.stubs.TactNamedStub
import org.tonstudio.tact.lang.stubs.TactParamDefinitionStub
import org.tonstudio.tact.lang.stubs.index.TactNamesIndex

abstract class TactNamedStubElementType<S : TactNamedStub<T>, T : TactNamedElement>(debugName: String) :
    TactStubElementType<S, T>(debugName) {

    override fun shouldCreateStub(node: ASTNode): Boolean {
        if (!super.shouldCreateStub(node)) return false
        return node.psi is TactNamedElement
    }

    override fun indexStub(stub: S, sink: IndexSink) {
        val name = stub.name ?: return
        if (shouldIndex() && name.isNotEmpty()) {
            if (stub !is TactFieldDefinitionStub && stub !is TactParamDefinitionStub) {
                sink.occurrence(TactNamesIndex.KEY, name)
            }

            for (key in getExtraIndexKeys()) {
                sink.occurrence(key, name)
            }
        }
    }

    private fun shouldIndex() = true

    protected open fun getExtraIndexKeys() = emptyList<StubIndexKey<String, out TactNamedElement>>()
}
