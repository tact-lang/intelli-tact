package org.tonstudio.tact.lang.completion.providers

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.util.ProcessingContext
import org.tonstudio.tact.lang.completion.TactCompletionUtil
import org.tonstudio.tact.lang.psi.TactContractDeclaration
import org.tonstudio.tact.utils.inside

object ContractInitCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        if (!parameters.position.inside<TactContractDeclaration>()) {
            return
        }

        val element = LookupElementBuilder.create("init")
            .bold()
            .withTailText("() {}", true)
            .withInsertHandler(
                TactCompletionUtil.TemplateStringInsertHandler(
                    "(\$params$) {\$END$}",
                    true,
                    "params" to ConstantNode(""),
                )
            )

        result.addElement(element)
    }
}
