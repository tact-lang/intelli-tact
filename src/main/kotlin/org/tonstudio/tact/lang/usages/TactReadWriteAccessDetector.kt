package org.tonstudio.tact.lang.usages

import com.intellij.codeInsight.highlighting.ReadWriteAccessDetector
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.util.parentOfType
import org.tonstudio.tact.lang.psi.*

class TactReadWriteAccessDetector : ReadWriteAccessDetector() {
    override fun isReadWriteAccessible(element: PsiElement): Boolean {
        return element is TactVarDefinition ||
                element is TactConstDeclaration ||
                element is TactParamDefinition ||
                element is TactFieldDefinition
    }

    override fun isDeclarationWriteAccess(element: PsiElement): Boolean {
        return element is TactVarDefinition || element is TactConstDeclaration
    }

    override fun getReferenceAccess(referencedElement: PsiElement, reference: PsiReference): Access {
        return getExpressionAccess(reference.element)
    }

    override fun getExpressionAccess(expression: PsiElement): Access {
        if (expression is TactFieldName) {
            return if (expression.parent is TactInstanceArgumentFull) Access.Write else Access.Read
        }
        val referenceExpression = expression as? TactReferenceExpression ?: expression.parentOfType()
        return referenceExpression?.readWriteAccess ?: Access.Read
    }
}
