package org.tonstudio.tact.lang.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor
import com.intellij.psi.util.PsiTreeUtil
import org.tonstudio.tact.lang.psi.*

open class TactCompositeElementImpl(node: ASTNode) : ASTWrapperPsiElement(node), TactCompositeElement {
    override fun toString(): String = node.elementType.toString()

    override fun processDeclarations(
        processor: PsiScopeProcessor,
        state: ResolveState,
        lastParent: PsiElement?,
        place: PsiElement,
    ): Boolean {
        if (this is TactBlock ||
            this is TactIfStatement ||
            this is TactWhileStatement ||
            this is TactUntilStatement ||
            this is TactTryStatement ||
            this is TactForEachStatement
        ) {
            if (!processor.execute(this, state)) {
                return false
            }
        }

        return processDeclarationsDefault(this, processor, state, lastParent, place)
    }

    companion object {
        fun processDeclarationsDefault(
            o: TactCompositeElement,
            processor: PsiScopeProcessor,
            state: ResolveState,
            lastParent: PsiElement?,
            place: PsiElement,
        ): Boolean {
            if (o is TactExpression && o !is TactIfStatement) {
                return true
            }
            if (!processor.execute(o, state)) {
                return false
            }
            if ((o is TactIfStatement || o is TactWhileStatement || o is TactUntilStatement || o is TactForEachStatement || o is TactTryStatement || o is TactBlock)
                && processor is TactScopeProcessorBase
            ) {
                if (!PsiTreeUtil.isAncestor(o, processor.origin, false)) {
                    return true
                }
            }
            return if (o is TactBlock)
                processBlock(o, processor, state, lastParent, place)
            else
                ResolveUtil.processChildren(o, processor, state, lastParent, place)
        }

        private fun processBlock(
            o: TactBlock,
            processor: PsiScopeProcessor,
            state: ResolveState,
            lastParent: PsiElement?, place: PsiElement,
        ): Boolean {
            return ResolveUtil.processChildrenFromTop(o, processor, state, lastParent, place) &&
                    processParameters(o, processor)
        }

        private fun processParameters(b: TactBlock, processor: PsiScopeProcessor): Boolean {
            if (processor !is TactScopeProcessorBase || b.parent !is TactSignatureOwner) {
                return true
            }

            return TactPsiImplUtil.processSignatureOwner(
                b.parent as TactSignatureOwner,
                processor
            )
        }
    }
}

