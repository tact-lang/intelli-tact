package org.tonstudio.tact.lang.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.tonstudio.tact.lang.psi.*

open class TactVarProcessor(
    requestedName: PsiElement,
    origin: PsiElement,
    completion: Boolean,
) : TactScopeProcessorBase(requestedName, origin, completion) {

    constructor(origin: PsiElement, completion: Boolean) : this(origin, origin, completion)

    override fun crossOff(e: PsiElement): Boolean {
        if (origin is TactTypeReferenceExpression && e is TactParamDefinition) {
            return true
        }

        if (e is TactVarDefinition) {
            if (e.parent is TactForEachStatement || e.parent is TactCatchClause || e.parent is TactDestructItem) {
                return false
            }

            val decl = e.parent as TactVarDeclaration
            // forbid to resolve to `<var>` inside `let <var> = <caret>`
            return PsiTreeUtil.isAncestor(decl.expression, origin, false)
        }

        return e !is TactParamDefinition &&
                e !is TactConstDeclaration &&
                e !is TactStructDeclaration
    }
}
