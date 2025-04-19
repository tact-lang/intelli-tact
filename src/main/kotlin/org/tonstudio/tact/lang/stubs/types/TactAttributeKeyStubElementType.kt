package org.tonstudio.tact.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import org.tonstudio.tact.lang.psi.TactAttributeKey
import org.tonstudio.tact.lang.psi.impl.TactAttributeKeyImpl
import org.tonstudio.tact.lang.stubs.TactAttributeKeyStub

class TactAttributeKeyStubElementType(name: String) : TactStubElementType<TactAttributeKeyStub, TactAttributeKey>(name) {
    override fun createPsi(stub: TactAttributeKeyStub): TactAttributeKey {
        return TactAttributeKeyImpl(stub, this)
    }

    override fun createStub(psi: TactAttributeKey, parentStub: StubElement<*>?): TactAttributeKeyStub {
        return TactAttributeKeyStub(parentStub, this, psi.text)
    }

    override fun serialize(stub: TactAttributeKeyStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.getText())
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TactAttributeKeyStub {
        return TactAttributeKeyStub(parentStub, this, dataStream.readName())
    }
}
