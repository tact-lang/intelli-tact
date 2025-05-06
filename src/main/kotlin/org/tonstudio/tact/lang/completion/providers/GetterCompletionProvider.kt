package org.tonstudio.tact.lang.completion.providers

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import org.tonstudio.tact.lang.completion.TactCompletionUtil.TemplateStringInsertHandler
import org.tonstudio.tact.lang.psi.*

object GetterCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, ctx: ProcessingContext, result: CompletionResultSet) {
        val owner = parameters.position.parentOfType<TactStorageMembersOwner>() ?: return

        val fields = owner.getFieldList()
        fields.forEach { field ->
            val name = field.name ?: return@forEach
            val type = field.getType(null) ?: return@forEach
            val element = LookupElementBuilder.create(name)
                .withTailText(" generate getter for \"${name}\" field")
                .withInsertHandler { context, item ->
                    // remove inserted name
                    val doc = context.document
                    val start = context.startOffset
                    doc.deleteString(start, start + name.length)
                    TemplateStringInsertHandler(
                        "get fun ${name}(): ${type.name()} {\n\treturn self.${name};\n}", true
                    ).handleInsert(context, item)
                }
            result.addElement(element)
        }

        if (owner is TactContractType) {
            val decl = owner.parent as? TactContractDeclaration
            val name = decl?.name ?: return
            val element = LookupElementBuilder.create("state")
                .withTailText(" generate getter for all contract state")
                .withInsertHandler { context, item ->
                    // remove inserted name
                    val doc = context.document
                    val start = context.startOffset
                    doc.deleteString(start, start + "state".length)
                    TemplateStringInsertHandler(
                        "get fun contractState(): $name {\n\treturn self;\n}", true
                    ).handleInsert(context, item)
                }
            result.addElement(element)
        }
    }
}
