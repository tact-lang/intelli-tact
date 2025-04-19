package org.tonstudio.tact.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import org.tonstudio.tact.lang.psi.TactTypeReferenceExpression
import org.tonstudio.tact.lang.psi.impl.TactTypeReferenceExpressionImpl
import org.tonstudio.tact.lang.stubs.TactTypeReferenceExpressionStub

class TactTypeReferenceExpressionStubElementType(name: String) : TactStubElementType<TactTypeReferenceExpressionStub, TactTypeReferenceExpression>(name) {
    override fun createPsi(stub: TactTypeReferenceExpressionStub): TactTypeReferenceExpression {
        return TactTypeReferenceExpressionImpl(stub, this)
    }

    override fun createStub(psi: TactTypeReferenceExpression, parentStub: StubElement<*>?): TactTypeReferenceExpressionStub {
        return TactTypeReferenceExpressionStub(parentStub, this, psi.text, psi.startOffsetInParent, psi.textLength)
    }

    override fun serialize(stub: TactTypeReferenceExpressionStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.getText())
        dataStream.writeInt(stub.startOffsetInParent)
        dataStream.writeInt(stub.textLength)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TactTypeReferenceExpressionStub {
        return TactTypeReferenceExpressionStub(parentStub, this, dataStream.readName(), dataStream.readInt(), dataStream.readInt())
    }
}
