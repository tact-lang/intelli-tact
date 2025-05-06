package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.*
import com.intellij.util.io.StringRef
import org.tonstudio.tact.lang.psi.TactNativeFunctionDeclaration
import org.tonstudio.tact.lang.stubs.types.TactNamedStubElementType
import org.tonstudio.tact.lang.psi.impl.TactNativeFunctionDeclarationImpl
import org.tonstudio.tact.lang.stubs.TactFunctionDeclarationStub.Type.Companion.calcTypeText
import org.tonstudio.tact.lang.stubs.index.TactMethodIndex

class TactNativeFunctionDeclarationStub : TactNamedStub<TactNativeFunctionDeclaration> {
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
        type: String?,
    ) : super(parent, elementType, name, isExported) {
        this.receiverType = type
    }

    class Type(name: String) : TactNamedStubElementType<TactNativeFunctionDeclarationStub, TactNativeFunctionDeclaration>(name) {

        override fun createPsi(stub: TactNativeFunctionDeclarationStub) =
            TactNativeFunctionDeclarationImpl(stub, this)

        override fun createStub(psi: TactNativeFunctionDeclaration, parentStub: StubElement<*>?) =
            TactNativeFunctionDeclarationStub(parentStub, this, psi.name ?: "", true, calcTypeText(psi))

        override fun serialize(stub: TactNativeFunctionDeclarationStub, dataStream: StubOutputStream) {
            dataStream.writeName(stub.name)
            dataStream.writeBoolean(stub.isExported)
            dataStream.writeName(stub.receiverType)
        }

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?) =
            TactNativeFunctionDeclarationStub(parentStub, this, dataStream.readName(), dataStream.readBoolean(), dataStream.readNameString())

        override fun indexStub(stub: TactNativeFunctionDeclarationStub, sink: IndexSink) {
            super.indexStub(stub, sink)

            val receiverType = stub.receiverType
            if (receiverType.isNullOrEmpty()) return
            sink.occurrence(TactMethodIndex.KEY, receiverType)
        }
    }
}
