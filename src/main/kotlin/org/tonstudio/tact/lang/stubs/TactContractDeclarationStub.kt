package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.ArrayFactory
import com.intellij.util.io.StringRef
import org.tonstudio.tact.lang.psi.TactContractDeclaration
import org.tonstudio.tact.lang.stubs.types.TactNamedStubElementType
import org.tonstudio.tact.lang.psi.impl.TactContractDeclarationImpl
import org.tonstudio.tact.lang.stubs.index.TactClassLikeIndex
import org.tonstudio.tact.lang.stubs.index.TactContractsTraitsIndex

class TactContractDeclarationStub : TactNamedStub<TactContractDeclaration> {
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

    class Type(name: String) : TactNamedStubElementType<TactContractDeclarationStub, TactContractDeclaration>(name) {

        override fun createPsi(stub: TactContractDeclarationStub) =
            TactContractDeclarationImpl(stub, this)

        override fun createStub(psi: TactContractDeclaration, parentStub: StubElement<*>?) =
            TactContractDeclarationStub(parentStub, this, psi.name, true)

        override fun serialize(stub: TactContractDeclarationStub, dataStream: StubOutputStream) {
            dataStream.writeName(stub.name)
            dataStream.writeBoolean(stub.isExported)
        }

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?) =
            TactContractDeclarationStub(parentStub, this, dataStream.readName(), dataStream.readBoolean())

        override fun getExtraIndexKeys() = listOf(TactContractsTraitsIndex.KEY, TactClassLikeIndex.KEY)

        companion object {
            private val EMPTY_ARRAY: Array<TactContractDeclaration?> = arrayOfNulls(0)
            val ARRAY_FACTORY = ArrayFactory<TactContractDeclaration> { count: Int ->
                if (count == 0) EMPTY_ARRAY else arrayOfNulls<TactContractDeclaration>(count)
            }
        }
    }
}
