package org.tonstudio.tact.ide.documentation

import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.psi.PsiElement
import org.tonstudio.tact.ide.documentation.DocumentationUtils.asKeyword
import org.tonstudio.tact.ide.documentation.DocumentationUtils.line
import org.tonstudio.tact.ide.documentation.DocumentationUtils.part
import org.tonstudio.tact.ide.documentation.DocumentationUtils.appendNotNull
import org.tonstudio.tact.ide.documentation.DocumentationUtils.asAttribute
import org.tonstudio.tact.ide.documentation.DocumentationUtils.asParameter
import org.tonstudio.tact.ide.documentation.DocumentationUtils.asParen
import org.tonstudio.tact.ide.documentation.DocumentationUtils.asType
import org.tonstudio.tact.ide.documentation.DocumentationUtils.colorize
import org.tonstudio.tact.lang.psi.*
import org.tonstudio.tact.lang.psi.types.*
import org.tonstudio.tact.lang.psi.types.TactBaseTypeEx.Companion.toEx
import kotlin.math.max
import io.ktor.util.*
import org.tonstudio.tact.asm.findInstruction
import org.tonstudio.tact.asm.getStackPresentation
import org.tonstudio.tact.ide.documentation.DocumentationUtils.asAsmInstruction
import org.tonstudio.tact.ide.documentation.DocumentationUtils.asBraces
import org.tonstudio.tact.ide.documentation.DocumentationUtils.asBuiltin
import org.tonstudio.tact.ide.documentation.DocumentationUtils.asComment
import org.tonstudio.tact.ide.documentation.DocumentationUtils.asConst
import org.tonstudio.tact.ide.documentation.DocumentationUtils.asContract
import org.tonstudio.tact.ide.documentation.DocumentationUtils.asField
import org.tonstudio.tact.ide.documentation.DocumentationUtils.asFunction
import org.tonstudio.tact.ide.documentation.DocumentationUtils.asIdentifier
import org.tonstudio.tact.ide.documentation.DocumentationUtils.asMessage
import org.tonstudio.tact.ide.documentation.DocumentationUtils.asNativeFunction
import org.tonstudio.tact.ide.documentation.DocumentationUtils.asNumber
import org.tonstudio.tact.ide.documentation.DocumentationUtils.asOperator
import org.tonstudio.tact.ide.documentation.DocumentationUtils.asPrimitive
import org.tonstudio.tact.ide.documentation.DocumentationUtils.asString
import org.tonstudio.tact.ide.documentation.DocumentationUtils.asStruct
import org.tonstudio.tact.ide.documentation.DocumentationUtils.asTrait
import org.tonstudio.tact.lang.TactSyntaxHighlighter
import org.tonstudio.tact.lang.doc.psi.TactDocComment

private fun StringBuilder.generateOwnerDpc(element: TactNamedElement) {
    val owner = element.getOwner() ?: return

    val kind = owner.kindPresentation()
    val name = owner.name ?: ""

    val nameColor = when (element) {
        is TactContractDeclaration -> asContract
        is TactTraitDeclaration -> asTrait
        is TactMessageDeclaration -> asMessage
        is TactStructDeclaration -> asStruct
        else -> asStruct
    }

    colorize(kind, asKeyword)
    append(" ")
    colorize(name, nameColor)
    append("\n")
}

fun TactFunctionDeclaration.generateDoc(): String {
    val parameters = getSignature().parameters
    val returnType = getSignature().result

    return buildString {
        append(DocumentationMarkup.DEFINITION_START)
        generateOwnerDpc(this@generateDoc)

        line(attributes?.generateDoc())

        append(functionAttributeList.generateDoc())

        part("fun", asKeyword)
        colorize(name, asFunction)

        append(parameters.generateDoc())
        if (returnType != null) {
            append(": ")
            appendNotNull(returnType.generateDoc())
        }

        append(DocumentationMarkup.DEFINITION_END)
        generateCommentsPart(this@generateDoc)
    }
}

fun TactAsmFunctionDeclaration.generateDoc(): String {
    val parameters = getSignature()?.parameters
    val returnType = getSignature()?.result

    return buildString {
        append(DocumentationMarkup.DEFINITION_START)
        generateOwnerDpc(this@generateDoc)

        append(functionAttributeList.generateDoc())

        part("asm", asKeyword)
        part("fun", asKeyword)
        colorize(name ?: "anon", asFunction)

        append(parameters?.generateDoc())
        if (returnType != null) {
            append(": ")
            appendNotNull(returnType.generateDoc())
        }

        append(DocumentationMarkup.DEFINITION_END)
        generateCommentsPart(this@generateDoc)
    }
}

fun TactNativeFunctionDeclaration.generateDoc(): String? {
    val signature = getSignature() ?: return null
    val parameters = signature.parameters
    val returnType = signature.result

    return buildString {
        append(DocumentationMarkup.DEFINITION_START)
        line(attributes?.generateDoc())
        generateOwnerDpc(this@generateDoc)

        append(functionAttributeList.generateDoc())

        part("native", asKeyword)
        colorize(name ?: "", asNativeFunction)

        append(parameters.generateDoc())
        if (returnType != null) {
            append(": ")
            appendNotNull(returnType.generateDoc())
        }

        append(DocumentationMarkup.DEFINITION_END)
        generateCommentsPart(this@generateDoc)
    }
}

private fun List<TactFunctionAttribute>.generateDoc(): String {
    return buildString {
        this@generateDoc.forEach {
            part(it.text, asKeyword)
        }
    }
}

private fun TactResult.generateDoc(): String {
    val type = type.toEx()
    return type.generateDoc(this)
}

private fun TactParameters.generateDoc(): String {
    val params = paramDefinitionList

    if (params.isEmpty()) {
        return buildString { colorize("()", asParen) }
    }

    if (params.size == 1) {
        val param = params.first()
        return buildString {
            colorize("(", asParen)
            append(param.generateDocForMethod())
            colorize(")", asParen)
        }
    }

    val paramNameMaxWidth = params.maxOfOrNull { it.name?.length ?: 0 } ?: 0

    return buildString {
        colorize("(", asParen)
        append("\n")
        append(
            params.joinToString(",\n") { param ->
                buildString {
                    append("   ")

                    val name = param.name
                    if (name != null) {
                        if (name == "self") {
                            colorize(name, asKeyword)
                        } else {
                            colorize(name, asParameter)
                        }
                    }
                    append(": ")
                    val nameLength = name?.length ?: 0
                    append("".padEnd(max(paramNameMaxWidth - nameLength, 0)))
                    append(param.type.toEx().generateDoc(this@generateDoc))
                }
            } + ","
        )
        append("\n")
        colorize(")", asParen)
    }
}

private fun TactParamDefinition.generateDocForMethod(): String {
    return buildString {
        val name = name
        if (name != null) {
            if (name == "self") {
                colorize(name, asKeyword)
            } else {
                colorize(name, asParameter)
            }
            append(": ")
        }
        append(type.toEx().generateDoc(this@generateDocForMethod))
    }
}

private fun TactAttributes.generateDoc(): String {
    return attributeList.joinToString("\n") { attr ->
        attr.generateDoc()
    }
}

private fun generateDocForConstantModifiers(attrs: List<TactConstantModifier>): String {
    return attrs.joinToString(" ") { it.text }
}

private fun TactAttribute.generateDoc(): String {
    val plainAttribute = attributeExpression?.plainAttribute ?: return ""
    val name = plainAttribute.attributeKey.text ?: ""
    val arguments = plainAttribute.argumentList?.expressionList ?: emptyList()
    return buildString {
        colorize("@", asAttribute)
        colorize(name, asAttribute)
        if (arguments.isNotEmpty()) {
            colorize("(", asParen)
            arguments.forEachIndexed { index, argument ->
                append(argument.generateDoc())
                if (index != arguments.size - 1) {
                    append(", ")
                }
            }
            colorize(")", asParen)
        }
    }
}

fun TactStructDeclaration.generateDoc(): String {
    return buildString {
        append(DocumentationMarkup.DEFINITION_START)

        part("struct", asKeyword)
        colorize(name, asStruct)

        val fields = structType.fieldDeclarationList

        generateFields(fields)

        append(DocumentationMarkup.DEFINITION_END)
        generateCommentsPart(this@generateDoc)
    }
}

fun TactMessageDeclaration.generateDoc(): String {
    return buildString {
        append(DocumentationMarkup.DEFINITION_START)

        colorize("message", asKeyword)

        val messageId = messageType.messageId
        if (messageId != null) {
            colorize("(", asParen)
            append(messageId.expression?.generateDoc())
            colorize(")", asParen)
        }

        append(" ")
        colorize(name, asStruct)

        val fields = messageType.fieldDeclarationList

        generateFields(fields)

        append(DocumentationMarkup.DEFINITION_END)
        generateCommentsPart(this@generateDoc)
    }
}

private fun StringBuilder.generateFields(fields: List<TactFieldDeclaration>) {
    if (fields.isEmpty()) {
        colorize(" {}", asBraces)
        return
    }

    colorize(" {", asBraces)
    appendLine()
    append(
        fields.joinToString("\n") { field ->
            val def = field.fieldDefinition
            buildString {
                append("   ")
                colorize(def.name ?: "", asField)
                append(": ")
                append(def.getType(null)?.generateDoc(field) ?: "<unknown>")

                if (field.defaultFieldValue != null) {
                    append(" = ")
                    append(field.defaultFieldValue?.expression?.generateDoc())
                }

                append(";")
            }
        }
    )
    append("\n")
    colorize("}", asBraces)
}

fun TactFieldDefinition.generateDoc(): String {
    return buildString {
        append(DocumentationMarkup.DEFINITION_START)
        val parent = parent as? TactFieldDeclaration ?: return@buildString
        val type = parent.type

        generateOwnerDpc(this@generateDoc)

        colorize(name ?: "", asField)
        append(": ")
        append(type.toEx().generateDoc(this@generateDoc))

        val valueDoc = parent.defaultFieldValue?.expression?.generateDoc()
        if (valueDoc != null) {
            part(" =")
            append(valueDoc)
        }

        append(DocumentationMarkup.DEFINITION_END)
        generateCommentsPart(this@generateDoc)
    }
}

fun TactContractDeclaration.generateDoc(): String {
    return buildString {
        append(DocumentationMarkup.DEFINITION_START)

        part("contract", asKeyword)
        part(name, asContract)

        val inheritedTraits = contractType.getWithClause()?.typeList ?: emptyList()
        formatInheritTraits(inheritedTraits)

        append(DocumentationMarkup.DEFINITION_END)
        generateCommentsPart(this@generateDoc)
    }
}

fun TactTraitDeclaration.generateDoc(): String {
    return buildString {
        append(DocumentationMarkup.DEFINITION_START)

        part("trait", asKeyword)
        part(name, asTrait)

        val inheritedTraits = traitType.getWithClause()?.typeList ?: emptyList()
        formatInheritTraits(inheritedTraits)

        append(DocumentationMarkup.DEFINITION_END)
        generateCommentsPart(this@generateDoc)
    }
}

private fun StringBuilder.formatInheritTraits(inheritedTraits: List<TactType>) {
    val filtered = inheritedTraits.filter { it.text != "BaseTrait" }
    if (filtered.isEmpty()) return

    colorize("with ", asKeyword)
    filtered.forEachIndexed { index, trait ->
        colorize(trait.text, asTrait)
        if (index != filtered.size - 1) {
            append(", ")
        }
    }
}

fun TactPrimitiveDeclaration.generateDoc(): String {
    return buildString {
        append(DocumentationMarkup.DEFINITION_START)

        part("primitive", asKeyword)
        colorize(name, asPrimitive)

        append(DocumentationMarkup.DEFINITION_END)
        generateCommentsPart(this@generateDoc)
    }
}

fun TactConstDefinition.generateDoc(): String {
    return buildString {
        append(DocumentationMarkup.DEFINITION_START)

        val modifiers = (parent as? TactConstDeclaration)?.constantModifierList ?: emptyList()

        part(generateDocForConstantModifiers(modifiers))
        part("const", asKeyword)
        colorize(name, asConst)

        val type = getType(null)
        append(": ")
        append(type?.generateDoc(this@generateDoc))

        if (expression != null) {
            append(" = ")
            val defaultValue = expression
            append(defaultValue?.generateDoc())
        }

        append(DocumentationMarkup.DEFINITION_END)
        generateCommentsPart(this@generateDoc)
    }
}

fun TactVarDefinition.generateDoc(): String {
    return buildString {
        append(DocumentationMarkup.DEFINITION_START)
        val type = getType(null)

        part("let", asKeyword)
        colorize(name, asIdentifier)
        append(": ")
        append(type?.generateDoc(this@generateDoc) ?: "unknown")
        append(DocumentationMarkup.DEFINITION_END)

        generateCommentsPart(this@generateDoc)
    }
}

fun TactParamDefinition.generateDoc(): String {
    return buildString {
        append(DocumentationMarkup.DEFINITION_START)
        val type = getType(null)

        part("parameter", asKeyword)
        colorize(name ?: "", asParameter)
        append(": ")
        append(type?.generateDoc(this@generateDoc) ?: "unknown")
        append(DocumentationMarkup.DEFINITION_END)

        generateCommentsPart(this@generateDoc)
    }
}

fun formatOperands(operands: Map<String, Any?>): String {
    return operands.values.joinToString(" ") { it.toString() }
}

fun TactAsmInstruction.generateDoc(): String {
    val expr = parent as? TactAsmExpression ?: return "unknown instruction"
    val instr = expr.asmInstruction
    val arguments = expr.asmArguments.asmPrimitiveList

    val info = findInstruction(instr.identifier.text, arguments) ?: return "unknown instruction"

    val stackInfo = "<li>Stack (top is on the right): <code>${getStackPresentation(info.doc.stack)}</code></li>"

    val gas = info.doc.gas.ifEmpty { "unknown" }

    val actualInstructionDescription = mutableListOf(
        wrapDefinition(colorize(info.mnemonic, asAsmInstruction)),
        DocumentationMarkup.CONTENT_START,
        "<ul>",
        stackInfo,
        "<li>Gas: <code>$gas</code></li>",
        "</ul>",
        documentationAsHtml(info.doc.description, null, TactDocRenderMode.QUICK_DOC_POPUP, this),
        DocumentationMarkup.CONTENT_END,
    )

    val alias = info.aliasInfo
    if (alias != null) {
        val operandsStr = formatOperands(alias.operands) + " "
        val aliasInfoDescription = " (alias of $operandsStr${alias.aliasOf})"

        val aliasStackInfo = alias.docStack?.let {
            "<li>Stack (top is on the right): <code>${getStackPresentation(it)}</code></li>"
        } ?: ""

        val withAliasDescription = listOf(
            wrapDefinition(colorize(alias.mnemonic, asAsmInstruction) + colorize(aliasInfoDescription, asComment)),
            DocumentationMarkup.CONTENT_START,
            "<ul>",
            aliasStackInfo,
            "</ul>",
            documentationAsHtml(alias.description ?: "", null, TactDocRenderMode.QUICK_DOC_POPUP, this),
            DocumentationMarkup.CONTENT_END,
            "<hr>",
            "Aliased instruction info:",
            "<br>",
        ) + actualInstructionDescription
        return withAliasDescription.joinToString("\n")
    }

    return actualInstructionDescription.joinToString("\n")
}

fun wrapDefinition(content: String): String = DocumentationMarkup.DEFINITION_START + content + DocumentationMarkup.DEFINITION_END

fun TactExpression.generateDoc(): String {
    val text = text
    val highlighter = TactSyntaxHighlighter()
    val lexer = highlighter.highlightingLexer
    val builder = StringBuilder()
    lexer.start(text)
    while (lexer.tokenType != null) {
        val type = lexer.tokenType
        val tokenText = lexer.tokenText
        val keyword = TactTokenTypes.KEYWORDS.contains(type)
        val number = TactTokenTypes.NUMBERS.contains(type)
        val string = TactTokenTypes.STRING_LITERALS.contains(type)
        val operators = TactTokenTypes.OPERATORS.contains(type)
        val booleanLiteral = tokenText == "true" || tokenText == "false"
        val primitiveType = TactPrimitiveTypes.isPrimitiveType(tokenText)

        if (tokenText.contains("\n")) {
            builder.append("...")
            break
        }

        builder.append(
            when {
                keyword        -> colorize(tokenText, asKeyword)
                number         -> colorize(tokenText, asNumber)
                string         -> colorize(tokenText, asString)
                operators      -> colorize(tokenText, asOperator)
                booleanLiteral -> colorize(tokenText, asKeyword)
                primitiveType  -> colorize(tokenText, asBuiltin)
                else           -> tokenText
            }
        )
        lexer.advance()
    }

    return builder.toString()
}

fun TactTypeEx.generateDoc(anchor: PsiElement): String {
    when (this) {
        is TactMapTypeEx       -> return this.generateDoc(anchor)
        is TactOptionTypeEx    -> return this.generateDoc(anchor)
        is TactStructTypeEx    -> return this.generateDoc(anchor)
        is TactMessageTypeEx   -> return this.generateDoc(anchor)
        is TactTraitTypeEx     -> return this.generateDoc(anchor)
        is TactFunctionTypeEx  -> return this.generateDoc(anchor)
        is TactTupleTypeEx     -> return this.generateDoc(anchor)
        is TactPrimitiveTypeEx -> return this.generateDoc(anchor)
    }
    return colorize(this.readableName(anchor).escapeHTML(), asType)
}

fun TactStructTypeEx.generateDoc(anchor: PsiElement): String {
    return buildString {
        append(generateFqnTypeDoc(readableName(anchor), asStruct))
    }
}

fun TactTraitTypeEx.generateDoc(anchor: PsiElement): String {
    return buildString {
        append(generateFqnTypeDoc(readableName(anchor), asTrait))
    }
}

fun TactMessageTypeEx.generateDoc(anchor: PsiElement): String {
    return buildString {
        append(generateFqnTypeDoc(readableName(anchor), asMessage))
    }
}

fun TactMapTypeEx.generateDoc(anchor: PsiElement): String {
    return buildString {
        colorize("map", asKeyword)
        append("<")
        appendNotNull(key.generateDoc(anchor))
        append(", ")
        appendNotNull(value.generateDoc(anchor))
        append(">")
    }
}

fun TactOptionTypeEx.generateDoc(anchor: PsiElement): String {
    return buildString {
        appendNotNull(inner.generateDoc(anchor))
        append("?")
    }
}

fun TactFunctionTypeEx.generateDoc(anchor: PsiElement): String {
    return buildString {
        colorize("fun", asKeyword)
        append(" (")
        params.forEachIndexed { index, param ->
            if (index > 0) {
                append(", ")
            }
            appendNotNull(param.generateDoc(anchor))
        }
        colorize(")", asParen)
        if (result != null) {
            append(": ")
            appendNotNull(result.generateDoc(anchor))
        }
    }
}

fun TactTupleTypeEx.generateDoc(anchor: PsiElement): String {
    return buildString {
        colorize("(", asParen)
        types.forEachIndexed { index, param ->
            if (index > 0) {
                append(", ")
            }
            appendNotNull(param.generateDoc(anchor))
        }
        colorize(")", asParen)
    }
}

fun TactPrimitiveTypeEx.generateDoc(anchor: PsiElement): String {
    val name = readableName(anchor)
    val tlb = tlbType
    if (tlb != null) {
        return buildString {
            colorize(name, asPrimitive)
            colorize(" as ", asKeyword)
            colorize(tlb, asPrimitive)
        }
    }
    return colorize(name, asPrimitive)
}

private fun generateFqnTypeDoc(fqn: String, color: TextAttributes): String {
    val parts = fqn.split(".")
    if (parts.size == 1) {
        return colorize(parts[0], color)
    }

    return parts.subList(0, parts.size - 1).joinToString(".") {
        it
    } + "." + colorize(parts.last(), color)
}

fun StringBuilder.generateCommentsPart(element: PsiElement?) {
    val commentsList = CommentsConverter.getCommentsForElement(element)
    if (commentsList.any { it is TactDocComment }) {
        append(DocumentationMarkup.CONTENT_START)
        for (comment in commentsList) {
            if (comment is TactDocComment) {
                append(comment.documentationAsHtml())
                append("\n")
            }
        }
        append(DocumentationMarkup.CONTENT_END)
        return
    }

    val comments = CommentsConverter.toHtml(commentsList)
    if (comments.isNotEmpty()) {
        append(DocumentationMarkup.CONTENT_START)
        append(comments)
        append(DocumentationMarkup.CONTENT_END)
    }
}
