package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.io.StringRef
import org.tonstudio.tact.lang.psi.TactVarDefinition
import org.tonstudio.tact.lang.psi.impl.TactVarDefinitionImpl
import org.tonstudio.tact.lang.stubs.types.TactNamedStubElementType

class TactVarDefinitionStub : TactNamedStub<TactVarDefinition> {
    constructor(parent: StubElement<*>?, elementType: IStubElementType<*, *>, name: StringRef?, isPublic: Boolean) :
            super(parent, elementType, name, isPublic)

    constructor(parent: StubElement<*>?, elementType: IStubElementType<*, *>, name: String?, isPublic: Boolean) :
            super(parent, elementType, name, isPublic)

    class Type(name: String) : TactNamedStubElementType<TactVarDefinitionStub, TactVarDefinition>(name) {
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
}
