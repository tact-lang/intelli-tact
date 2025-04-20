package org.tonstudio.tact.lang.doc.psi.impl

import com.intellij.psi.LiteralTextEscaper
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.impl.source.tree.AstBufferUtil
import com.intellij.psi.impl.source.tree.CompositePsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.childrenOfType
import com.intellij.util.text.CharArrayUtil
import org.tonstudio.tact.lang.doc.psi.*
import org.tonstudio.tact.lang.psi.StringLiteralEscaper
import org.tonstudio.tact.lang.psi.impl.TactElementFactory
import org.tonstudio.tact.utils.ancestorStrict
import org.tonstudio.tact.utils.childOfType
import org.tonstudio.tact.utils.getPrevNonWhitespaceSibling

abstract class TactDocElementImpl(type: IElementType) : CompositePsiElement(type), TactDocElement {
    protected open fun <T : Any> notNullChild(child: T?): T =
        child ?: error("$text parent=${parent.text}")

    override val containingDoc: TactDocComment
        get() = ancestorStrict()
            ?: error("TactDocElement cannot leave outside of the doc comment! `${text}`")

    override val markdownValue: String
        get() = AstBufferUtil.getTextSkippingWhitespaceComments(this)

    override fun toString(): String = "${javaClass.simpleName}($elementType)"
}

class TactDocGapImpl(type: IElementType, text: CharSequence) : LeafPsiElement(type, text), TactDocGap {
    override fun getTokenType(): IElementType = elementType
}

class TactDocAtxHeadingImpl(type: IElementType) : TactDocElementImpl(type), TactDocAtxHeading
class TactDocSetextHeadingImpl(type: IElementType) : TactDocElementImpl(type), TactDocSetextHeading

class TactDocEmphasisImpl(type: IElementType) : TactDocElementImpl(type), TactDocEmphasis
class TactDocStrongImpl(type: IElementType) : TactDocElementImpl(type), TactDocStrong
class TactDocCodeSpanImpl(type: IElementType) : TactDocElementImpl(type), TactDocCodeSpan
class TactDocAutoLinkImpl(type: IElementType) : TactDocElementImpl(type), TactDocAutoLink

class TactDocInlineLinkImpl(type: IElementType) : TactDocElementImpl(type), TactDocInlineLink {
    override val linkText: TactDocLinkText
        get() = notNullChild(childOfType())

    override val linkDestination: TactDocLinkDestination
        get() = notNullChild(childOfType())
}

class TactDocLinkReferenceShortImpl(type: IElementType) : TactDocElementImpl(type), TactDocLinkReferenceShort {
    override val linkLabel: TactDocLinkLabel
        get() = notNullChild(childOfType())
}

class TactDocLinkReferenceFullImpl(type: IElementType) : TactDocElementImpl(type), TactDocLinkReferenceFull {
    override val linkText: TactDocLinkText
        get() = notNullChild(childOfType())

    override val linkLabel: TactDocLinkLabel
        get() = notNullChild(childOfType())
}

class TactDocLinkDefinitionImpl(type: IElementType) : TactDocElementImpl(type), TactDocLinkDefinition {
    override val linkLabel: TactDocLinkLabel
        get() = notNullChild(childOfType())

    override val linkDestination: TactDocLinkDestination
        get() = notNullChild(childOfType())
}

class TactDocLinkTextImpl(type: IElementType) : TactDocElementImpl(type), TactDocLinkText
class TactDocLinkLabelImpl(type: IElementType) : TactDocElementImpl(type), TactDocLinkLabel
class TactDocLinkTitleImpl(type: IElementType) : TactDocElementImpl(type), TactDocLinkTitle
class TactDocLinkDestinationImpl(type: IElementType) : TactDocElementImpl(type), TactDocLinkDestination

class TactDocCodeFenceImpl(type: IElementType) : TactDocElementImpl(type), TactDocCodeFence {
    override fun isValidHost(): Boolean = true

    override val start: TactDocCodeFenceStartEnd
        get() = notNullChild(childOfType())

    override val end: TactDocCodeFenceStartEnd?
        get() = childrenOfType<TactDocCodeFenceStartEnd>().getOrNull(1)

    override val lang: TactDocCodeFenceLang?
        get() = childOfType()

    /**
     * Handles changes in PSI injected to the comment (see [org.tonstudio.tact.ide.injections.TactDoctestLanguageInjector]).
     * It is not used on typing. Instead, it's used on direct PSI changes (performed by
     * intentions/quick fixes).
     */
    override fun updateText(text: String): PsiLanguageInjectionHost {
        val prefix = "///"
        val infix = ""

        val prevSibling = getPrevNonWhitespaceSibling()

        val newText = StringBuilder()

        if (prevSibling != null && prevSibling.text != prefix) {
            // `newText` must be parsed in an empty file
            newText.append(prefix)

            // Then add a proper whitespace between the prefix (`/**`) and the first (`*`)
            val prevPrevSibling = prevSibling.prevSibling
            if (prevPrevSibling is PsiWhiteSpace) {
                newText.append(prevPrevSibling.text)
            } else {
                newText.append("\n")
            }
        }

        newText.append(infix)

        // Add a whitespace between `infix` and a code fence start (e.g. between "///" and "```").
        // The whitespace affects markdown escaping, hence markdown parsing
        if (prevSibling != null && prevSibling.nextSibling != this) {
            newText.append(prevSibling.nextSibling.text)
        }

        var prevIndent = ""
        var index = 0
        while (index < text.length) {
            val linebreakIndex = text.indexOf("\n", index)
            if (linebreakIndex == -1) {
                newText.append(text, index, text.length)
                break
            } else {
                val nextLineStart = linebreakIndex + 1
                newText.append(text, index, nextLineStart)
                index = nextLineStart

                val firstNonWhitespace = CharArrayUtil.shiftForward(text, nextLineStart, " \t")
                if (firstNonWhitespace == text.length) continue
                val isStartCorrect = text.startsWith(infix, firstNonWhitespace)
                if (!isStartCorrect) {
                    newText.append(prevIndent)
                    newText.append(infix)
                    newText.append(" ")
                } else {
                    prevIndent = text.substring(nextLineStart, firstNonWhitespace)
                }
            }
        }

        // There are some problems with indentation if we just use replaceWithText(text).
        // copied from PsiCommentManipulator
        val fromText = TactElementFactory.createFileFromText(project, newText.toString())
        val newElement = PsiTreeUtil.findChildOfType(fromText, javaClass, false) ?: error(newText)
        return replace(newElement) as TactDocCodeFenceImpl
    }

    override fun createLiteralTextEscaper(): LiteralTextEscaper<TactDocCodeFenceImpl> =
        StringLiteralEscaper(this)
}

class TactDocCodeBlockImpl(type: IElementType) : TactDocElementImpl(type), TactDocCodeBlock
class TactDocHtmlBlockImpl(type: IElementType) : TactDocElementImpl(type), TactDocHtmlBlock

class TactDocCodeFenceStartEndImpl(type: IElementType) : TactDocElementImpl(type), TactDocCodeFenceStartEnd
class TactDocCodeFenceLangImpl(type: IElementType) : TactDocElementImpl(type), TactDocCodeFenceLang


