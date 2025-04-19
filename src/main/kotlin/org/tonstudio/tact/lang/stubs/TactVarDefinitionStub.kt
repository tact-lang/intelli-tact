package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.util.io.StringRef
import org.tonstudio.tact.lang.psi.TactVarDefinition

class TactVarDefinitionStub : TactNamedStub<TactVarDefinition> {
    constructor(parent: StubElement<*>?, elementType: IStubElementType<*, *>, name: StringRef?, isPublic: Boolean) :
            super(parent, elementType, name, isPublic)

    constructor(parent: StubElement<*>?, elementType: IStubElementType<*, *>, name: String?, isPublic: Boolean) :
            super(parent, elementType, name, isPublic)
}
