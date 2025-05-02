package org.tonstudio.tact.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import org.tonstudio.tact.lang.psi.TactFieldDefinition
import org.tonstudio.tact.lang.psi.impl.TactFieldDefinitionImpl
import org.tonstudio.tact.lang.stubs.TactFieldDefinitionStub

class TactFieldDefinitionStubElementType(name: String) : TactNamedStubElementType<TactFieldDefinitionStub, TactFieldDefinition>(name) {
    override fun createPsi(stub: TactFieldDefinitionStub): TactFieldDefinition {
        return TactFieldDefinitionImpl(stub, this)
    }

    override fun createStub(psi: TactFieldDefinition, parentStub: StubElement<*>?): TactFieldDefinitionStub {
        return TactFieldDefinitionStub(parentStub, this, psi.name, true)
    }

    override fun serialize(stub: TactFieldDefinitionStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
        dataStream.writeBoolean(stub.isPublic)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TactFieldDefinitionStub {
        return TactFieldDefinitionStub(parentStub, this, dataStream.readName(), dataStream.readBoolean())
    }
}
