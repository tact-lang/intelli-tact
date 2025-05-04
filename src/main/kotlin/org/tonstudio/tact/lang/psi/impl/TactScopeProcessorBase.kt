package org.tonstudio.tact.lang.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import org.tonstudio.tact.lang.psi.TactFunctionDeclaration
import org.tonstudio.tact.lang.psi.TactNamedElement

abstract class TactScopeProcessorBase(
    private val requestedNameElement: PsiElement,
    val origin: PsiElement,
    private val isForCompletion: Boolean,
) : TactScopeProcessor() {

    private val result = mutableSetOf<TactNamedElement>()

    constructor(origin: PsiElement) : this(origin, origin, false)

    override fun execute(e: PsiElement, state: ResolveState): Boolean {
        if (e is TactFunctionDeclaration) {
            return false
        }

        if (e !is TactNamedElement) {
            return true
        }

        val name = state.get(TactReferenceBase.SEARCH_NAME) ?: e.name ?: return true
        if (name.isEmpty() || !isForCompletion && !requestedNameElement.textMatches(name)) {
            return true
        }
        if (crossOff(e)) {
            return true
        }

        if (e == origin) {
            return true
        }

        return add(e) || isForCompletion
    }

    protected open fun add(psiElement: TactNamedElement): Boolean {
        return !result.add(psiElement)
    }

    fun getResult(): TactNamedElement? = result.firstOrNull()

    fun getVariants() = result

    protected abstract fun crossOff(e: PsiElement): Boolean

    override fun isCompletion() = isForCompletion
}
