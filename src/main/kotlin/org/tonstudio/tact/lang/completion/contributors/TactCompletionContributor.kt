package org.tonstudio.tact.lang.completion.contributors

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import org.tonstudio.tact.lang.completion.TactCompletionPatterns.cachedReferenceExpression
import org.tonstudio.tact.lang.completion.TactCompletionPatterns.onAsmInstruction
import org.tonstudio.tact.lang.completion.TactCompletionPatterns.onContractTraitTopLevel
import org.tonstudio.tact.lang.completion.TactCompletionPatterns.onStatement
import org.tonstudio.tact.lang.completion.TactCompletionPatterns.referenceExpression
import org.tonstudio.tact.lang.completion.providers.*
import org.tonstudio.tact.lang.completion.sort.withTactSorter

class TactCompletionContributor : CompletionContributor() {
    init {
        extend(CompletionType.BASIC, referenceExpression(), ReferenceCompletionProvider)
        extend(CompletionType.BASIC, cachedReferenceExpression(), ReferenceCompletionProvider)
        extend(CompletionType.BASIC, referenceExpression(), TlbTypesCompletionProvider)

        extend(CompletionType.BASIC, onContractTraitTopLevel(), ReceiversCompletionProvider)
        extend(CompletionType.BASIC, onContractTraitTopLevel(), ContractInitCompletionProvider)
        extend(CompletionType.BASIC, onContractTraitTopLevel(), ContractTraitMembersCompletionProvider)
        extend(CompletionType.BASIC, onContractTraitTopLevel(), GetterCompletionProvider)
        extend(CompletionType.BASIC, onContractTraitTopLevel(), OverrideCompletionProvider)

        extend(CompletionType.BASIC, onStatement(), ReturnCompletionProvider)

        extend(CompletionType.BASIC, onAsmInstruction(), AsmInstructionsCompletionProvider)
    }

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        super.fillCompletionVariants(parameters, withTactSorter(parameters, result))
    }
}
