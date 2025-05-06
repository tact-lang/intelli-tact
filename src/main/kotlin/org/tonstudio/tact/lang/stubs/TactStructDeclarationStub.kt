package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.ArrayFactory
import com.intellij.util.io.StringRef
import org.tonstudio.tact.lang.psi.TactStructDeclaration
import org.tonstudio.tact.lang.psi.impl.TactStructDeclarationImpl
import org.tonstudio.tact.lang.stubs.index.TactClassLikeIndex
import org.tonstudio.tact.lang.stubs.index.TactStructIndex
import org.tonstudio.tact.lang.stubs.types.TactNamedStubElementType

class TactStructDeclarationStub : TactNamedStub<TactStructDeclaration> {
    constructor(
        parent: StubElement<*>?,
        elementType: IStubElementType<*, *>,
        name: StringRef?,
        isPublic: Boolean,
    ) : super(parent, elementType, name, isPublic)

    constructor(
        parent: StubElement<*>?,
        elementType: IStubElementType<*, *>,
        name: String?,
        isPublic: Boolean,
    ) : super(parent, elementType, name, isPublic)

    class Type(name: String) :
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

        override fun getExtraIndexKeys() = listOf(TactStructIndex.KEY, TactClassLikeIndex.KEY)

        companion object {
            private val EMPTY_ARRAY: Array<TactStructDeclaration?> = arrayOfNulls(0)
            val ARRAY_FACTORY = ArrayFactory<TactStructDeclaration> { count: Int ->
                if (count == 0) EMPTY_ARRAY else arrayOfNulls<TactStructDeclaration>(count)
            }
        }
    }
}
