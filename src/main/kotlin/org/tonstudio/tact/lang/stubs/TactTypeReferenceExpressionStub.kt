package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.util.io.StringRef
import org.tonstudio.tact.lang.psi.TactTypeReferenceExpression

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
}
