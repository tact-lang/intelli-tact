package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.io.StringRef
import org.tonstudio.tact.lang.psi.TactResult
import org.tonstudio.tact.lang.psi.impl.TactResultImpl
import org.tonstudio.tact.lang.stubs.types.TactStubElementType

class TactResultStub(parent: StubElement<*>?, elementType: IStubElementType<*, *>?, ref: StringRef?) :
    StubWithText<TactResult>(parent, elementType, ref) {

    constructor(parent: StubElement<*>?, elementType: IStubElementType<*, *>?, text: String?) :
            this(parent, elementType, StringRef.fromString(text))

    class Type(name: String) : TactStubElementType<TactResultStub, TactResult>(name) {
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
}
