package org.tonstudio.tact.lang.psi

import com.intellij.psi.PsiElement

interface TactReferenceExpressionBase : TactCompositeElement {
    fun getIdentifier(): PsiElement
    fun getQualifier(): TactCompositeElement?
    fun resolve(): PsiElement?
}
