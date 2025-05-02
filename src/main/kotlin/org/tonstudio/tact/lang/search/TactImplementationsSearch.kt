package org.tonstudio.tact.lang.search

import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.psi.PsiElement
import com.intellij.psi.search.searches.DefinitionsScopedSearch
import com.intellij.util.CommonProcessors
import com.intellij.util.Processor
import org.tonstudio.tact.lang.psi.*
import org.tonstudio.tact.lang.stubs.index.TactContractsTraitsIndex

fun <T : PsiElement> hasImplementations(
    element: T,
    search: QueryExecutorBase<T, DefinitionsScopedSearch.SearchParameters>,
): Boolean {
    val processor = CommonProcessors.FindFirstProcessor<T>()
    search.processQuery(DefinitionsScopedSearch.SearchParameters(element), processor)
    return processor.isFound
}

class TactImplementationsSearch : QueryExecutorBase<TactStorageMembersOwner, DefinitionsScopedSearch.SearchParameters>(true) {
    override fun processQuery(
        parameters: DefinitionsScopedSearch.SearchParameters,
        consumer: Processor<in TactStorageMembersOwner>,
    ) {
        if (!parameters.isCheckDeep) return

        val trait = parameters.element as? TactTraitDeclaration ?: return
        TactContractsTraitsIndex.processAll(parameters.project, { el ->
            val elType = when (el) {
                is TactContractDeclaration -> el.contractType
                is TactTraitDeclaration    -> el.traitType
                else                       -> return@processAll true
            }

            val inheritedTraits = elType.getInheritedTraits()
            if (inheritedTraits.isEmpty()) return@processAll true

            val inheritsFromTrait = inheritedTraits.any { it.name == trait.name }
            if (inheritsFromTrait) {
                if (!consumer.process(elType)) return@processAll false
            }
            true
        })
    }
}
