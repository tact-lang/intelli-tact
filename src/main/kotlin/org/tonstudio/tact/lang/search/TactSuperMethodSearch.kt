package org.tonstudio.tact.lang.search

import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.psi.search.searches.DefinitionsScopedSearch
import com.intellij.util.CommonProcessors
import com.intellij.util.Processor
import org.tonstudio.tact.lang.psi.TactContractDeclaration
import org.tonstudio.tact.lang.psi.TactFunctionDeclaration
import org.tonstudio.tact.lang.psi.TactTraitDeclaration
import org.tonstudio.tact.lang.psi.methods

fun hasSuperMethod(method: TactFunctionDeclaration): Boolean {
    val processor = CommonProcessors.FindFirstProcessor<TactFunctionDeclaration>()
    TactSuperMethodSearch().processQuery(DefinitionsScopedSearch.SearchParameters(method), processor)
    return processor.isFound
}

class TactSuperMethodSearch : QueryExecutorBase<TactFunctionDeclaration, DefinitionsScopedSearch.SearchParameters>(true) {
    override fun processQuery(
        parameters: DefinitionsScopedSearch.SearchParameters,
        consumer: Processor<in TactFunctionDeclaration>,
    ) {
        if (!parameters.isCheckDeep) return

        val method = parameters.element as? TactFunctionDeclaration ?: return
        val methodName = method.name

        val owner = method.getOwner() ?: return
        val ownerType = when (owner) {
            is TactContractDeclaration -> owner.contractType
            is TactTraitDeclaration    -> owner.traitType
            else                       -> null
        } ?: return

        val inheritedTraits = ownerType.getInheritedTraits()
        if (inheritedTraits.isEmpty()) return

        val superTraitWithFun = inheritedTraits.find { it.traitType.methods().find { method -> method.name == methodName } != null }
        if (superTraitWithFun == null) return

        val superMethod = superTraitWithFun.traitType.methods().find { m -> m.name == methodName }
        consumer.process(superMethod)
    }
}
