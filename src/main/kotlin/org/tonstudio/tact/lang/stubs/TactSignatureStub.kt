package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.io.StringRef
import org.tonstudio.tact.lang.psi.TactSignature
import org.tonstudio.tact.lang.psi.impl.TactSignatureImpl
import org.tonstudio.tact.lang.stubs.types.TactStubElementType

class TactSignatureStub(parent: StubElement<*>?, elementType: IStubElementType<*, *>?, ref: StringRef?) :
    StubWithText<TactSignature>(parent, elementType, ref) {

    constructor(parent: StubElement<*>?, elementType: IStubElementType<*, *>?, text: String?) :
            this(parent, elementType, StringRef.fromString(text))

    class Type(name: String) : TactStubElementType<TactSignatureStub, TactSignature>(name) {
        override fun createPsi(stub: TactSignatureStub): TactSignature {
            return TactSignatureImpl(stub, this)
        }

        override fun createStub(psi: TactSignature, parentStub: StubElement<*>?): TactSignatureStub {
            return TactSignatureStub(parentStub, this, psi.text)
        }

        override fun serialize(stub: TactSignatureStub, dataStream: StubOutputStream) {
            dataStream.writeName(stub.getText())
        }

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TactSignatureStub {
            return TactSignatureStub(parentStub, this, dataStream.readName())
        }
    }
}
