package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.*
import com.intellij.util.ArrayFactory
import com.intellij.util.io.StringRef
import org.tonstudio.tact.lang.psi.TactAsmFunctionDeclaration
import org.tonstudio.tact.lang.stubs.types.TactNamedStubElementType
import org.tonstudio.tact.lang.psi.impl.TactAsmFunctionDeclarationImpl
import org.tonstudio.tact.lang.stubs.index.TactMethodIndex
import org.tonstudio.tact.lang.stubs.types.TactFunctionDeclarationStubElementType.Companion.calcTypeText

class TactAsmFunctionDeclarationStub : TactNamedStub<TactAsmFunctionDeclaration> {
    var type: String? = null

    constructor(
        parent: StubElement<*>?,
        elementType: IStubElementType<*, *>,
        name: StringRef?,
        isPublic: Boolean,
        type: String?,
    ) : super(parent, elementType, name, isPublic) {
        this.type = type
    }

    constructor(
        parent: StubElement<*>?,
        elementType: IStubElementType<*, *>,
        name: String,
        isPublic: Boolean,
        type: String?,
    ) : super(parent, elementType, name, isPublic) {
        this.type = type
    }

    class Type(name: String) : TactNamedStubElementType<TactAsmFunctionDeclarationStub, TactAsmFunctionDeclaration>(name) {

        override fun createPsi(stub: TactAsmFunctionDeclarationStub) =
            TactAsmFunctionDeclarationImpl(stub, this)

        override fun createStub(psi: TactAsmFunctionDeclaration, parentStub: StubElement<*>?) =
            TactAsmFunctionDeclarationStub(parentStub, this, psi.name ?: "", true, calcTypeText(psi))

        override fun serialize(stub: TactAsmFunctionDeclarationStub, dataStream: StubOutputStream) {
            dataStream.writeName(stub.name)
            dataStream.writeBoolean(stub.isPublic)
            dataStream.writeName(stub.type)
        }

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?) =
            TactAsmFunctionDeclarationStub(parentStub, this, dataStream.readName(), dataStream.readBoolean(), dataStream.readNameString())

        override fun indexStub(stub: TactAsmFunctionDeclarationStub, sink: IndexSink) {
            super.indexStub(stub, sink)
            if (stub.type.isNullOrEmpty()) return

            sink.occurrence(TactMethodIndex.KEY, stub.type!!)
        }

        companion object {
            private val EMPTY_ARRAY: Array<TactAsmFunctionDeclaration?> = arrayOfNulls(0)
            val ARRAY_FACTORY = ArrayFactory<TactAsmFunctionDeclaration> { count: Int ->
                if (count == 0) EMPTY_ARRAY else arrayOfNulls<TactAsmFunctionDeclaration>(count)
            }
        }
    }
}
