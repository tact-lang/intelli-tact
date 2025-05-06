package org.tonstudio.tact.lang.completion.providers

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import org.tonstudio.tact.lang.completion.TactCompletionUtil.TemplateStringInsertHandler
import org.tonstudio.tact.lang.psi.*

object OverrideCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, ctx: ProcessingContext, result: CompletionResultSet) {
        val owner = parameters.position.parentOfType<TactStorageMembersOwner>() ?: return

        val inheritedTraits = owner.getInheritedTraits()
        if (inheritedTraits.isEmpty()) return

        val inheritedMethods = inheritedTraits.flatMap { it.traitType.getMethodsList() }.associateBy { it.name }

        val added = mutableSetOf<String>()

        // add already defined methods to avoid duplicates
        for (ownMethod in owner.getMethodsList()) {
            added.add(ownMethod.name)
        }

        for ((index, method) in inheritedMethods.values.withIndex()) {
            if (!method.isAbstract && !method.isVirtual) continue
            if (added.contains(method.name)) continue

            val name = method.name
            val methodOwner = method.getOwner() ?: continue
            val lookup = "override${index}"
            val element = LookupElementBuilder.create(lookup)
                .withPresentableText("override")
                .withTailText(" fun ${name}${method.getSignature().text} {} of ${methodOwner.name}")
                .withInsertHandler { context, item ->
                    // remove inserted index
                    val doc = context.document
                    val start = context.startOffset
                    doc.deleteString(start + lookup.length - 1, start + lookup.length)
                    TemplateStringInsertHandler(
                        " fun ${name}${method.getSignature().text} {\$END$}", true
                    ).handleInsert(context, item)
                }
            result.addElement(element)
        }
    }
}
