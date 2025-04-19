package org.tonstudio.tact.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import org.tonstudio.tact.lang.psi.TactAttributes
import org.tonstudio.tact.lang.psi.impl.TactAttributesImpl
import org.tonstudio.tact.lang.stubs.TactAttributesStub

class TactAttributesStubElementType(name: String) : TactStubElementType<TactAttributesStub, TactAttributes>(name) {
    override fun createPsi(stub: TactAttributesStub): TactAttributes {
        return TactAttributesImpl(stub, this)
    }

    override fun createStub(psi: TactAttributes, parentStub: StubElement<*>?): TactAttributesStub {
        return TactAttributesStub(parentStub, this, psi.text)
    }

    override fun serialize(stub: TactAttributesStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.getText())
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TactAttributesStub {
        return TactAttributesStub(parentStub, this, dataStream.readName())
    }
}
