package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.io.StringRef
import org.tonstudio.tact.lang.psi.TactContractParameters
import org.tonstudio.tact.lang.psi.impl.TactContractParametersImpl
import org.tonstudio.tact.lang.stubs.types.TactStubElementType

class TactContractParametersStub(parent: StubElement<*>?, elementType: IStubElementType<*, *>?, ref: StringRef?) :
    StubWithText<TactContractParameters>(parent, elementType, ref) {

    constructor(parent: StubElement<*>?, elementType: IStubElementType<*, *>?, text: String?) :
            this(parent, elementType, StringRef.fromString(text))

    class Type(name: String) : TactStubElementType<TactContractParametersStub, TactContractParameters>(name) {
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
}
