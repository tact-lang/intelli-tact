package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.io.StringRef
import org.tonstudio.tact.lang.psi.TactTraitDeclaration
import org.tonstudio.tact.lang.stubs.types.TactNamedStubElementType
import org.tonstudio.tact.lang.psi.impl.TactTraitDeclarationImpl
import org.tonstudio.tact.lang.stubs.index.TactClassLikeIndex
import org.tonstudio.tact.lang.stubs.index.TactContractsTraitsIndex

class TactTraitDeclarationStub : TactNamedStub<TactTraitDeclaration> {
    constructor(
        parent: StubElement<*>?,
        elementType: IStubElementType<*, *>,
        name: StringRef?,
        isExported: Boolean,
    ) : super(parent, elementType, name, isExported)

    constructor(
        parent: StubElement<*>?,
        elementType: IStubElementType<*, *>,
        name: String?,
        isExported: Boolean,
    ) : super(parent, elementType, name, isExported)

    class Type(name: String) : TactNamedStubElementType<TactTraitDeclarationStub, TactTraitDeclaration>(name) {

        override fun createPsi(stub: TactTraitDeclarationStub) =
            TactTraitDeclarationImpl(stub, this)

        override fun createStub(psi: TactTraitDeclaration, parentStub: StubElement<*>?) =
            TactTraitDeclarationStub(parentStub, this, psi.name, true)

        override fun serialize(stub: TactTraitDeclarationStub, dataStream: StubOutputStream) {
            dataStream.writeName(stub.name)
            dataStream.writeBoolean(stub.isExported)
        }

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?) =
            TactTraitDeclarationStub(parentStub, this, dataStream.readName(), dataStream.readBoolean())

        override fun getExtraIndexKeys() = listOf(TactContractsTraitsIndex.KEY, TactClassLikeIndex.KEY)
    }
}
