package org.tonstudio.tact.lang.search

import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.psi.search.searches.DefinitionsScopedSearch
import com.intellij.util.Processor
import org.tonstudio.tact.lang.psi.*

class TactConstantImplementationsSearch : QueryExecutorBase<TactConstDefinition, DefinitionsScopedSearch.SearchParameters>(true) {
    override fun processQuery(
        parameters: DefinitionsScopedSearch.SearchParameters,
        consumer: Processor<in TactConstDefinition>,
    ) {
        if (!parameters.isCheckDeep) return

        val constant = parameters.element as? TactConstDefinition ?: return
        val trait = constant.getOwner() as? TactTraitDeclaration ?: return

        TactImplementationsSearch().processQuery(DefinitionsScopedSearch.SearchParameters(trait), { t ->
            val implementations = t.getConstantsList().filter { c -> c.name == constant.name }
            for (implementation in implementations) {
                consumer.process(implementation)
            }
            true
        })
    }
}
