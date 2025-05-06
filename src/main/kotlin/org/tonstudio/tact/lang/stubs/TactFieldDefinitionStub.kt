package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.io.StringRef
import org.tonstudio.tact.lang.psi.TactFieldDefinition
import org.tonstudio.tact.lang.psi.impl.TactFieldDefinitionImpl
import org.tonstudio.tact.lang.stubs.types.TactNamedStubElementType

class TactFieldDefinitionStub : TactNamedStub<TactFieldDefinition> {
    constructor(parent: StubElement<*>?, elementType: IStubElementType<*, *>, name: StringRef?, isPublic: Boolean) :
            super(parent, elementType, name, isPublic)

    constructor(parent: StubElement<*>?, elementType: IStubElementType<*, *>, name: String?, isPublic: Boolean) :
            super(parent, elementType, name, isPublic)


    class Type(name: String) : TactNamedStubElementType<TactFieldDefinitionStub, TactFieldDefinition>(name) {
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
}
