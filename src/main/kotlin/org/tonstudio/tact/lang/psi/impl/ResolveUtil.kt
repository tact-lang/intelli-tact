package org.tonstudio.tact.lang.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.TokenType
import com.intellij.psi.scope.PsiScopeProcessor
import org.tonstudio.tact.lang.TactTypes
import org.tonstudio.tact.lang.psi.TactCompositeElement

object ResolveUtil {
    fun treeWalkUp(place: PsiElement?, processor: PsiScopeProcessor, until: (PsiElement) -> Boolean = { false }): Boolean {
        var lastParent: PsiElement? = null
        var run = place
        while (run != null) {
            if (until(run)) {
                return true
            }

            if (place !== run && !run.processDeclarations(processor, ResolveState.initial(), lastParent, place!!)) return false
            lastParent = run
            run = run.parent
        }
        return true
    }

    fun processChildren(
        element: PsiElement,
        processor: PsiScopeProcessor,
        substitutor: ResolveState,
        lastParent: PsiElement?,
        place: PsiElement,
    ): Boolean {
        var run = getStartNode(element, lastParent)
        while (run != null) {
            if (shouldProcess(run)) {
                val psi = run.psi
                if (psi is TactCompositeElement) {
                    if (!psi.processDeclarations(processor, substitutor, null, place)) return false
                }
            }
            run = run.treePrev
        }
        return true
    }

    private fun getStartNode(element: PsiElement, lastParent: PsiElement?): ASTNode? {
        if (lastParent == null) {
            val node = element.node
            return node?.lastChildNode
        }
        val node = lastParent.node
        return node?.treePrev
    }

    private fun shouldProcess(run: ASTNode): Boolean {
        val type = run.elementType
        if (type === TokenType.WHITE_SPACE || type === TactTypes.LBRACE || type === TactTypes.RBRACE) return false
        if (type === TactTypes.SIMPLE_STATEMENT) {
            return run.firstChildNode == null
        }
        return true
    }

    fun processChildrenFromTop(
        element: PsiElement,
        processor: PsiScopeProcessor,
        substitutor: ResolveState,
        lastParent: PsiElement?,
        place: PsiElement,
    ): Boolean {
        var run = element.firstChild
        while (run != null) {
            if (run is TactCompositeElement) {
                if (run.isEquivalentTo(lastParent)) return true
                if (!run.processDeclarations(processor, substitutor, null, place)) return false
            }
            run = run.nextSibling
        }
        return true
    }
}
