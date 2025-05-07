package org.tonstudio.tact.lang.search

import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.psi.search.searches.DefinitionsScopedSearch
import com.intellij.util.Processor
import org.tonstudio.tact.lang.psi.*

class TactFunctionImplementationsSearch : QueryExecutorBase<TactFunctionDeclaration, DefinitionsScopedSearch.SearchParameters>(true) {
    override fun processQuery(
        parameters: DefinitionsScopedSearch.SearchParameters,
        consumer: Processor<in TactFunctionDeclaration>,
    ) {
        if (!parameters.isCheckDeep) return

        val func = parameters.element as? TactFunctionDeclaration ?: return
        val trait = func.getOwner() as? TactTraitDeclaration ?: return

        TactImplementationsSearch().processQuery(DefinitionsScopedSearch.SearchParameters(trait)) { t ->
            val implementations = t.getMethodsList().filter { m -> m.name == func.name }
            for (implementation in implementations) {
                consumer.process(implementation)
            }
            true
        }
    }
}
