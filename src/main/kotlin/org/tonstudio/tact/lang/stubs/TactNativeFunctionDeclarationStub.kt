package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.*
import com.intellij.util.ArrayFactory
import com.intellij.util.io.StringRef
import org.tonstudio.tact.lang.psi.TactNativeFunctionDeclaration
import org.tonstudio.tact.lang.stubs.types.TactNamedStubElementType
import org.tonstudio.tact.lang.psi.impl.TactNativeFunctionDeclarationImpl
import org.tonstudio.tact.lang.stubs.TactFunctionDeclarationStub.Type.Companion.calcTypeText
import org.tonstudio.tact.lang.stubs.index.TactMethodIndex

class TactNativeFunctionDeclarationStub : TactNamedStub<TactNativeFunctionDeclaration> {
    var type: String? = null

    constructor(
        parent: StubElement<*>?,
        elementType: IStubElementType<*, *>,
        name: StringRef?,
        isExported: Boolean,
        type: String?,
    ) : super(parent, elementType, name, isExported) {
        this.type = type
    }

    constructor(
        parent: StubElement<*>?,
        elementType: IStubElementType<*, *>,
        name: String,
        isExported: Boolean,
        type: String?,
    ) : super(parent, elementType, name, isExported) {
        this.type = type
    }

    class Type(name: String) : TactNamedStubElementType<TactNativeFunctionDeclarationStub, TactNativeFunctionDeclaration>(name) {

        override fun createPsi(stub: TactNativeFunctionDeclarationStub) =
            TactNativeFunctionDeclarationImpl(stub, this)

        override fun createStub(psi: TactNativeFunctionDeclaration, parentStub: StubElement<*>?) =
            TactNativeFunctionDeclarationStub(parentStub, this, psi.name ?: "", true, calcTypeText(psi))

        override fun serialize(stub: TactNativeFunctionDeclarationStub, dataStream: StubOutputStream) {
            dataStream.writeName(stub.name)
            dataStream.writeBoolean(stub.isExported)
            dataStream.writeName(stub.type)
        }

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?) =
            TactNativeFunctionDeclarationStub(parentStub, this, dataStream.readName(), dataStream.readBoolean(), dataStream.readNameString())

        override fun indexStub(stub: TactNativeFunctionDeclarationStub, sink: IndexSink) {
            super.indexStub(stub, sink)
            if (stub.type.isNullOrEmpty()) return

            sink.occurrence(TactMethodIndex.KEY, stub.type!!)
        }

        companion object {
            private val EMPTY_ARRAY: Array<TactNativeFunctionDeclaration?> = arrayOfNulls(0)
            val ARRAY_FACTORY = ArrayFactory<TactNativeFunctionDeclaration> { count: Int ->
                if (count == 0) EMPTY_ARRAY else arrayOfNulls<TactNativeFunctionDeclaration>(count)
            }
        }
    }
}
