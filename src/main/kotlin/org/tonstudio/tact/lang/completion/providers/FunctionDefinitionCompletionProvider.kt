package org.tonstudio.tact.lang.completion.providers

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.util.ProcessingContext
import org.tonstudio.tact.lang.completion.TactCompletionUtil

object FunctionDefinitionCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        val modifiers = listOf("", "inline ", "extends ", "asm ")
        modifiers.forEach { modifier ->
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

        val extendsPrefixes = listOf("", "asm ")
        val extendsModifiers = listOf("", "mutates ")
        extendsPrefixes.forEach { prefix ->
            extendsModifiers.forEach { modifier ->
                val extendsElement = LookupElementBuilder.create("${prefix}extends ${modifier}fun")
                    .bold()
                    .withTailText(" <name>(self: Type) {}", true)
                    .withInsertHandler(
                        TactCompletionUtil.TemplateStringInsertHandler(
                            " \$name$(self: \$type$\$params$)\$return$ {\$END$}",
                            true,
                            "name" to ConstantNode("add"),
                            "type" to ConstantNode("Int"),
                            "params" to ConstantNode(""),
                            "return" to ConstantNode(""),
                        )
                    )

                result.addElement(extendsElement)
            }
        }
    }
}
