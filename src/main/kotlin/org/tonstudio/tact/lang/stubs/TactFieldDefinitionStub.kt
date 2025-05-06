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
    constructor(parent: StubElement<*>?, elementType: IStubElementType<*, *>, name: StringRef?) :
            super(parent, elementType, name, true)

    constructor(parent: StubElement<*>?, elementType: IStubElementType<*, *>, name: String?) :
            super(parent, elementType, name, true)

    class Type(name: String) : TactNamedStubElementType<TactFieldDefinitionStub, TactFieldDefinition>(name) {
        override fun createPsi(stub: TactFieldDefinitionStub): TactFieldDefinition {
            return TactFieldDefinitionImpl(stub, this)
        }

        override fun createStub(psi: TactFieldDefinition, parentStub: StubElement<*>?): TactFieldDefinitionStub {
            return TactFieldDefinitionStub(parentStub, this, psi.name)
        }

        override fun serialize(stub: TactFieldDefinitionStub, dataStream: StubOutputStream) {
            dataStream.writeName(stub.name)
            dataStream.writeBoolean(stub.isExported)
        }

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TactFieldDefinitionStub {
            return TactFieldDefinitionStub(parentStub, this, dataStream.readName())
        }
    }
}
