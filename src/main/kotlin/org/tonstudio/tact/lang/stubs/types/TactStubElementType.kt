package org.tonstudio.tact.lang.stubs.types

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubBase
import org.tonstudio.tact.lang.TactLanguage
import org.tonstudio.tact.lang.psi.TactCompositeElement

abstract class TactStubElementType<S : StubBase<T>, T : TactCompositeElement>(debugName: String) :
    IStubElementType<S, T>(debugName, TactLanguage) {

    override fun getExternalId() = "tact." + super.toString()

    override fun indexStub(stub: S, sink: IndexSink) {}
}
