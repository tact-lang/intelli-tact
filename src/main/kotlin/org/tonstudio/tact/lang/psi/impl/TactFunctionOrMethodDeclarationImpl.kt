package org.tonstudio.tact.lang.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import org.tonstudio.tact.lang.psi.TactFunctionOrMethodDeclaration
import org.tonstudio.tact.lang.stubs.TactFunctionOrMethodDeclarationStub

abstract class TactFunctionOrMethodDeclarationImpl<T : TactFunctionOrMethodDeclarationStub<*>> : TactNamedElementImpl<T>,
    TactFunctionOrMethodDeclaration {

    constructor(stub: T, nodeType: IStubElementType<*, *>) : super(stub, nodeType)
    constructor(node: ASTNode) : super(node)
}
