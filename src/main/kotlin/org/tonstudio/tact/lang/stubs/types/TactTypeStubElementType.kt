package org.tonstudio.tact.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import org.tonstudio.tact.lang.psi.TactType
import org.tonstudio.tact.lang.stubs.TactTypeStub

abstract class TactTypeStubElementType(name: String) : TactStubElementType<TactTypeStub, TactType>(name) {
    override fun createStub(psi: TactType, parentStub: StubElement<*>?): TactTypeStub {
        return TactTypeStub(parentStub, this, psi.text)
    }

    override fun serialize(stub: TactTypeStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.getText())
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TactTypeStub {
        return TactTypeStub(parentStub, this, dataStream.readName())
    }
}
