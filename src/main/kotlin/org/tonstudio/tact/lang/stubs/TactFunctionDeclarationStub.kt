package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.ArrayFactory
import com.intellij.util.io.StringRef
import org.tonstudio.tact.lang.psi.TactFunctionDeclaration
import org.tonstudio.tact.lang.psi.TactMapType
import org.tonstudio.tact.lang.psi.TactSignatureOwner
import org.tonstudio.tact.lang.psi.impl.TactFunctionDeclarationImpl
import org.tonstudio.tact.lang.stubs.index.TactMethodIndex
import org.tonstudio.tact.lang.stubs.types.TactNamedStubElementType

class TactFunctionDeclarationStub : TactNamedStub<TactFunctionDeclaration> {
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


    class Type(name: String) :
        TactNamedStubElementType<TactFunctionDeclarationStub, TactFunctionDeclaration>(name) {

        override fun createPsi(stub: TactFunctionDeclarationStub): TactFunctionDeclaration {
            return TactFunctionDeclarationImpl(stub, this)
        }

        override fun createStub(psi: TactFunctionDeclaration, parentStub: StubElement<*>?): TactFunctionDeclarationStub {
            return TactFunctionDeclarationStub(parentStub, this, psi.name, true, calcTypeText(psi))
        }

        override fun serialize(stub: TactFunctionDeclarationStub, dataStream: StubOutputStream) {
            dataStream.writeName(stub.name)
            dataStream.writeBoolean(stub.isPublic)
            dataStream.writeName(stub.type)
        }

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TactFunctionDeclarationStub {
            return TactFunctionDeclarationStub(
                parentStub,
                this,
                dataStream.readName(),
                dataStream.readBoolean(),
                dataStream.readNameString(),
            )
        }

        override fun indexStub(stub: TactFunctionDeclarationStub, sink: IndexSink) {
            super.indexStub(stub, sink)
            if (stub.type.isNullOrEmpty()) return

            sink.occurrence(TactMethodIndex.KEY, stub.type!!)
        }

        companion object {
            val EMPTY_ARRAY: Array<TactFunctionDeclaration?> = arrayOfNulls(0)
            val ARRAY_FACTORY = ArrayFactory<TactFunctionDeclaration> { count: Int ->
                if (count == 0) EMPTY_ARRAY else arrayOfNulls<TactFunctionDeclaration>(count)
            }

            fun calcTypeText(psi: TactSignatureOwner): String? {
                val params = psi.getSignature()?.parameters?.paramDefinitionList ?: return null
                if (params.isEmpty()) return null
                val firstParam = params.first()
                if (firstParam.name != "self") return null // not a method

                val type = firstParam.type
                if (type is TactMapType) {
                    return "map"
                }
                return type.typeReferenceExpression?.text ?: type.text
            }
        }
    }

}
