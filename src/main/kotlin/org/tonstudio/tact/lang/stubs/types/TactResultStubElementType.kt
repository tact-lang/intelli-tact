package org.tonstudio.tact.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import org.tonstudio.tact.lang.psi.TactResult
import org.tonstudio.tact.lang.psi.impl.TactResultImpl
import org.tonstudio.tact.lang.stubs.TactResultStub

class TactResultStubElementType(name: String) : TactStubElementType<TactResultStub, TactResult>(name) {
    override fun createPsi(stub: TactResultStub): TactResult {
        return TactResultImpl(stub, this)
    }

    override fun createStub(psi: TactResult, parentStub: StubElement<*>?): TactResultStub {
        return TactResultStub(parentStub, this, psi.text)
    }

    override fun serialize(stub: TactResultStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.getText())
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TactResultStub {
        return TactResultStub(parentStub, this, dataStream.readName())
    }
}
