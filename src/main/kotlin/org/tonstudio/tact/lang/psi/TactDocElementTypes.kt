package org.tonstudio.tact.lang.psi

import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.tonstudio.tact.lang.doc.psi.TactDocCommentElementType
import org.tonstudio.tact.lang.doc.psi.TactDocCompositeTokenType
import org.tonstudio.tact.lang.doc.psi.TactDocTokenType
import org.tonstudio.tact.lang.doc.psi.impl.*

@Suppress("MemberVisibilityCanBePrivate")
object TactDocElementTypes {
    @JvmField val DOC_COMMENT = TactDocCommentElementType()

    val DOC_DATA = TactDocTokenType("<DOC_DATA>")
    val DOC_GAP = TactDocTokenType("<DOC_GAP>")

    val DOC_ATX_HEADING = TactDocCompositeTokenType("<DOC_ATX_HEADING>", ::TactDocAtxHeadingImpl)
    val DOC_SETEXT_HEADING = TactDocCompositeTokenType("<DOC_SETEXT_HEADING>", ::TactDocSetextHeadingImpl)

    val DOC_EMPHASIS = TactDocCompositeTokenType("<DOC_EMPHASIS>", ::TactDocEmphasisImpl)
    val DOC_STRONG = TactDocCompositeTokenType("<DOC_STRONG>", ::TactDocStrongImpl)
    val DOC_CODE_SPAN = TactDocCompositeTokenType("<DOC_CODE_SPAN>", ::TactDocCodeSpanImpl)

    val DOC_AUTO_LINK = TactDocCompositeTokenType("<DOC_AUTO_LINK>", ::TactDocAutoLinkImpl)
    val DOC_INLINE_LINK = TactDocCompositeTokenType("<DOC_INLINE_LINK>", ::TactDocInlineLinkImpl)
    val DOC_SHORT_REFERENCE_LINK = TactDocCompositeTokenType("<DOC_SHORT_REFERENCE_LINK>", ::TactDocLinkReferenceShortImpl)
    val DOC_FULL_REFERENCE_LINK = TactDocCompositeTokenType("<DOC_FULL_REFERENCE_LINK>", ::TactDocLinkReferenceFullImpl)
    val DOC_LINK_DEFINITION = TactDocCompositeTokenType("<DOC_LINK_DEFINITION>", ::TactDocLinkDefinitionImpl)

    val DOC_LINK_TEXT = TactDocCompositeTokenType("<DOC_LINK_TEXT>", ::TactDocLinkTextImpl)
    val DOC_LINK_LABEL = TactDocCompositeTokenType("<DOC_LINK_LABEL>", ::TactDocLinkLabelImpl)
    val DOC_LINK_TITLE = TactDocCompositeTokenType("<DOC_LINK_TITLE>", ::TactDocLinkTitleImpl)
    val DOC_LINK_DESTINATION = TactDocCompositeTokenType("<DOC_LINK_DESTINATION>", ::TactDocLinkDestinationImpl)

    val DOC_CODE_FENCE = TactDocCompositeTokenType("<DOC_CODE_FENCE>", ::TactDocCodeFenceImpl)
    val DOC_CODE_BLOCK = TactDocCompositeTokenType("<DOC_CODE_BLOCK>", ::TactDocCodeBlockImpl)
    val DOC_HTML_BLOCK = TactDocCompositeTokenType("<DOC_HTML_BLOCK>", ::TactDocHtmlBlockImpl)

    val DOC_CODE_FENCE_START_END = TactDocCompositeTokenType("<DOC_CODE_FENCE_START_END>", ::TactDocCodeFenceStartEndImpl)
    val DOC_CODE_FENCE_LANG = TactDocCompositeTokenType("<DOC_CODE_FENCE_LANG>", ::TactDocCodeFenceLangImpl)

    private val MARKDOWN_ATX_HEADINGS = setOf(
        MarkdownElementTypes.ATX_1,
        MarkdownElementTypes.ATX_2,
        MarkdownElementTypes.ATX_3,
        MarkdownElementTypes.ATX_4,
        MarkdownElementTypes.ATX_5,
        MarkdownElementTypes.ATX_6
    )

    /**
     * Some Markdown nodes are skipped (like [MarkdownElementTypes.PARAGRAPH]) because they are mostly useless
     * for Tact and just increase Markdown tree depth. We're trying to keep the tree as simple as possible.
     */
    fun mapMarkdownToTact(type: org.intellij.markdown.IElementType): TactDocCompositeTokenType? {
        return when (type) {
            in MARKDOWN_ATX_HEADINGS                                               -> DOC_ATX_HEADING
            MarkdownElementTypes.SETEXT_1, MarkdownElementTypes.SETEXT_2           -> DOC_SETEXT_HEADING
            MarkdownElementTypes.EMPH                                              -> DOC_EMPHASIS
            MarkdownElementTypes.STRONG                                            -> DOC_STRONG
            MarkdownElementTypes.CODE_SPAN                                         -> DOC_CODE_SPAN
            MarkdownElementTypes.AUTOLINK                                          -> DOC_AUTO_LINK
            MarkdownElementTypes.INLINE_LINK                                       -> DOC_INLINE_LINK
            MarkdownElementTypes.SHORT_REFERENCE_LINK                              -> DOC_SHORT_REFERENCE_LINK
            MarkdownElementTypes.FULL_REFERENCE_LINK                               -> DOC_FULL_REFERENCE_LINK
            MarkdownElementTypes.LINK_DEFINITION                                   -> DOC_LINK_DEFINITION
            MarkdownElementTypes.LINK_TEXT                                         -> DOC_LINK_TEXT
            MarkdownElementTypes.LINK_LABEL                                        -> DOC_LINK_LABEL
            MarkdownElementTypes.LINK_TITLE                                        -> DOC_LINK_TITLE
            MarkdownElementTypes.LINK_DESTINATION                                  -> DOC_LINK_DESTINATION
            MarkdownElementTypes.CODE_FENCE                                        -> DOC_CODE_FENCE
            MarkdownElementTypes.CODE_BLOCK                                        -> DOC_CODE_BLOCK
            MarkdownElementTypes.HTML_BLOCK                                        -> DOC_HTML_BLOCK
            MarkdownTokenTypes.CODE_FENCE_START, MarkdownTokenTypes.CODE_FENCE_END -> DOC_CODE_FENCE_START_END
            MarkdownTokenTypes.FENCE_LANG                                          -> DOC_CODE_FENCE_LANG
            else                                                                   -> null // `null` means that the node is skipped
        }
    }
}
