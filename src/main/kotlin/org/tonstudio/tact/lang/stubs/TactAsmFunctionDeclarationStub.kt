package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.*
import com.intellij.util.io.StringRef
import org.tonstudio.tact.lang.psi.TactAsmFunctionDeclaration
import org.tonstudio.tact.lang.stubs.types.TactNamedStubElementType
import org.tonstudio.tact.lang.psi.impl.TactAsmFunctionDeclarationImpl
import org.tonstudio.tact.lang.stubs.TactFunctionDeclarationStub.Type.Companion.calcTypeText
import org.tonstudio.tact.lang.stubs.index.TactMethodIndex

class TactAsmFunctionDeclarationStub : TactNamedStub<TactAsmFunctionDeclaration> {
    var receiverType: String? = null

    constructor(
        parent: StubElement<*>?,
        elementType: IStubElementType<*, *>,
        name: StringRef?,
        isExported: Boolean,
        receiverType: String?,
    ) : super(parent, elementType, name, isExported) {
        this.receiverType = receiverType
    }

    constructor(
        parent: StubElement<*>?,
        elementType: IStubElementType<*, *>,
        name: String,
        isExported: Boolean,
        receiverType: String?,
    ) : super(parent, elementType, name, isExported) {
        this.receiverType = receiverType
    }

    class Type(name: String) : TactNamedStubElementType<TactAsmFunctionDeclarationStub, TactAsmFunctionDeclaration>(name) {

        override fun createPsi(stub: TactAsmFunctionDeclarationStub) =
            TactAsmFunctionDeclarationImpl(stub, this)

        override fun createStub(psi: TactAsmFunctionDeclaration, parentStub: StubElement<*>?) =
            TactAsmFunctionDeclarationStub(parentStub, this, psi.name ?: "", true, calcTypeText(psi))

        override fun serialize(stub: TactAsmFunctionDeclarationStub, dataStream: StubOutputStream) {
            dataStream.writeName(stub.name)
            dataStream.writeBoolean(stub.isExported)
            dataStream.writeName(stub.receiverType)
        }

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?) =
            TactAsmFunctionDeclarationStub(parentStub, this, dataStream.readName(), dataStream.readBoolean(), dataStream.readNameString())

        override fun indexStub(stub: TactAsmFunctionDeclarationStub, sink: IndexSink) {
            super.indexStub(stub, sink)

            val receiverType = stub.receiverType
            if (receiverType.isNullOrEmpty()) return
            sink.occurrence(TactMethodIndex.KEY, receiverType)
        }
    }
}
