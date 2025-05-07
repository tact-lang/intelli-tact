package org.tonstudio.tact.lang.completion.contributors

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.psi.util.parentOfTypes
import com.intellij.util.ProcessingContext
import org.tonstudio.tact.ide.ui.Icons
import org.tonstudio.tact.lang.completion.TactCompletionPatterns.onExpression
import org.tonstudio.tact.lang.completion.TactCompletionPatterns.onIfElse
import org.tonstudio.tact.lang.completion.TactCompletionPatterns.onStatement
import org.tonstudio.tact.lang.completion.TactCompletionPatterns.onTopLevel
import org.tonstudio.tact.lang.completion.TactCompletionUtil.KEYWORD_PRIORITY
import org.tonstudio.tact.lang.completion.TactCompletionUtil.StringInsertHandler
import org.tonstudio.tact.lang.completion.TactCompletionUtil.TemplateStringInsertHandler
import org.tonstudio.tact.lang.completion.TactCompletionUtil.showCompletion
import org.tonstudio.tact.lang.completion.TactCompletionUtil.toTactLookupElement
import org.tonstudio.tact.lang.completion.TactCompletionUtil.withPriority
import org.tonstudio.tact.lang.completion.TactLookupElementProperties
import org.tonstudio.tact.lang.completion.sort.withTactSorter
import org.tonstudio.tact.lang.psi.TactContractDeclaration
import org.tonstudio.tact.lang.psi.TactFile
import org.tonstudio.tact.lang.psi.TactTraitDeclaration

class TactKeywordsCompletionContributor : CompletionContributor() {
    init {
        // Top Level
        extend(CompletionType.BASIC, onTopLevel(), ConstCompletionProvider)
        extend(CompletionType.BASIC, onTopLevel(), ImportsCompletionProvider())

        extend(
            CompletionType.BASIC,
            onTopLevel(),
            ClassLikeSymbolCompletionProvider(
                "struct",
                "trait",
                "message",
                "contract",
            )
        )

        extend(
            CompletionType.BASIC,
            onStatement(),
            ConditionBlockKeywordCompletionProvider("if")
        )
        extend(
            CompletionType.BASIC,
            onExpression(),
            KeywordsCompletionProvider(
                "null",
                "true",
                "false",
                needSpace = false,
            )
        )

        extend(
            CompletionType.BASIC,
            onExpression(),
            SelfCompletionProvider,
        )

        extend(
            CompletionType.BASIC,
            onExpression(),
            InitOfCodeOfProvider,
        )

        // Other
        extend(
            CompletionType.BASIC,
            onIfElse(),
            ElseIfKeywordCompletionProvider(),
        )
    }

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        super.fillCompletionVariants(parameters, withTactSorter(parameters, result))
    }

    private val elseElement = LookupElementBuilder.create("else")
        .bold()
        .withTailText(" {...}")
        .withInsertHandler(StringInsertHandler(" {  }", 3))
        .withPriority(KEYWORD_PRIORITY)

    private object ConstCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val constElement = LookupElementBuilder.create("const")
                .withTailText(" name: Type = value")
                .withInsertHandler(
                    TemplateStringInsertHandler(
                        " \$name$: \$type$ = \$value$;", true,
                        "name" to ConstantNode("FOO"),
                        "type" to ConstantNode("Int"),
                        "value" to ConstantNode("0"),
                    )
                )
                .bold()
                .withPriority(KEYWORD_PRIORITY)

            result.addElement(constElement)
        }
    }

    private object SelfCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val owner = parameters.position.parentOfTypes(TactTraitDeclaration::class, TactContractDeclaration::class) ?: return

            val icon = if (owner is TactTraitDeclaration) Icons.Trait else Icons.Contract

            val constElement = LookupElementBuilder.create("self")
                .withTypeText(owner.getType(null)?.name() ?: "")
                .withIcon(icon)
                .bold()
                .withPriority(KEYWORD_PRIORITY)

            result.addElement(constElement)
        }
    }

    private object InitOfCodeOfProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val file = parameters.position.containingFile as? TactFile ?: return
            val contractToSuggest = file.getContracts().firstOrNull()?.name ?: "Foo"

            val initOfElement = LookupElementBuilder.create("initOf")
                .bold()
                .withTailText(" Contract(<params>)")
                .withInsertHandler(
                    TemplateStringInsertHandler(
                        " \$name$(\$params$)\$END$", true, "name" to ConstantNode(contractToSuggest), "params" to ConstantNode("")
                    )
                )
                .withPriority(KEYWORD_PRIORITY)

            result.addElement(initOfElement)

            val codeOfElement = LookupElementBuilder.create("codeOf")
                .bold()
                .withTailText(" Contract")
                .withInsertHandler(
                    TemplateStringInsertHandler(
                        " \$name$\$END$", true, "name" to ConstantNode(contractToSuggest)
                    )
                )
                .withPriority(KEYWORD_PRIORITY)

            result.addElement(codeOfElement)
        }
    }

    private inner class ElseIfKeywordCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val elseIfElement = LookupElementBuilder.create("else if")
                .bold()
                .withTailText(" (expr) {...}")
                .withInsertHandler(
                    TemplateStringInsertHandler(" (\$expr$) { \$END$ }", true, "expr" to ConstantNode("expr"))
                )
                .withPriority(KEYWORD_PRIORITY)

            result.addElement(elseElement)
            result.addElement(elseIfElement)
        }
    }

    open class KeywordsCompletionProvider(
        private vararg val keywords: String,
        private val needSpace: Boolean = false,
        private val properties: TactLookupElementProperties = TactLookupElementProperties(),
    ) : CompletionProvider<CompletionParameters>() {

        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            result: CompletionResultSet,
        ) {
            val elements = keywords.map { keyword ->
                LookupElementBuilder.create(keyword)
                    .withInsertHandler(StringInsertHandler(" ", 1, needSpace))
                    .bold()
                    .withPriority(KEYWORD_PRIORITY)
                    .toTactLookupElement(properties)
            }
            result.addAllElements(elements)
        }
    }

    private inner class ConditionBlockKeywordCompletionProvider(private vararg val keywords: String) :
        CompletionProvider<CompletionParameters>() {

        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val elements = keywords.map {
                LookupElementBuilder.create(it)
                    .withTailText(" (expr) {...}")
                    .withInsertHandler(
                        TemplateStringInsertHandler(
                            " (\$expr$) {\n\$END$\n}", true,
                            "expr" to ConstantNode("expr")
                        )
                    )
                    .bold()
                    .withPriority(KEYWORD_PRIORITY)
            }

            result.addAllElements(elements)
        }
    }

    private inner class ClassLikeSymbolCompletionProvider(private vararg val keywords: String) :
        CompletionProvider<CompletionParameters>() {

        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val elements = keywords.map { keyword ->
                LookupElementBuilder.create(keyword)
                    .withTailText(" Name {...}")
                    .withInsertHandler(
                        TemplateStringInsertHandler(
                            " \$name$ {\n\$END$\n}", true,
                            "name" to ConstantNode("Name")
                        )
                    )
                    .bold()
                    .withPriority(KEYWORD_PRIORITY)
                    .toTactLookupElement(
                        TactLookupElementProperties(
                            isContextElement = true,
                        )
                    )
            }

            result.addAllElements(elements)
        }
    }

    class ImportsCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val element = LookupElementBuilder.create("import")
                .withTailText(" \"\"")
                .withInsertHandler { ctx, item ->
                    StringInsertHandler(" \"\";", 2).handleInsert(ctx, item)
                    showCompletion(ctx.editor)
                }
                .bold()
                .withPriority(KEYWORD_PRIORITY)

            result.addElement(element)
        }
    }
}
