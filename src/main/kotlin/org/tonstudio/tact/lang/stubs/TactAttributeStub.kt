package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.io.StringRef
import org.tonstudio.tact.lang.psi.*
import org.tonstudio.tact.lang.psi.impl.TactAttributeImpl
import org.tonstudio.tact.lang.stubs.types.TactStubElementType

class TactAttributeStub(parent: StubElement<*>?, elementType: IStubElementType<*, *>?, ref: StringRef?) :
    StubWithText<TactAttribute>(parent, elementType, ref) {

    constructor(parent: StubElement<*>?, elementType: IStubElementType<*, *>?, text: String?) :
            this(parent, elementType, StringRef.fromString(text))

    class Type(name: String) : TactStubElementType<TactAttributeStub, TactAttribute>(name) {
        override fun createPsi(stub: TactAttributeStub): TactAttribute {
            return TactAttributeImpl(stub, this)
        }

        override fun createStub(psi: TactAttribute, parentStub: StubElement<*>?): TactAttributeStub {
            return TactAttributeStub(parentStub, this, psi.text)
        }

        override fun serialize(stub: TactAttributeStub, dataStream: StubOutputStream) {
            dataStream.writeName(stub.getText())
        }

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TactAttributeStub {
            return TactAttributeStub(parentStub, this, dataStream.readName())
        }
    }
}
