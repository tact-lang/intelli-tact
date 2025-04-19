package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.io.StringRef
import org.tonstudio.tact.lang.psi.TactContractInitDeclaration
import org.tonstudio.tact.lang.stubs.types.TactNamedStubElementType
import org.tonstudio.tact.lang.psi.impl.TactContractInitDeclarationImpl

class TactContractInitDeclarationStub : TactNamedStub<TactContractInitDeclaration> {
    constructor(
        parent: StubElement<*>?,
        elementType: IStubElementType<*, *>,
        name: StringRef?,
    ) : super(parent, elementType, name, true)

    constructor(
        parent: StubElement<*>?,
        elementType: IStubElementType<*, *>,
        name: String?,
    ) : super(parent, elementType, name, true)

    class Type(name: String) : TactNamedStubElementType<TactContractInitDeclarationStub, TactContractInitDeclaration>(name) {

        override fun createPsi(stub: TactContractInitDeclarationStub) =
            TactContractInitDeclarationImpl(stub, this)

        override fun createStub(psi: TactContractInitDeclaration, parentStub: StubElement<*>?) =
            TactContractInitDeclarationStub(parentStub, this, psi.name)

        override fun serialize(stub: TactContractInitDeclarationStub, dataStream: StubOutputStream) {
            dataStream.writeName(stub.name)
        }

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?) =
            TactContractInitDeclarationStub(parentStub, this, dataStream.readName())
    }
}
