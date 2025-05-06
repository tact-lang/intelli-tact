package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.io.StringRef
import org.tonstudio.tact.lang.psi.*
import org.tonstudio.tact.lang.psi.impl.TactAttributeExpressionImpl
import org.tonstudio.tact.lang.stubs.types.TactStubElementType

class TactAttributeExpressionStub(parent: StubElement<*>?, elementType: IStubElementType<*, *>?, ref: StringRef?) :
    StubWithText<TactAttributeExpression>(parent, elementType, ref) {

    constructor(parent: StubElement<*>?, elementType: IStubElementType<*, *>?, text: String?) :
            this(parent, elementType, StringRef.fromString(text))

    class Type(name: String) : TactStubElementType<TactAttributeExpressionStub, TactAttributeExpression>(name) {
        override fun createPsi(stub: TactAttributeExpressionStub): TactAttributeExpression {
            return TactAttributeExpressionImpl(stub, this)
        }

        override fun createStub(psi: TactAttributeExpression, parentStub: StubElement<*>?): TactAttributeExpressionStub {
            return TactAttributeExpressionStub(parentStub, this, psi.text)
        }

        override fun serialize(stub: TactAttributeExpressionStub, dataStream: StubOutputStream) {
            dataStream.writeName(stub.getText())
        }

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TactAttributeExpressionStub {
            return TactAttributeExpressionStub(parentStub, this, dataStream.readName())
        }
    }
}
