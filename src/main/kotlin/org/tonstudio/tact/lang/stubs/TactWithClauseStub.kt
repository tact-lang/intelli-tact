package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.io.StringRef
import org.tonstudio.tact.lang.psi.TactWithClause
import org.tonstudio.tact.lang.psi.impl.TactWithClauseImpl
import org.tonstudio.tact.lang.stubs.types.TactStubElementType

class TactWithClauseStub(parent: StubElement<*>?, elementType: IStubElementType<*, *>?, ref: StringRef?) :
    StubWithText<TactWithClause>(parent, elementType, ref) {

    constructor(parent: StubElement<*>?, elementType: IStubElementType<*, *>?, text: String?) :
            this(parent, elementType, StringRef.fromString(text))

    class Type(name: String) : TactStubElementType<TactWithClauseStub, TactWithClause>(name) {
        override fun createPsi(stub: TactWithClauseStub): TactWithClause {
            return TactWithClauseImpl(stub, this)
        }

        override fun createStub(psi: TactWithClause, parentStub: StubElement<*>?): TactWithClauseStub {
            return TactWithClauseStub(parentStub, this, psi.text)
        }

        override fun serialize(stub: TactWithClauseStub, dataStream: StubOutputStream) {
            dataStream.writeName(stub.getText())
        }

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TactWithClauseStub {
            return TactWithClauseStub(parentStub, this, dataStream.readName())
        }
    }
}
