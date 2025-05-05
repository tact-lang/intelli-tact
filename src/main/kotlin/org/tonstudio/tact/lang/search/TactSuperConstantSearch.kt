package org.tonstudio.tact.lang.search

import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.psi.search.searches.DefinitionsScopedSearch
import com.intellij.util.CommonProcessors
import com.intellij.util.Processor
import org.tonstudio.tact.lang.psi.TactConstDefinition
import org.tonstudio.tact.lang.psi.TactContractDeclaration
import org.tonstudio.tact.lang.psi.TactTraitDeclaration
import org.tonstudio.tact.lang.psi.constants

fun hasSuperConstant(method: TactConstDefinition): Boolean {
    val processor = CommonProcessors.FindFirstProcessor<TactConstDefinition>()
    TactSuperConstantSearch().processQuery(DefinitionsScopedSearch.SearchParameters(method), processor)
    return processor.isFound
}

class TactSuperConstantSearch : QueryExecutorBase<TactConstDefinition, DefinitionsScopedSearch.SearchParameters>(true) {
    override fun processQuery(
        parameters: DefinitionsScopedSearch.SearchParameters,
        consumer: Processor<in TactConstDefinition>,
    ) {
        if (!parameters.isCheckDeep) return

        val const = parameters.element as? TactConstDefinition ?: return
        val constName = const.name

        val owner = const.getOwner() ?: return
        val ownerType = when (owner) {
            is TactContractDeclaration -> owner.contractType
            is TactTraitDeclaration    -> owner.traitType
            else                       -> null
        } ?: return

        val inheritedTraits = ownerType.getInheritedTraits()
        if (inheritedTraits.isEmpty()) return

        val superTraitWithConst = inheritedTraits.find { it.traitType.constants().find { const -> const.name == constName } != null }
        if (superTraitWithConst == null) return

        val superConstant = superTraitWithConst.traitType.constants().find { m -> m.name == constName }
        consumer.process(superConstant)
    }
}
