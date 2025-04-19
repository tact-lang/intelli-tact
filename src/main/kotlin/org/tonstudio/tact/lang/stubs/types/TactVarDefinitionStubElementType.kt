package org.tonstudio.tact.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import org.tonstudio.tact.lang.psi.TactVarDefinition
import org.tonstudio.tact.lang.psi.impl.TactVarDefinitionImpl
import org.tonstudio.tact.lang.stubs.TactVarDefinitionStub

class TactVarDefinitionStubElementType(name: String) : TactNamedStubElementType<TactVarDefinitionStub, TactVarDefinition>(name) {
    override fun createPsi(stub: TactVarDefinitionStub): TactVarDefinition {
        return TactVarDefinitionImpl(stub, this)
    }

    override fun createStub(psi: TactVarDefinition, parentStub: StubElement<*>?): TactVarDefinitionStub {
        return TactVarDefinitionStub(parentStub, this, psi.name, true)
    }

    override fun serialize(stub: TactVarDefinitionStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
        dataStream.writeBoolean(stub.isPublic)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TactVarDefinitionStub {
        return TactVarDefinitionStub(parentStub, this, dataStream.readName(), dataStream.readBoolean())
    }
}
