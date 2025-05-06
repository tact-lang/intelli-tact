package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.io.StringRef
import org.tonstudio.tact.lang.psi.TactImportDeclaration
import org.tonstudio.tact.lang.psi.impl.TactImportDeclarationImpl
import org.tonstudio.tact.lang.stubs.types.TactStubElementType

class TactImportDeclarationStub(parent: StubElement<*>?, elementType: IStubElementType<*, *>?, ref: StringRef?) :
    StubWithText<TactImportDeclaration>(parent, elementType, ref) {

    constructor(parent: StubElement<*>?, elementType: IStubElementType<*, *>?, text: String?) :
            this(parent, elementType, StringRef.fromString(text))

    class Type(name: String) : TactStubElementType<TactImportDeclarationStub, TactImportDeclaration>(name) {
        override fun createPsi(stub: TactImportDeclarationStub): TactImportDeclaration {
            return TactImportDeclarationImpl(stub, this)
        }

        override fun createStub(psi: TactImportDeclaration, parentStub: StubElement<*>?): TactImportDeclarationStub {
            return TactImportDeclarationStub(parentStub, this, psi.text)
        }

        override fun serialize(stub: TactImportDeclarationStub, dataStream: StubOutputStream) {
            dataStream.writeName(stub.getText())
        }

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TactImportDeclarationStub {
            return TactImportDeclarationStub(parentStub, this, dataStream.readName())
        }
    }
}
