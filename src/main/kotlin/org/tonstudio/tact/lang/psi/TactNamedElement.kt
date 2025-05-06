package org.tonstudio.tact.lang.psi

import com.intellij.navigation.NavigationItem
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner

interface TactNamedElement : TactTypeOwner, TactCompositeElement, PsiNameIdentifierOwner, NavigationItem {
    fun isBlank(): Boolean
    fun getIdentifier(): PsiElement?
    fun getQualifiedName(): String?
    fun getOwner(): TactNamedElement?
    fun kindPresentation(): String
}
