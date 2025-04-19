package org.tonstudio.tact.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import org.tonstudio.tact.lang.psi.TactContractParameters
import org.tonstudio.tact.lang.psi.impl.TactContractParametersImpl
import org.tonstudio.tact.lang.stubs.TactContractParametersStub

class TactContractParametersStubElementType(name: String) : TactStubElementType<TactContractParametersStub, TactContractParameters>(name) {
    override fun createPsi(stub: TactContractParametersStub): TactContractParameters {
        return TactContractParametersImpl(stub, this)
    }

    override fun createStub(psi: TactContractParameters, parentStub: StubElement<*>?): TactContractParametersStub {
        return TactContractParametersStub(parentStub, this, psi.text)
    }

    override fun serialize(stub: TactContractParametersStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.getText())
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TactContractParametersStub {
        return TactContractParametersStub(parentStub, this, dataStream.readName())
    }
}
