package org.tonstudio.tact.lang

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.spellchecker.inspections.PlainTextSplitter
import com.intellij.spellchecker.tokenizer.SpellcheckingStrategy
import com.intellij.spellchecker.tokenizer.TokenConsumer
import com.intellij.spellchecker.tokenizer.Tokenizer
import org.tonstudio.tact.lang.psi.TactStringLiteral

class TactSpellcheckingStrategy : SpellcheckingStrategy() {
    override fun isMyContext(element: PsiElement): Boolean = element.language == TactLanguage

    override fun getTokenizer(element: PsiElement?): Tokenizer<*> {
        if (element is TactStringLiteral) {
            return TactStringLiteralTokenizer
        }
        return super.getTokenizer(element)
    }

    object TactStringLiteralTokenizer : Tokenizer<TactStringLiteral>() {
        override fun tokenize(element: TactStringLiteral, consumer: TokenConsumer) {
            val text = element.contents
            consumer.consumeToken(element, text, true, 1, TextRange.allOf(text), PlainTextSplitter.getInstance())
        }
    }
}
