package org.tonstudio.tact.lang.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import org.tonstudio.tact.lang.psi.TactBlock
import org.tonstudio.tact.lang.psi.TactStatement
import org.tonstudio.tact.lang.psi.TactVarDefinition

class TactVarReference(element: TactVarDefinition) : TactCachedReference<TactVarDefinition>(element) {
    private val contextBlock = element.parentOfType<TactBlock>() ?: element.containingFile

    override fun resolveInner(): PsiElement? {
        val p = TactVarProcessor(myElement, false)
        processResolveVariants(p)
        return p.getResult()
    }

    override fun processResolveVariants(processor: TactScopeProcessor): Boolean {
        if (contextBlock == null || element.isBlank()) {
            return false
        }

        val proc = if (processor is TactVarProcessor)
            processor
        else
            object : TactVarProcessor(myElement, processor.isCompletion()) {
                override fun execute(e: PsiElement, state: ResolveState): Boolean {
                    return super.execute(e, state) && processor.execute(e, state)
                }
            }

        if (!contextBlock.processDeclarations(
                proc,
                ResolveState.initial(),
                PsiTreeUtil.getParentOfType(myElement, TactStatement::class.java),
                myElement
            )
        ) {
            return false
        }

        return ResolveUtil.treeWalkUp(myElement, proc)
    }
}
