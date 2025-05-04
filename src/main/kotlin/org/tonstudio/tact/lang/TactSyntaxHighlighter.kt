package org.tonstudio.tact.lang

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import org.tonstudio.tact.ide.colors.TactColor
import org.tonstudio.tact.lang.TactTypes.*
import org.tonstudio.tact.lang.psi.TactDocElementTypes.DOC_COMMENT
import org.tonstudio.tact.lang.psi.TactTokenTypes.BOOL_LITERALS
import org.tonstudio.tact.lang.psi.TactTokenTypes.COMMENTS
import org.tonstudio.tact.lang.psi.TactTokenTypes.KEYWORDS
import org.tonstudio.tact.lang.psi.TactTokenTypes.LINE_COMMENT
import org.tonstudio.tact.lang.psi.TactTokenTypes.NUMBERS
import org.tonstudio.tact.lang.psi.TactTokenTypes.OPERATORS
import org.tonstudio.tact.lang.psi.TactTokenTypes.STRING_LITERALS

class TactSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer() = TactHighlightingLexer()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> =
        pack(map(tokenType)?.textAttributesKey)
}

fun map(tokenType: IElementType): TactColor? = when (tokenType) {
    DOC_COMMENT          -> TactColor.LINE_COMMENT
    LINE_COMMENT         -> TactColor.LINE_COMMENT

    LPAREN, RPAREN       -> TactColor.PARENTHESES
    LBRACE, RBRACE       -> TactColor.BRACES
    LBRACK, RBRACK       -> TactColor.BRACKETS
    ASSERT_NOT_NULL_EXPR -> TactColor.NOT_NULL_OPERATOR

    STRING_ESCAPE_ENTRY  -> TactColor.VALID_STRING_ESCAPE
    DOT                  -> TactColor.DOT
    COMMA                -> TactColor.COMMA

    in KEYWORDS          -> TactColor.KEYWORD
    in BOOL_LITERALS     -> TactColor.KEYWORD
    in STRING_LITERALS   -> TactColor.STRING
    in NUMBERS           -> TactColor.NUMBER
    in OPERATORS         -> TactColor.OPERATOR
    in COMMENTS          -> TactColor.LINE_COMMENT

    else                 -> null
}
