package org.tonstudio.tact.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import org.tonstudio.tact.lang.psi.TactParamDefinition
import org.tonstudio.tact.lang.psi.impl.TactParamDefinitionImpl
import org.tonstudio.tact.lang.stubs.TactParamDefinitionStub

class TactParamDefinitionStubElementType(name: String) : TactNamedStubElementType<TactParamDefinitionStub, TactParamDefinition>(name) {
    override fun createPsi(stub: TactParamDefinitionStub): TactParamDefinition {
        return TactParamDefinitionImpl(stub, this)
    }

    override fun createStub(psi: TactParamDefinition, parentStub: StubElement<*>?): TactParamDefinitionStub {
        return TactParamDefinitionStub(parentStub, this, psi.name, true, isVariadic = false)
    }

    override fun serialize(stub: TactParamDefinitionStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
        dataStream.writeBoolean(stub.isPublic)
        dataStream.writeBoolean(stub.isVariadic)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TactParamDefinitionStub {
        return TactParamDefinitionStub(parentStub, this, dataStream.readName(), dataStream.readBoolean(), dataStream.readBoolean())
    }
}
