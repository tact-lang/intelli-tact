package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.io.StringRef
import org.tonstudio.tact.lang.psi.TactConstDeclaration
import org.tonstudio.tact.lang.psi.impl.TactConstDeclarationImpl
import org.tonstudio.tact.lang.stubs.types.TactNamedStubElementType

class TactConstDeclarationStub : TactNamedStub<TactConstDeclaration> {
    // used in documentation to get type and expression without AST loading
    var value: String = ""
    var type: String = ""

    constructor(
        parent: StubElement<*>?,
        elementType: IStubElementType<*, *>,
        name: StringRef?,
        isExported: Boolean,
        type: String,
        value: String,
    ) : super(parent, elementType, name, isExported) {
        this.type = type
        this.value = value
    }

    constructor(
        parent: StubElement<*>?,
        elementType: IStubElementType<*, *>,
        name: String?,
        isExported: Boolean,
        type: String,
        value: String,
    ) : super(parent, elementType, name, isExported) {
        this.type = type
        this.value = value
    }

    class Type(name: String) : TactNamedStubElementType<TactConstDeclarationStub, TactConstDeclaration>(name) {
        override fun createPsi(stub: TactConstDeclarationStub): TactConstDeclaration {
            return TactConstDeclarationImpl(stub, this)
        }

        override fun createStub(psi: TactConstDeclaration, parentStub: StubElement<*>?): TactConstDeclarationStub {
            val value = psi.expression?.text ?: ""
            return TactConstDeclarationStub(parentStub, this, psi.name, true, "", value)
        }

        override fun serialize(stub: TactConstDeclarationStub, dataStream: StubOutputStream) {
            dataStream.writeName(stub.name)
            dataStream.writeBoolean(stub.isExported)
            dataStream.writeName(stub.type)
            dataStream.writeName(stub.value)
        }

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TactConstDeclarationStub {
            return TactConstDeclarationStub(
                parentStub,
                this,
                dataStream.readName(),
                dataStream.readBoolean(),
                dataStream.readNameString() ?: "",
                dataStream.readNameString() ?: ""
            )
        }
    }
}
