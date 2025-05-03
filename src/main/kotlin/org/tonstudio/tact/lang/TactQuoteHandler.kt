package org.tonstudio.tact.lang

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler
import com.intellij.openapi.editor.highlighter.HighlighterIterator

class TactQuoteHandler : SimpleTokenSetQuoteHandler(
    TactTypes.OPEN_QUOTE,
    TactTypes.CLOSING_QUOTE,
) {
    override fun isClosingQuote(iterator: HighlighterIterator, offset: Int): Boolean {
        return iterator.tokenType == TactTypes.CLOSING_QUOTE
    }

    override fun isOpeningQuote(iterator: HighlighterIterator, offset: Int): Boolean {
        return iterator.tokenType == TactTypes.OPEN_QUOTE
    }

    override fun isNonClosedLiteral(iterator: HighlighterIterator?, chars: CharSequence?): Boolean {
        return super.isNonClosedLiteral(iterator, chars)
    }
}
