package org.tonstudio.tact.lang.psi

import com.intellij.psi.PsiElement

open class TactRecursiveElementVisitor : TactVisitor() {
    override fun visitElement(element: PsiElement) {
        super.visitElement(element)
        var child = element.firstChild
        while (child != null) {
            child.accept(this)
            child = child.nextSibling
        }
    }
}
