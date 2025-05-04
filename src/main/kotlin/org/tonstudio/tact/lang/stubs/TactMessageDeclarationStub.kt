package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.io.StringRef
import org.tonstudio.tact.lang.psi.TactMessageDeclaration
import org.tonstudio.tact.lang.stubs.index.TactStructIndex
import org.tonstudio.tact.lang.stubs.types.TactNamedStubElementType
import com.intellij.util.ArrayFactory
import org.tonstudio.tact.lang.psi.impl.TactMessageDeclarationImpl
import org.tonstudio.tact.lang.stubs.index.TactClassLikeIndex

class TactMessageDeclarationStub : TactNamedStub<TactMessageDeclaration> {
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

    class Type(name: String) : TactNamedStubElementType<TactMessageDeclarationStub, TactMessageDeclaration>(name) {

        override fun createPsi(stub: TactMessageDeclarationStub) =
            TactMessageDeclarationImpl(stub, this)

        override fun createStub(psi: TactMessageDeclaration, parentStub: StubElement<*>?) =
            TactMessageDeclarationStub(parentStub, this, psi.name, true)

        override fun serialize(stub: TactMessageDeclarationStub, dataStream: StubOutputStream) {
            dataStream.writeName(stub.name)
            dataStream.writeBoolean(stub.isPublic)
        }

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?) =
            TactMessageDeclarationStub(parentStub, this, dataStream.readName(), dataStream.readBoolean())

        override fun getExtraIndexKeys() = listOf(TactStructIndex.KEY, TactClassLikeIndex.KEY)

        companion object {
            private val EMPTY_ARRAY: Array<TactMessageDeclaration?> = arrayOfNulls(0)
            val ARRAY_FACTORY = ArrayFactory<TactMessageDeclaration> { count: Int ->
                if (count == 0) EMPTY_ARRAY else arrayOfNulls<TactMessageDeclaration>(count)
            }
        }
    }
}
