package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.io.StringRef
import org.tonstudio.tact.lang.psi.TactTypeReferenceExpression
import org.tonstudio.tact.lang.psi.impl.TactTypeReferenceExpressionImpl
import org.tonstudio.tact.lang.stubs.types.TactStubElementType

class TactTypeReferenceExpressionStub : StubWithText<TactTypeReferenceExpression> {
    var startOffsetInParent: Int = -1
    var textLength: Int = -1

    constructor(
        parent: StubElement<*>?,
        elementType: IStubElementType<*, *>?,
        ref: StringRef?,
        startOffsetInParent: Int,
        textLength: Int,
    ) : super(parent, elementType, ref) {
        this.startOffsetInParent = startOffsetInParent
        this.textLength = textLength
    }

    constructor(
        parent: StubElement<*>?,
        elementType: IStubElementType<*, *>?, text: String?, startOffsetInParent: Int, textLength: Int,
    ) : this(parent, elementType, StringRef.fromString(text), startOffsetInParent, textLength)

    class Type(name: String) : TactStubElementType<TactTypeReferenceExpressionStub, TactTypeReferenceExpression>(name) {
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
}
