package org.tonstudio.tact.ide.injections

import com.intellij.lang.injection.MultiHostInjector
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.TokenType.WHITE_SPACE
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.intellij.util.containers.withPrevious
import com.intellij.util.text.CharArrayUtil
import org.tonstudio.tact.lang.TactLanguage
import org.tonstudio.tact.lang.doc.psi.TactDocCodeFence
import org.tonstudio.tact.lang.doc.psi.TactDocGap
import org.tonstudio.tact.lang.psi.TactDocElementTypes.DOC_DATA
import java.util.regex.Pattern

class TactDoctestLanguageInjector : MultiHostInjector {
    override fun elementsToInjectIn(): List<Class<out PsiElement>> =
        listOf(TactDocCodeFence::class.java)

    override fun getLanguagesToInject(registrar: MultiHostRegistrar, context: PsiElement) {
        if (context !is TactDocCodeFence || !context.isValidHost) return

        val info = context.doctestInfo() ?: return
        val ranges = info.rangesForInjection.map { TextRange(it.startOffset, it.endOffset) }
        if (ranges.isEmpty()) return

        val inj = registrar.startInjecting(TactLanguage)
        ranges.forEachIndexed { _, range ->
            inj.addPlace("", null, context, range)
        }

        inj.doneInjecting()
    }
}

private val LANG_SPLIT_REGEX = Pattern.compile("[^\\w-]+", Pattern.UNICODE_CHARACTER_CLASS)
private val TACT_LANG_ALIASES = listOf("tact")

/**
 * ~~~
 * ///.foo
 * ///.---```
 * ///.---let a = 1;
 * ///
 * ///.---let b = a;
 * ///.---```
 * ~~~
 *
 * In this snippet, a dot (.) shows [docIndent] and a `---` shows [fenceIndent].
 * [rangesForInjection] include only `let a = 1;\n` and `let b = a;\n`, and
 * [rangesForBackgroundHighlighting] include `.---let a = 1;\n`, `\n` and `.---let b = a;`
 */
class DoctestInfo private constructor(
    private val docIndent: Int,
    private val fenceIndent: Int,
    private val contents: List<Content>,
    val text: String,
) {
    val rangesForInjection: List<TextRange>
        get() = contents.mapNotNull { c ->
            val psi = (c as? Content.DocData)?.psi ?: return@mapNotNull null
            val range = psi.textRangeInParent
            val add = if (range.endOffset < text.length) 1 else 0
            val startOffset = CharArrayUtil.shiftForward(text, range.startOffset, range.startOffset + fenceIndent, " \t")
            if (startOffset < range.endOffset) TextRange(startOffset, range.endOffset + add) else null
        }

    private val rangesForBackgroundHighlighting: List<TextRange>
        get() = contents.map { c ->
            when (c) {
                is Content.DocData   -> {
                    val range = c.psi.textRangeInParent
                    val add = if (range.endOffset < text.length) 1 else 0
                    TextRange(range.startOffset - docIndent, range.endOffset + add)
                }

                is Content.EmptyLine -> c.range
            }
        }

    private sealed class Content {
        class DocData(val psi: PsiElement) : Content()
        class EmptyLine(val range: TextRange) : Content()
    }

    companion object {
        fun fromCodeFence(codeFence: TactDocCodeFence): DoctestInfo? {
            val lang = codeFence.lang?.text ?: ""
            val parts = lang.split(LANG_SPLIT_REGEX).filter { it.isNotBlank() }
            if (parts.any { it !in TACT_LANG_ALIASES }) return null

            val start = codeFence.start
            val fenceIndent = start.text.indexOfFirst { it == '`' || it == '~' }
            val prevLeaf = PsiTreeUtil.prevLeaf(codeFence)
            val docIndent = if (prevLeaf is PsiWhiteSpace && PsiTreeUtil.prevLeaf(prevLeaf) is TactDocGap) {
                prevLeaf.textLength
            } else {
                0
            }

            var isAfterNewLine = false
            val contents = mutableListOf<Content>()

            for (element in codeFence.childrenWithLeaves) {
                when (element.elementType) {
                    DOC_DATA    -> {
                        isAfterNewLine = false
                        contents += Content.DocData(element)
                    }

                    WHITE_SPACE -> {
                        for ((index, prevIndex) in element.text.indicesOf("\n").withPrevious()) {
                            if (isAfterNewLine) {
                                val startOffset = if (prevIndex != null) prevIndex + 1 else 0
                                val endOffset = index + 1
                                val range = TextRange(startOffset, endOffset).shiftRight(element.startOffsetInParent)
                                contents += Content.EmptyLine(range)
                            }
                            isAfterNewLine = true
                        }
                    }
                }
            }

            return DoctestInfo(docIndent, fenceIndent, contents, codeFence.text)
        }
    }
}

private fun String.indicesOf(s: String): Sequence<Int> =
    generateSequence(indexOf(s)) { indexOf(s, it + s.length) }.takeWhile { it != -1 }

fun TactDocCodeFence.doctestInfo(): DoctestInfo? =
    DoctestInfo.fromCodeFence(this)

val PsiElement.childrenWithLeaves: Sequence<PsiElement>
    get() = generateSequence(this.firstChild) { it.nextSibling }
