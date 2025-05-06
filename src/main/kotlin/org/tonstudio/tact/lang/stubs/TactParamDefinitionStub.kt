package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.io.StringRef
import org.tonstudio.tact.lang.psi.TactParamDefinition
import org.tonstudio.tact.lang.psi.impl.TactParamDefinitionImpl
import org.tonstudio.tact.lang.stubs.types.TactNamedStubElementType

class TactParamDefinitionStub : TactNamedStub<TactParamDefinition> {
    constructor(parent: StubElement<*>?, elementType: IStubElementType<*, *>, name: StringRef?) :
            super(parent, elementType, name, true)

    constructor(parent: StubElement<*>?, elementType: IStubElementType<*, *>, name: String?) :
            super(parent, elementType, name, true)

    class Type(name: String) : TactNamedStubElementType<TactParamDefinitionStub, TactParamDefinition>(name) {
        override fun createPsi(stub: TactParamDefinitionStub): TactParamDefinition {
            return TactParamDefinitionImpl(stub, this)
        }

        override fun createStub(psi: TactParamDefinition, parentStub: StubElement<*>?): TactParamDefinitionStub {
            return TactParamDefinitionStub(parentStub, this, psi.name)
        }

        override fun serialize(stub: TactParamDefinitionStub, dataStream: StubOutputStream) {
            dataStream.writeName(stub.name)
        }

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TactParamDefinitionStub {
            return TactParamDefinitionStub(parentStub, this, dataStream.readName())
        }
    }
}
