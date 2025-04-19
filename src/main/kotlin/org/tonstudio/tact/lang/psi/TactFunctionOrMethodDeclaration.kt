package org.tonstudio.tact.lang.psi

import com.intellij.psi.PsiElement

interface TactFunctionOrMethodDeclaration : TactAttributeOwner, TactNamedElement {
    fun getBlock(): TactBlock?
    fun getSignature(): TactSignature?
    fun getFun(): PsiElement
    override fun getName(): String?
}
