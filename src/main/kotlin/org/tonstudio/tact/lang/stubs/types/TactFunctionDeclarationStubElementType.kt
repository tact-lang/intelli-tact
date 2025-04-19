package org.tonstudio.tact.lang.stubs.types

import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.ArrayFactory
import org.tonstudio.tact.lang.psi.TactFunctionDeclaration
import org.tonstudio.tact.lang.psi.TactMapType
import org.tonstudio.tact.lang.psi.TactSignatureOwner
import org.tonstudio.tact.lang.psi.impl.TactFunctionDeclarationImpl
import org.tonstudio.tact.lang.stubs.TactFunctionDeclarationStub
import org.tonstudio.tact.lang.stubs.index.TactMethodIndex

class TactFunctionDeclarationStubElementType(name: String) :
    TactNamedStubElementType<TactFunctionDeclarationStub, TactFunctionDeclaration>(name) {

    override fun createPsi(stub: TactFunctionDeclarationStub): TactFunctionDeclaration {
        return TactFunctionDeclarationImpl(stub, this)
    }

    override fun createStub(psi: TactFunctionDeclaration, parentStub: StubElement<*>?): TactFunctionDeclarationStub {
        return TactFunctionDeclarationStub(parentStub, this, psi.name, true, calcTypeText(psi))
    }

    override fun serialize(stub: TactFunctionDeclarationStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
        dataStream.writeBoolean(stub.isPublic)
        dataStream.writeName(stub.type)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TactFunctionDeclarationStub {
        return TactFunctionDeclarationStub(
            parentStub,
            this,
            dataStream.readName(),
            dataStream.readBoolean(),
            dataStream.readNameString(),
        )
    }

    override fun indexStub(stub: TactFunctionDeclarationStub, sink: IndexSink) {
        super.indexStub(stub, sink)
        if (stub.type.isNullOrEmpty()) return

        sink.occurrence(TactMethodIndex.KEY, stub.type!!)
    }

    companion object {
        val EMPTY_ARRAY: Array<TactFunctionDeclaration?> = arrayOfNulls(0)
        val ARRAY_FACTORY = ArrayFactory<TactFunctionDeclaration> { count: Int ->
            if (count == 0) EMPTY_ARRAY else arrayOfNulls<TactFunctionDeclaration>(count)
        }

        fun calcTypeText(psi: TactSignatureOwner): String? {
            val params = psi.getSignature()?.parameters?.paramDefinitionList ?: return null
            if (params.isEmpty()) return null
            val type = params.first().type
            if (type is TactMapType) {
                return "map"
            }
            return type.text
        }
    }
}