package org.tonstudio.tact.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import org.tonstudio.tact.lang.psi.TactAttribute
import org.tonstudio.tact.lang.psi.impl.TactAttributeImpl
import org.tonstudio.tact.lang.stubs.TactAttributeStub

class TactAttributeStubElementType(name: String) : TactStubElementType<TactAttributeStub, TactAttribute>(name) {
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
