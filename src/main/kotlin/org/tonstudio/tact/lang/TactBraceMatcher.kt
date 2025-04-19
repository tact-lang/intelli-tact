package org.tonstudio.tact.lang

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import org.tonstudio.tact.lang.psi.TactTokenTypes

class TactBraceMatcher : PairedBraceMatcher {
    override fun getPairs() = arrayOf(
        BracePair(TactTypes.LBRACE, TactTypes.RBRACE, true),
        BracePair(TactTypes.LBRACK, TactTypes.RBRACK, false),
        BracePair(TactTypes.LPAREN, TactTypes.RPAREN, false),
    )

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, type: IElementType?) =
        TactTokenTypes.COMMENTS.contains(type) ||
                TactTokenTypes.WHITE_SPACES.contains(type) ||
                type === TactTypes.SEMICOLON ||
                type === TactTypes.COMMA ||
                type === TactTypes.RPAREN ||
                type === TactTypes.RBRACK ||
                type === TactTypes.RBRACE ||
                type === TactTypes.LBRACE

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int) = openingBraceOffset
}
