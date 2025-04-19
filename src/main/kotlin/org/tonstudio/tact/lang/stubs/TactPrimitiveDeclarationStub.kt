package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.io.StringRef
import org.tonstudio.tact.lang.psi.TactPrimitiveDeclaration
import org.tonstudio.tact.lang.stubs.types.TactNamedStubElementType
import com.intellij.util.ArrayFactory
import org.tonstudio.tact.lang.psi.impl.TactPrimitiveDeclarationImpl

class TactPrimitiveDeclarationStub : TactNamedStub<TactPrimitiveDeclaration> {
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

    class Type(name: String) : TactNamedStubElementType<TactPrimitiveDeclarationStub, TactPrimitiveDeclaration>(name) {

        override fun createPsi(stub: TactPrimitiveDeclarationStub) =
            TactPrimitiveDeclarationImpl(stub, this)

        override fun createStub(psi: TactPrimitiveDeclaration, parentStub: StubElement<*>?) =
            TactPrimitiveDeclarationStub(parentStub, this, psi.name, true)

        override fun serialize(stub: TactPrimitiveDeclarationStub, dataStream: StubOutputStream) {
            dataStream.writeName(stub.name)
            dataStream.writeBoolean(stub.isPublic)
        }

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?) =
            TactPrimitiveDeclarationStub(parentStub, this, dataStream.readName(), dataStream.readBoolean())

        companion object {
            private val EMPTY_ARRAY: Array<TactPrimitiveDeclaration?> = arrayOfNulls(0)
            val ARRAY_FACTORY = ArrayFactory<TactPrimitiveDeclaration> { count: Int ->
                if (count == 0) EMPTY_ARRAY else arrayOfNulls<TactPrimitiveDeclaration>(count)
            }
        }
    }
}
