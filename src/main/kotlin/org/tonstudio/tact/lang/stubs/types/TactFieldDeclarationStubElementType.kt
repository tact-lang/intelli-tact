package org.tonstudio.tact.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import org.tonstudio.tact.lang.psi.TactFieldDeclaration
import org.tonstudio.tact.lang.psi.impl.TactFieldDeclarationImpl
import org.tonstudio.tact.lang.stubs.TactFieldDeclarationStub

class TactFieldDeclarationStubElementType(name: String) : TactStubElementType<TactFieldDeclarationStub, TactFieldDeclaration>(name) {
    override fun createPsi(stub: TactFieldDeclarationStub): TactFieldDeclaration {
        return TactFieldDeclarationImpl(stub, this)
    }

    override fun createStub(psi: TactFieldDeclaration, parentStub: StubElement<*>?): TactFieldDeclarationStub {
        return TactFieldDeclarationStub(parentStub, this)
    }

    override fun serialize(stub: TactFieldDeclarationStub, dataStream: StubOutputStream) {}

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TactFieldDeclarationStub {
        return TactFieldDeclarationStub(parentStub, this)
    }
}
