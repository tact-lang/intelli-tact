package org.tonstudio.tact.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.ArrayFactory
import org.tonstudio.tact.lang.psi.TactStructDeclaration
import org.tonstudio.tact.lang.psi.impl.TactStructDeclarationImpl
import org.tonstudio.tact.lang.stubs.TactStructDeclarationStub
import org.tonstudio.tact.lang.stubs.index.TactStructIndex

class TactStructDeclarationStubElementType(name: String) :
    TactNamedStubElementType<TactStructDeclarationStub, TactStructDeclaration>(name) {

    override fun createPsi(stub: TactStructDeclarationStub): TactStructDeclaration {
        return TactStructDeclarationImpl(stub, this)
    }

    override fun createStub(psi: TactStructDeclaration, parentStub: StubElement<*>?): TactStructDeclarationStub {
        return TactStructDeclarationStub(parentStub, this, psi.name, true)
    }

    override fun serialize(stub: TactStructDeclarationStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
        dataStream.writeBoolean(stub.isPublic)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TactStructDeclarationStub {
        return TactStructDeclarationStub(
            parentStub,
            this,
            dataStream.readName(),
            dataStream.readBoolean(),
        )
    }

    override fun getExtraIndexKeys() = EXTRA_KEYS

    companion object {
        private val EXTRA_KEYS = listOf(TactStructIndex.KEY)

        private val EMPTY_ARRAY: Array<TactStructDeclaration?> = arrayOfNulls(0)
        val ARRAY_FACTORY = ArrayFactory<TactStructDeclaration> { count: Int ->
            if (count == 0) EMPTY_ARRAY else arrayOfNulls<TactStructDeclaration>(count)
        }
    }
}
