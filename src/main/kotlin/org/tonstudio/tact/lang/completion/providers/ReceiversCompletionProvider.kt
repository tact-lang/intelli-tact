package org.tonstudio.tact.lang.completion.providers

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.util.ProcessingContext
import org.tonstudio.tact.lang.completion.TactCompletionUtil.TemplateStringInsertHandler
import org.tonstudio.tact.lang.psi.TactFile

object ReceiversCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        val options = listOf("receive", "external")

        val file = parameters.position.containingFile as? TactFile ?: return
        val messageToSuggest = file.getMessages().firstOrNull()?.name ?: "Foo"

        options.forEach { name ->
            val element = LookupElementBuilder.create(name)
                .withPresentableText(name)
                .withLookupString(name)
                .bold()
                .withTailText("(msg: <type>) {}", true)
                .withInsertHandler(
                    TemplateStringInsertHandler(
                        "(msg: \$type$) {\$END$}",
                        true,
                        "type" to ConstantNode(messageToSuggest),
                    )
                )

            result.addElement(element)

            val element2 = LookupElementBuilder.create(name + "2")
                .withPresentableText(name)
                .withLookupString(name)
                .bold()
                .withTailText("(\"<message>\") {}", true)
                .withInsertHandler { context, item ->
                    // remove suffix "2"
                    val doc = context.document
                    val start = context.startOffset
                    doc.deleteString(start + name.length, start + name.length + 1)
                    TemplateStringInsertHandler(
                        "(\"\$type$\") {\$END$}",
                        true,
                        "type" to ConstantNode("")
                    ).handleInsert(context, item)
                }

            result.addElement(element2)

            val element3 = LookupElementBuilder.create(name + "3")
                .withPresentableText(name)
                .withLookupString(name)
                .bold()
                .withTailText("() {}", true)
                .withInsertHandler { context, item ->
                    // remove suffix "3"
                    val doc = context.document
                    val start = context.startOffset
                    doc.deleteString(start + name.length, start + name.length + 1)
                    TemplateStringInsertHandler(
                        "() {\$END$}",
                    ).handleInsert(context, item)
                }

            result.addElement(element3)
        }

        val bounced = LookupElementBuilder.create("bounced")
            .bold()
            .withTailText("(msg: <type>) {}", true)
            .withInsertHandler(
                TemplateStringInsertHandler(
                    "(msg: \$type$) {\$END$}",
                    true,
                    "type" to ConstantNode(messageToSuggest),
                )
            )

        result.addElement(bounced)
    }
}
