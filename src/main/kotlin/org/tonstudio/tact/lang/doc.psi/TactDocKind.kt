package org.tonstudio.tact.lang.doc.psi

import com.intellij.psi.tree.IElementType
import org.tonstudio.tact.lang.psi.TactDocElementTypes.DOC_COMMENT
import kotlin.math.min

enum class TactDocKind {
    Eol {
        override val infix: String = "///"
        override val prefix: String = infix

        override fun removeDecoration(lines: Sequence<TactDocLine>): Sequence<TactDocLine> =
            removeEolDecoration(lines)
    };

    abstract val prefix: String
    abstract val infix: String

    /**
     * Removes doc comment decoration from a sequence of token's lines.
     *
     * This method expects non-empty line sequences of valid doc comment tokens.
     * It does **not** perform any validation!
     */
    protected abstract fun removeDecoration(lines: Sequence<TactDocLine>): Sequence<TactDocLine>

    fun removeDecorationToLines(text: CharSequence): Sequence<TactDocLine> =
        removeDecoration(TactDocLine.splitLines(text))

    fun removeDecoration(text: CharSequence): Sequence<CharSequence> =
        removeDecorationToLines(text).mapNotNull { if (it.isRemoved) null else it.content }

    protected fun removeEolDecoration(decoratedLines: Sequence<TactDocLine>, infix: String = this.infix): Sequence<TactDocLine> {
        return removeCommonIndent(decoratedLines.map { it.trimStart().removePrefix(infix) })
    }

    protected fun removeCommonIndent(decoratedLines: Sequence<TactDocLine>): Sequence<TactDocLine> {
        val lines = decoratedLines.toList()

        val minIndent = lines.fold(Int.MAX_VALUE) { minIndent, line ->
            if (line.isRemoved || line.content.isBlank()) {
                minIndent
            } else {
                min(minIndent, line.countStartWhitespace())
            }
        }

        return lines.asSequence().map { line ->
            line.substring(min(minIndent, line.contentLength))
        }
    }


    companion object {
        /**
         * Get [TactDocKind] of given doc comment token [IElementType].
         *
         * @throws IllegalArgumentException when given token type is unsupported
         */
        fun of(tokenType: IElementType): TactDocKind = when (tokenType) {
            DOC_COMMENT -> Eol
            else        -> throw IllegalArgumentException("unsupported token type")
        }
    }
}
