package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.io.StringRef
import org.tonstudio.tact.lang.psi.*
import org.tonstudio.tact.lang.psi.impl.TactPlainAttributeImpl
import org.tonstudio.tact.lang.stubs.types.TactStubElementType

class TactPlainAttributeStub(parent: StubElement<*>?, elementType: IStubElementType<*, *>?, ref: StringRef?) :
    StubWithText<TactPlainAttribute>(parent, elementType, ref) {

    constructor(parent: StubElement<*>?, elementType: IStubElementType<*, *>?, text: String?) :
            this(parent, elementType, StringRef.fromString(text))

    class Type(name: String) : TactStubElementType<TactPlainAttributeStub, TactPlainAttribute>(name) {
        override fun createPsi(stub: TactPlainAttributeStub): TactPlainAttribute {
            return TactPlainAttributeImpl(stub, this)
        }

        override fun createStub(psi: TactPlainAttribute, parentStub: StubElement<*>?): TactPlainAttributeStub {
            return TactPlainAttributeStub(parentStub, this, psi.text)
        }

        override fun serialize(stub: TactPlainAttributeStub, dataStream: StubOutputStream) {
            dataStream.writeName(stub.getText())
        }

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TactPlainAttributeStub {
            return TactPlainAttributeStub(parentStub, this, dataStream.readName())
        }
    }
}
