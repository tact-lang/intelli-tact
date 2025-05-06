package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.io.StringRef
import org.tonstudio.tact.lang.psi.TactParameters
import org.tonstudio.tact.lang.psi.impl.TactParametersImpl
import org.tonstudio.tact.lang.stubs.types.TactStubElementType

class TactParametersStub(parent: StubElement<*>?, elementType: IStubElementType<*, *>?, ref: StringRef?) :
    StubWithText<TactParameters>(parent, elementType, ref) {

    constructor(parent: StubElement<*>?, elementType: IStubElementType<*, *>?, text: String?) :
            this(parent, elementType, StringRef.fromString(text))

    class Type(name: String) : TactStubElementType<TactParametersStub, TactParameters>(name) {
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
}
