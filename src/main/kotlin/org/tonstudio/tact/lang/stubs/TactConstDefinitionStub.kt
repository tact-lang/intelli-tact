package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.util.io.StringRef
import org.tonstudio.tact.lang.psi.TactConstDefinition

class TactConstDefinitionStub : TactNamedStub<TactConstDefinition> {
    var value: String = ""
    var type: String = ""

    constructor(
        parent: StubElement<*>?,
        elementType: IStubElementType<*, *>,
        name: StringRef?,
        isPublic: Boolean,
        value: String,
        type: String,
    ) : super(parent, elementType, name, isPublic) {
        this.value = value
        this.type = type
    }

    constructor(
        parent: StubElement<*>?,
        elementType: IStubElementType<*, *>,
        name: String?,
        isPublic: Boolean,
        value: String,
        type: String,
    ) : super(parent, elementType, name, isPublic) {
        this.value = value
        this.type = type
    }
}
