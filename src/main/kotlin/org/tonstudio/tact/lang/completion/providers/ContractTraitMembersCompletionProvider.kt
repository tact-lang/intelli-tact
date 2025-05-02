package org.tonstudio.tact.lang.completion.providers

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.util.ProcessingContext
import org.tonstudio.tact.lang.completion.TactCompletionUtil
import org.tonstudio.tact.lang.psi.TactTraitDeclaration
import org.tonstudio.tact.utils.inside

object ContractTraitMembersCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        val insideTrait = parameters.position.inside<TactTraitDeclaration>()

        val modifiers = listOf("", "inline ", "get ", "virtual ")
        modifiers.forEach { modifier ->
            if (!insideTrait && modifier == "virtual ") {
                return
            }

            val element = LookupElementBuilder.create("${modifier}fun")
                .bold()
                .withTailText(" <name>() {}", true)
                .withInsertHandler(
                    TactCompletionUtil.TemplateStringInsertHandler(
                        " \$name$(\$params$)\$return$ {\$END$}",
                        true,
                        "name" to ConstantNode("foo"),
                        "params" to ConstantNode(""),
                        "return" to ConstantNode(""),
                    )
                )

            result.addElement(element)
        }

        if (insideTrait) {
            val abstractFunElement = LookupElementBuilder.create("abstract fun")
                .bold()
                .withTailText(" <name>();", true)
                .withInsertHandler(
                    TactCompletionUtil.TemplateStringInsertHandler(
                        " \$name$(\$params$)\$return$;",
                        true,
                        "name" to ConstantNode("foo"),
                        "params" to ConstantNode(""),
                        "return" to ConstantNode(""),
                    )
                )

            result.addElement(abstractFunElement)
        }

        val constModifiers = listOf("", "virtual ")
        constModifiers.forEach { modifier ->
            if (!insideTrait && modifier == "virtual ") {
                return
            }

            val element = LookupElementBuilder.create("${modifier}const")
                .bold()
                .withTailText(" <name>: <type> = <value>;", true)
                .withInsertHandler(
                    TactCompletionUtil.TemplateStringInsertHandler(
                        " \$name$: \$type$ = \$value$;",
                        true,
                        "name" to ConstantNode("FOO"),
                        "type" to ConstantNode("Int"),
                        "value" to ConstantNode("0"),
                    )
                )

            result.addElement(element)
        }

        if (insideTrait) {
            val element = LookupElementBuilder.create("abstract const")
                .bold()
                .withTailText(" <name>: <type>;", true)
                .withInsertHandler(
                    TactCompletionUtil.TemplateStringInsertHandler(
                        " \$name$: \$type$;",
                        true,
                        "name" to ConstantNode("FOO"),
                        "type" to ConstantNode("Int"),
                    )
                )

            result.addElement(element)
        }
    }
}
