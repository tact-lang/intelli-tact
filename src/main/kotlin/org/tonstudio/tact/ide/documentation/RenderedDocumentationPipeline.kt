package org.tonstudio.tact.ide.documentation

import com.intellij.codeEditor.printing.HTMLTextPainter
import com.intellij.codeInsight.documentation.DocumentationManagerProtocol
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.psi.PsiElement
import com.intellij.ui.ColorHexUtil
import com.intellij.ui.ColorUtil
import org.intellij.markdown.IElementType
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.flavours.MarkdownFlavourDescriptor
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.html.*
import org.intellij.markdown.html.entities.EntityConverter
import org.intellij.markdown.parser.LinkMap
import org.intellij.markdown.parser.MarkdownParser
import org.tonstudio.tact.lang.doc.psi.TactDocComment
import org.tonstudio.tact.lang.doc.psi.TactDocKind
import org.tonstudio.tact.lang.psi.TactNamedElement
import java.net.URI

enum class TactDocRenderMode {
    QUICK_DOC_POPUP,
    INLINE_DOC_COMMENT
}

fun TactDocComment.documentationAsHtml(renderMode: TactDocRenderMode = TactDocRenderMode.QUICK_DOC_POPUP): String {
    val documentationText = TactDocKind.of(tokenType)
        .removeDecoration(text)
        .joinToString("\n")

    return documentationAsHtml(documentationText, this, renderMode, this)
}

fun documentationAsHtml(
    documentationText: String,
    context: TactDocComment?,
    renderMode: TactDocRenderMode,
    anchor: PsiElement,
): String {
    val owner = context?.owner
    val path = qualifiedPathFor(owner)

    // We need some host with unique scheme to
    //
    // 1. Make `URI#resolve` work properly somewhere in Markdown to HTML converter implementation
    // 2. Identify relative links to language items from other links
    //
    // We can't use `DocumentationManagerProtocol.PSI_ELEMENT_PROTOCOL` scheme here
    // because it contains `_` and it is an invalid symbol for a URI scheme
    val tmpUriPrefix = "psi://element/"
    val baseURI = try {
        URI.create("$tmpUriPrefix$path")
    } catch (e: Exception) {
        null
    }

    val flavour = TactDocMarkdownFlavourDescriptor(context ?: anchor, baseURI, renderMode)
    val root = MarkdownParser(flavour).buildMarkdownTreeFromString(documentationText)
    return HtmlGenerator(documentationText, root, flavour).generateHtml()
        .replace("psi://element/", DocumentationManagerProtocol.PSI_ELEMENT_PROTOCOL)
}

fun qualifiedPathFor(element: TactNamedElement?): String {
    if (element == null) return ""
    return element.name ?: ""
}

private class TactDocMarkdownFlavourDescriptor(
    private val context: PsiElement,
    private val uri: URI? = null,
    private val renderMode: TactDocRenderMode,
    private val gfm: MarkdownFlavourDescriptor = GFMFlavourDescriptor(useSafeLinks = false, absolutizeAnchorLinks = true),
) : MarkdownFlavourDescriptor by gfm {

    override fun createHtmlGeneratingProviders(linkMap: LinkMap, baseURI: URI?): Map<IElementType, GeneratingProvider> {
        val generatingProviders = HashMap(gfm.createHtmlGeneratingProviders(linkMap, uri ?: baseURI))
        // Filter out MARKDOWN_FILE to avoid producing unnecessary <body> tags
        generatingProviders.remove(MarkdownElementTypes.MARKDOWN_FILE)
        // h1 and h2 are too large
        generatingProviders[MarkdownElementTypes.ATX_1] = SimpleTagProvider("h2")
        generatingProviders[MarkdownElementTypes.ATX_2] = SimpleTagProvider("h3")
        generatingProviders[MarkdownElementTypes.CODE_FENCE] = TactCodeFenceProvider(context, renderMode)

        generatingProviders[MarkdownElementTypes.SHORT_REFERENCE_LINK] =
            TactReferenceLinksGeneratingProvider(linkMap, uri ?: baseURI, resolveAnchors = true)
        generatingProviders[MarkdownElementTypes.FULL_REFERENCE_LINK] =
            TactReferenceLinksGeneratingProvider(linkMap, uri ?: baseURI, resolveAnchors = true)
        generatingProviders[MarkdownElementTypes.INLINE_LINK] =
            TactInlineLinkGeneratingProvider(uri ?: baseURI, resolveAnchors = true)

        return generatingProviders
    }
}

// Inspired by org.intellij.markdown.html.CodeFenceGeneratingProvider
private class TactCodeFenceProvider(
    private val context: PsiElement,
    private val renderMode: TactDocRenderMode,
) : GeneratingProvider {

    override fun processNode(visitor: HtmlGenerator.HtmlGeneratingVisitor, text: String, node: ASTNode) {
        val indentBefore = node.getTextInNode(text).commonPrefixWith(" ".repeat(10)).length

        val codeText = StringBuilder()

        var childrenToConsider = node.children
        if (childrenToConsider.last().type == MarkdownTokenTypes.CODE_FENCE_END) {
            childrenToConsider = childrenToConsider.subList(0, childrenToConsider.size - 1)
        }

        var isContentStarted = false

        loop@ for (child in childrenToConsider) {
            if (isContentStarted && child.type in listOf(MarkdownTokenTypes.CODE_FENCE_CONTENT, MarkdownTokenTypes.EOL)) {
                val rawLine = HtmlGenerator.trimIndents(child.getTextInNode(text), indentBefore)
                codeText.append(rawLine)
            }

            if (!isContentStarted && child.type == MarkdownTokenTypes.EOL) {
                isContentStarted = true
            }
        }

        visitor.consumeHtml(convertToHtmlWithHighlighting(codeText.toString()))
    }

    private fun convertToHtmlWithHighlighting(codeText: String): String {
        var htmlCodeText = HTMLTextPainter.convertCodeFragmentToHTMLFragmentWithInlineStyles(context, codeText)

        // TODO: use scheme of concrete editor instead of global one because they may differ
        val scheme = EditorColorsManager.getInstance().globalScheme
        htmlCodeText = htmlCodeText.replaceFirst(
            "<pre>",
            "<pre style=\"text-indent: ${CODE_SNIPPET_INDENT}px; margin-bottom: -20px;\">"
        )

        return when (renderMode) {
            TactDocRenderMode.INLINE_DOC_COMMENT -> htmlCodeText.dimColors(scheme)
            else                                  -> htmlCodeText
        }
    }

    private fun String.dimColors(scheme: EditorColorsScheme): String {
        val alpha = if (isColorSchemeDark(scheme)) DARK_THEME_ALPHA else LIGHT_THEME_ALPHA

        return replace(COLOR_PATTERN) { result ->
            val colorHexValue = result.groupValues[1]
            val fgColor = ColorHexUtil.fromHexOrNull(colorHexValue) ?: return@replace result.value
            val bgColor = scheme.defaultBackground
            val finalColor = ColorUtil.mix(bgColor, fgColor, alpha)

            "color: #${ColorUtil.toHex(finalColor)}"
        }
    }

    private fun isColorSchemeDark(scheme: EditorColorsScheme): Boolean {
        return ColorUtil.isDark(scheme.defaultBackground)
    }

    companion object {
        private val COLOR_PATTERN = """color:\s*#(\p{XDigit}{3,})""".toRegex()

        private const val CODE_SNIPPET_INDENT = 10
        private const val LIGHT_THEME_ALPHA = 0.6
        private const val DARK_THEME_ALPHA = 0.78
    }
}

open class TactReferenceLinksGeneratingProvider(private val linkMap: LinkMap, baseURI: URI?, resolveAnchors: Boolean) :
    ReferenceLinksGeneratingProvider(linkMap, baseURI, resolveAnchors) {
    override fun renderLink(visitor: HtmlGenerator.HtmlGeneratingVisitor, text: String, node: ASTNode, info: RenderInfo) {
        super.renderLink(visitor, text, node, info.copy(destination = markLinkAsLanguageItemIfItIsTactPath(info.destination)))
    }

    override fun getRenderInfo(text: String, node: ASTNode): RenderInfo? {
        val label = node.children.firstOrNull { it.type == MarkdownElementTypes.LINK_LABEL } ?: return null
        val labelText = label.getTextInNode(text)

        val linkInfo = linkMap.getLinkInfo(labelText)
        val (linkDestination, linkTitle) = if (linkInfo != null) {
            linkInfo.destination to linkInfo.title
        } else {
            val linkText = labelText.removeSurrounding("[", "]").removeSurrounding("`")
            if (!linkIsProbablyValidTactPath(linkText)) return null
            linkText to null
        }

        val linkTextNode = node.children.firstOrNull { it.type == MarkdownElementTypes.LINK_TEXT }
        return RenderInfo(
            linkTextNode ?: label,
            EntityConverter.replaceEntities(linkDestination, processEntities = true, processEscapes = true),
            linkTitle?.let { EntityConverter.replaceEntities(it, processEntities = true, processEscapes = true) }
        )
    }
}

open class TactInlineLinkGeneratingProvider(baseURI: URI?, resolveAnchors: Boolean) :
    InlineLinkGeneratingProvider(baseURI, resolveAnchors) {
    override fun renderLink(visitor: HtmlGenerator.HtmlGeneratingVisitor, text: String, node: ASTNode, info: RenderInfo) {
        super.renderLink(visitor, text, node, info.copy(destination = markLinkAsLanguageItemIfItIsTactPath(info.destination)))
    }
}

private fun linkIsProbablyValidTactPath(link: CharSequence): Boolean {
    return link.none { it in "/#" || it.isWhitespace() }
}

private fun markLinkAsLanguageItemIfItIsTactPath(link: CharSequence): CharSequence {
    return if (linkIsProbablyValidTactPath(link)) "${DocumentationManagerProtocol.PSI_ELEMENT_PROTOCOL}$link" else link
}
