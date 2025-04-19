package org.tonstudio.tact.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import org.tonstudio.tact.lang.psi.TactParameters
import org.tonstudio.tact.lang.psi.impl.TactParametersImpl
import org.tonstudio.tact.lang.stubs.TactParametersStub

class TactParametersStubElementType(name: String) : TactStubElementType<TactParametersStub, TactParameters>(name) {
    override fun createPsi(stub: TactParametersStub): TactParameters {
        return TactParametersImpl(stub, this)
    }

    override fun createStub(psi: TactParameters, parentStub: StubElement<*>?): TactParametersStub {
        return TactParametersStub(parentStub, this, psi.text)
    }

    override fun serialize(stub: TactParametersStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.getText())
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TactParametersStub {
        return TactParametersStub(parentStub, this, dataStream.readName())
    }
}
