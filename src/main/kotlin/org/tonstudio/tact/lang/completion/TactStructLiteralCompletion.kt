package org.tonstudio.tact.lang.completion

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.tonstudio.tact.lang.psi.*
import org.tonstudio.tact.lang.psi.impl.TactPsiImplUtil

internal object TactStructLiteralCompletion {
    fun expectKeyInInstanceExpression(structFieldReference: TactReferenceExpression?, refElement: PsiElement): Boolean {
        if (structFieldReference == null) {
            return false
        }

        if (TactPsiImplUtil.prevDot(refElement)) {
            return false
        }

        var argument = structFieldReference.parent
        while (argument is TactUnaryExpr) {
            argument = argument.parent
        }
        if (argument is TactInstanceArgumentFull) {
            val value = argument.expression ?: return false
            if (PsiTreeUtil.isAncestor(value, refElement, false)) {
                // foo: <caret>
                return false
            }
            // <caret>
            return true
        }
        return argument is TactInstanceArgumentShort
    }

    fun alreadyAssignedFields(elements: TactInstanceArguments?): Set<String> {
        val list = elements?.instanceArgumentList ?: return emptySet()
        return list.mapNotNull {
            if (it.instanceArgumentFull != null) {
                it.instanceArgumentFull!!.fieldName.getIdentifier().text
            } else if (it.instanceArgumentShort != null) {
                it.instanceArgumentShort!!.referenceExpression.text
            } else {
                null
            }
        }.toSet()
    }
}
