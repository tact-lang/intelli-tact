package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import org.tonstudio.tact.lang.psi.TactFieldDeclaration

class TactFieldDeclarationStub(parent: StubElement<*>?, elementType: IStubElementType<*, *>) :
    StubBase<TactFieldDeclaration>(parent, elementType)
