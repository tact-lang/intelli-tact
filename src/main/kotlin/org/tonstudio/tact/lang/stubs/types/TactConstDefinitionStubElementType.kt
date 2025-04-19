package org.tonstudio.tact.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.ArrayFactory
import org.tonstudio.tact.lang.psi.TactConstDefinition
import org.tonstudio.tact.lang.psi.impl.TactConstDefinitionImpl
import org.tonstudio.tact.lang.stubs.TactConstDefinitionStub

class TactConstDefinitionStubElementType(name: String) : TactNamedStubElementType<TactConstDefinitionStub, TactConstDefinition>(name) {
    override fun createPsi(stub: TactConstDefinitionStub): TactConstDefinition {
        return TactConstDefinitionImpl(stub, this)
    }

    override fun createStub(psi: TactConstDefinition, parentStub: StubElement<*>?): TactConstDefinitionStub {
//        val type = psi.expression?.getType(null)?.qualifiedName() ?: ""
        val value = "" // psi.expression?.text ?: ""
        return TactConstDefinitionStub(parentStub, this, psi.name, true, value, "")
    }

    override fun serialize(stub: TactConstDefinitionStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
        dataStream.writeBoolean(stub.isPublic)
        dataStream.writeName(stub.value)
        dataStream.writeName(stub.type)
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

    companion object {
        private val EMPTY_ARRAY: Array<TactConstDefinition?> = arrayOfNulls(0)
        val ARRAY_FACTORY = ArrayFactory<TactConstDefinition> { count: Int ->
            if (count == 0) EMPTY_ARRAY else arrayOfNulls<TactConstDefinition>(count)
        }
    }
}
