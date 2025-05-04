package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.util.io.StringRef
import org.tonstudio.tact.lang.psi.TactFunctionDeclaration

class TactFunctionDeclarationStub : TactNamedStub<TactFunctionDeclaration> {
    var type: String? = null

    constructor(
        parent: StubElement<*>?,
        elementType: IStubElementType<*, *>,
        name: StringRef?,
        isPublic: Boolean,
        type: String?,
    ) : super(parent, elementType, name, isPublic) {
        this.type = type
    }

    constructor(
        parent: StubElement<*>?,
        elementType: IStubElementType<*, *>,
        name: String,
        isPublic: Boolean,
        type: String?,
    ) : super(parent, elementType, name, isPublic) {
        this.type = type
    }
}
