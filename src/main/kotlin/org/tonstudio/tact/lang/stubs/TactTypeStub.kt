package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.io.StringRef
import org.tonstudio.tact.lang.psi.TactCompositeElement
import org.tonstudio.tact.lang.stubs.types.TactStubElementType

class TactTypeStub<PsiT : TactCompositeElement>(parent: StubElement<*>?, elementType: IStubElementType<*, *>?, ref: StringRef?) :
    StubWithText<PsiT>(parent, elementType, ref) {

    class Type<PsiT : TactCompositeElement>(
        name: String,
        private val psiCtor: (TactTypeStub<*>, IStubElementType<*, *>) -> PsiT,
    ) : TactStubElementType<TactTypeStub<PsiT>, PsiT>(name) {

        override fun createPsi(stub: TactTypeStub<PsiT>): PsiT = psiCtor(stub, this)
        override fun createStub(psi: PsiT, parentStub: StubElement<*>?): TactTypeStub<PsiT> =
            TactTypeStub(parentStub, this, StringRef.fromString(psi.text))

        override fun serialize(stub: TactTypeStub<PsiT>, dataStream: StubOutputStream) {
            dataStream.writeName(stub.getText())
        }

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TactTypeStub<PsiT> {
            return TactTypeStub(parentStub, this, dataStream.readName())
        }
    }
}
