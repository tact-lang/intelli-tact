package org.tonstudio.tact.lang.psi

import com.intellij.psi.PsiElement

interface TactReferenceExpressionBase : TactCompositeElement {
    fun getIdentifier(): PsiElement?
    fun getQualifier(): TactCompositeElement?
    fun resolve(): PsiElement?

    fun getIdentifierBounds(): Pair<Int, Int>? {
        val identifier = getIdentifier() ?: return null
        return Pair(identifier.startOffsetInParent, identifier.startOffsetInParent + identifier.textLength)
    }
}
