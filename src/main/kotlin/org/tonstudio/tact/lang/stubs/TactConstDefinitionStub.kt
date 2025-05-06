package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.io.StringRef
import org.tonstudio.tact.lang.psi.TactConstDefinition
import org.tonstudio.tact.lang.psi.impl.TactConstDefinitionImpl
import org.tonstudio.tact.lang.stubs.types.TactNamedStubElementType

class TactConstDefinitionStub : TactNamedStub<TactConstDefinition> {
    // used in documentation to get type and expression without AST loading
    var value: String = ""
    var type: String = ""

    constructor(
        parent: StubElement<*>?,
        elementType: IStubElementType<*, *>,
        name: StringRef?,
        isExported: Boolean,
        type: String,
        value: String,
    ) : super(parent, elementType, name, isExported) {
        this.type = type
        this.value = value
    }

    constructor(
        parent: StubElement<*>?,
        elementType: IStubElementType<*, *>,
        name: String?,
        isExported: Boolean,
        type: String,
        value: String,
    ) : super(parent, elementType, name, isExported) {
        this.type = type
        this.value = value
    }

    class Type(name: String) : TactNamedStubElementType<TactConstDefinitionStub, TactConstDefinition>(name) {
        override fun createPsi(stub: TactConstDefinitionStub): TactConstDefinition {
            return TactConstDefinitionImpl(stub, this)
        }

        override fun createStub(psi: TactConstDefinition, parentStub: StubElement<*>?): TactConstDefinitionStub {
            val value = psi.expression?.text ?: ""
            return TactConstDefinitionStub(parentStub, this, psi.name, true, "", value)
        }

        override fun serialize(stub: TactConstDefinitionStub, dataStream: StubOutputStream) {
            dataStream.writeName(stub.name)
            dataStream.writeBoolean(stub.isExported)
            dataStream.writeName(stub.type)
            dataStream.writeName(stub.value)
        }

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TactConstDefinitionStub {
            return TactConstDefinitionStub(
                parentStub,
                this,
                dataStream.readName(),
                dataStream.readBoolean(),
                dataStream.readNameString() ?: "",
                dataStream.readNameString() ?: ""
            )
        }
    }
}
