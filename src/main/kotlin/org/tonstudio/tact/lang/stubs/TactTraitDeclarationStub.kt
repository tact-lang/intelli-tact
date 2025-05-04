package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.io.StringRef
import org.tonstudio.tact.lang.psi.TactTraitDeclaration
import org.tonstudio.tact.lang.stubs.types.TactNamedStubElementType
import com.intellij.util.ArrayFactory
import org.tonstudio.tact.lang.psi.impl.TactTraitDeclarationImpl
import org.tonstudio.tact.lang.stubs.index.TactClassLikeIndex
import org.tonstudio.tact.lang.stubs.index.TactContractsTraitsIndex

class TactTraitDeclarationStub : TactNamedStub<TactTraitDeclaration> {
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

    class Type(name: String) : TactNamedStubElementType<TactTraitDeclarationStub, TactTraitDeclaration>(name) {

        override fun createPsi(stub: TactTraitDeclarationStub) =
            TactTraitDeclarationImpl(stub, this)

        override fun createStub(psi: TactTraitDeclaration, parentStub: StubElement<*>?) =
            TactTraitDeclarationStub(parentStub, this, psi.name, true)

        override fun serialize(stub: TactTraitDeclarationStub, dataStream: StubOutputStream) {
            dataStream.writeName(stub.name)
            dataStream.writeBoolean(stub.isPublic)
        }

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?) =
            TactTraitDeclarationStub(parentStub, this, dataStream.readName(), dataStream.readBoolean())

        override fun getExtraIndexKeys() = listOf(TactContractsTraitsIndex.KEY, TactClassLikeIndex.KEY)

        companion object {
            private val EMPTY_ARRAY: Array<TactTraitDeclaration?> = arrayOfNulls(0)
            val ARRAY_FACTORY = ArrayFactory<TactTraitDeclaration> { count: Int ->
                if (count == 0) EMPTY_ARRAY else arrayOfNulls<TactTraitDeclaration>(count)
            }
        }
    }
}
