package org.tonstudio.tact.lang.doc.psi

import com.intellij.psi.PsiComment
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.psi.impl.source.tree.injected.InjectionBackgroundSuppressor
import org.tonstudio.tact.lang.psi.TactCompositeElement

interface TactDocElement : TactCompositeElement {
    val containingDoc: TactDocComment

    val markdownValue: String
}

/**
 * A skipped `///`, or `*` (or other kind of documentation comment decorations)
 * is treated as a comment leaf in the Markdown tree
 */
interface TactDocGap : PsiComment

/**
 * [TactDocAtxHeading] or [TactDocSetextHeading]
 */
interface TactDocHeading : TactDocElement

/**
 * A [markdown ATX headings](https://spec.commonmark.org/0.29/#atx-heading)
 * ```
 * /// # Header 1
 * /// ## Header 2
 * /// ### Header 3
 * /// #### Header 4
 * /// ##### Header 5
 * /// ###### Header 6
 * ```
 */
interface TactDocAtxHeading : TactDocHeading

/**
 * A [markdown Setext headings](https://spec.commonmark.org/0.29/#setext-heading)
 * ```
 * /// Header 1
 * /// ========
 * ///
 * /// Header 2
 * /// --------
 * ```
 */
interface TactDocSetextHeading : TactDocHeading

/** *an emphasis span* or _an emphasis span_ */
interface TactDocEmphasis : TactDocElement

/** **a strong span** or __a strong span__ */
interface TactDocStrong : TactDocElement

/** `a code span` */
interface TactDocCodeSpan : TactDocElement

/** <http://example.com> */
interface TactDocAutoLink : TactDocElement

interface TactDocLink : TactDocElement

/**
 * ```
 * /// [link text](link_destination)
 * /// [link text](link_destination "link title")
 * ```
 */
interface TactDocInlineLink : TactDocLink {
    val linkText: TactDocLinkText
    val linkDestination: TactDocLinkDestination
}

/**
 * ```
 * /// [link label]
 * ```
 *
 * Then, the link should be defined with [TactDocLinkDefinition]
 */
interface TactDocLinkReferenceShort : TactDocLink {
    val linkLabel: TactDocLinkLabel
}

/**
 * ```
 * /// [link text][link label]
 * ```
 *
 * Then, the link should be defined with [TactDocLinkDefinition] (identified by [linkLabel])
 */
interface TactDocLinkReferenceFull : TactDocLink {
    val linkText: TactDocLinkText
    val linkLabel: TactDocLinkLabel
}

/**
 * ```
 * /// [link label]: link_destination
 * ```
 */
interface TactDocLinkDefinition : TactDocLink {
    val linkLabel: TactDocLinkLabel
    val linkDestination: TactDocLinkDestination
}

/**
 * A `[LINK TEXT]` part of such links:
 * ```
 * /// [LINK TEXT](link_destination)
 * /// [LINK TEXT][link label]
 * ```
 * Includes brackets (`[`, `]`).
 * A child of [TactDocInlineLink] or [TactDocLinkReferenceFull]
 */
interface TactDocLinkText : TactDocElement

/**
 * A `[LINK LABEL]` part in these contexts:
 * ```
 * /// [LINK LABEL]
 * /// [link text][LINK LABEL]
 * /// [LINK LABEL]: link_destination
 * ```
 *
 * A link label is used to match *a link reference* with *a link definition*.
 *
 * A child of [TactDocLinkReferenceShort], [TactDocLinkReferenceFull] or [TactDocLinkDefinition]
 */
interface TactDocLinkLabel : TactDocElement

/**
 * A `LINK TITLE` (with quotes and parentheses) part in these contexts:
 * ```
 * /// [inline link](http://example.com "LINK TITLE")
 * /// [inline link](http://example.com 'LINK TITLE')
 * /// [inline link](http://example.com (LINK TITLE))
 * ```
 *
 * A child of [TactDocInlineLink]
 */
interface TactDocLinkTitle : TactDocElement

/**
 * A `LINK DESTINATION` part in these contexts:
 * ```
 * /// [link text](LINK DESTINATION)
 * /// [link label]: LINK DESTINATION
 * ```
 *
 * A child of [TactDocInlineLink] or [TactDocLinkDefinition]
 */
interface TactDocLinkDestination : TactDocElement

/**
 * A [Markdown code fence](https://spec.commonmark.org/0.29/#fenced-code-blocks).
 *
 * Provides specific behavior for language injections (see [TactDoctestLanguageInjector]).
 *
 * [InjectionBackgroundSuppressor] is used to disable builtin background highlighting for injection.
 * We create such background manually by [TactDoctestAnnotator] (see the class docs)
 */
interface TactDocCodeFence : TactDocElement, PsiLanguageInjectionHost, InjectionBackgroundSuppressor {
    val start: TactDocCodeFenceStartEnd
    val end: TactDocCodeFenceStartEnd?
    val lang: TactDocCodeFenceLang?
}

// TODO should be `PsiLanguageInjectionHost` too
interface TactDocCodeBlock : TactDocElement

/**
 * See [markdown HTML blocks](https://spec.commonmark.org/0.29/#html-blocks)
 */
interface TactDocHtmlBlock : TactDocElement

/**
 * Leading and trailing backtick or tilda sequences of [TactDocCodeFence].
 *
 * `````
 * /// ```
 * ///  ^ this
 * /// ```
 *      ^ and this
 * `````
 */
interface TactDocCodeFenceStartEnd : TactDocElement

/**
 * A child of [TactDocCodeFence].
 *
 * `````
 * /// ```spawn
 *        ^^^^^ this text
 * `````
 */
interface TactDocCodeFenceLang : TactDocElement
