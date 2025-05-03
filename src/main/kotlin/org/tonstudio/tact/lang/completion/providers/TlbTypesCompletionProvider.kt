package org.tonstudio.tact.lang.completion.providers

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import org.tonstudio.tact.lang.completion.TactCompletionUtil.TemplateStringInsertHandler
import org.tonstudio.tact.lang.psi.TactFieldDeclaration
import org.tonstudio.tact.lang.psi.TactTlb
import org.tonstudio.tact.lang.psi.types.TactBaseTypeEx.Companion.toEx
import org.tonstudio.tact.lang.psi.types.TactPrimitiveTypeEx
import org.tonstudio.tact.lang.psi.types.TactPrimitiveTypes
import org.tonstudio.tact.utils.inside

object TlbTypesCompletionProvider : CompletionProvider<CompletionParameters>() {
    private val INT_VARIANTS = listOf(
        "uint8",
        "uint16",
        "uint32",
        "uint64",
        "uint128",
        "uint256",
        "int8",
        "int16",
        "int32",
        "int64",
        "int128",
        "int256",
        "int257",
        "coins",
    )

    private val VARIADIC_INT_VARIANTS = listOf(
        "int{X}" to "\$bits$",
        "uint{X}" to "\$bits\$"
    )

    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        val element = parameters.position
        if (!element.inside<TactTlb>()) return

        val parentField = element.parentOfType<TactFieldDeclaration>()
        val fieldType = parentField?.type?.toEx() ?: return

        if (fieldType is TactPrimitiveTypeEx && fieldType.name == TactPrimitiveTypes.INT) {
            INT_VARIANTS.forEach { variant ->
                val elem = LookupElementBuilder.create(variant)
                    .bold()

                result.addElement(elem)
            }

            VARIADIC_INT_VARIANTS.forEach { (variant, template) ->
                val elem = LookupElementBuilder.create(variant)
                    .withPresentableText(variant)
                    .bold()
                    .withInsertHandler { context, item ->
                        // remove suffix "{X}"
                        val doc = context.document
                        val start = context.startOffset
                        doc.deleteString(start + variant.length - 3, start + variant.length)
                        TemplateStringInsertHandler(template, true, "bits" to ConstantNode("8")).handleInsert(context, item)
                    }

                result.addElement(elem)
            }
        }

        if (
            fieldType is TactPrimitiveTypeEx &&
            (fieldType.name == TactPrimitiveTypes.SLICE ||
                    fieldType.name == TactPrimitiveTypes.CELL ||
                    fieldType.name == TactPrimitiveTypes.BUILDER)
        ) {
            val elem = LookupElementBuilder.create("remaining").bold()
            result.addElement(elem)
        }
    }
}
