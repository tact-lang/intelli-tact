package org.tonstudio.tact.lang.doc.psi

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.CompositeElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl
import com.intellij.psi.impl.source.tree.SharedImplUtil
import com.intellij.psi.tree.ILazyParseableElementType
import com.intellij.util.CharTable
import com.intellij.util.text.CharArrayUtil
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser
import org.tonstudio.tact.lang.TactLanguage
import org.tonstudio.tact.lang.doc.psi.impl.TactDocCommentImpl
import org.tonstudio.tact.lang.doc.psi.impl.TactDocGapImpl
import org.tonstudio.tact.lang.psi.TactDocElementTypes
import org.tonstudio.tact.lang.psi.TactDocElementTypes.DOC_DATA
import org.tonstudio.tact.lang.psi.TactDocElementTypes.DOC_GAP
import kotlin.math.max
import kotlin.math.min

class TactDocCommentElementType : ILazyParseableElementType("TACT_DOC_COMMENT", TactLanguage) {
    override fun doParseContents(chameleon: ASTNode, psi: PsiElement): ASTNode {
        val charTable = SharedImplUtil.findCharTableByTree(chameleon)
        val textMap = TactDocTextMap.new(chameleon.chars, TactDocKind.of(this))
        val root = TactDocCommentImpl(this, text = null)
        val markdownRoot = MarkdownParser(CommonMarkFlavourDescriptor()).buildMarkdownTreeFromString(textMap.mappedText.toString())
        TactDocMarkdownAstBuilder(textMap, charTable)
            .buildTree(root, markdownRoot)

        return root.firstChildNode
    }

    override fun createNode(text: CharSequence): ASTNode = TactDocCommentImpl(this, text)
}

private class TactDocMarkdownAstBuilder(
    private val textMap: TactDocTextMap,
    private val charTable: CharTable,
) {
    private var prevNodeEnd = 0

    fun buildTree(root: CompositeElement, markdownRoot: org.intellij.markdown.ast.ASTNode) {
        for (markdownChild in markdownRoot.children) {
            visitNode(root, markdownChild)
        }

        if (prevNodeEnd < textMap.originalText.length) {
            root.insertLeaves(prevNodeEnd, textMap.originalText.length)
        }
    }

    private fun visitNode(parent: CompositeElement, markdownNode: org.intellij.markdown.ast.ASTNode) {
        val type = TactDocElementTypes.mapMarkdownToTact(markdownNode.type)
        if (type == null) {
            // `null` means that we should skip the node. See docs for `mapMarkdownToV`
            if (markdownNode !is org.intellij.markdown.ast.LeafASTNode) {
                visitChildren(parent, markdownNode)
            }
            return
        }

        parent.insertLeaves(markdownNode.startOffset)

        val node = type.createCompositeNode()
        parent.rawAddChildrenWithoutNotifications(node)

        visitChildren(node, markdownNode)
        node.insertLeaves(markdownNode.endOffset)
    }

    private fun visitChildren(node: CompositeElement, markdownNode: org.intellij.markdown.ast.ASTNode) {
        for (markdownChild in markdownNode.children) {
            visitNode(node, markdownChild)
        }
    }

    private fun CompositeElement.insertLeaves(startOffset: Int, endOffset: Int) {
        textMap.processPiecesInRange(startOffset, endOffset) { piece ->
            val internedText = charTable.intern(piece.str)
            val element = when (piece.kind) {
                PieceKind.TEXT       -> LeafPsiElement(DOC_DATA, internedText)
                PieceKind.GAP        -> TactDocGapImpl(DOC_GAP, internedText)
                PieceKind.WHITESPACE -> PsiWhiteSpaceImpl(internedText)
            }
            rawAddChildrenWithoutNotifications(element)
        }
    }

    private fun CompositeElement.insertLeaves(endOffset: Int) {
        val endOffsetMapped = textMap.mapOffsetFromMarkdownToV(endOffset)
        if (endOffsetMapped != prevNodeEnd) {
            insertLeaves(prevNodeEnd, endOffsetMapped)
        }
        prevNodeEnd = endOffsetMapped
    }
}

private class TactDocTextMap(
    val originalText: CharSequence,
    val mappedText: CharSequence,
    private val offsetMap: IntArray, // mappedText -> originalText map
    private val pieces: List<Piece>,
) {
    fun mapOffsetFromMarkdownToV(offset: Int): Int = offsetMap[offset]

    inline fun processPiecesInRange(startOffset: Int, endOffset: Int, processor: (Piece) -> Unit) {
        var offset = 0
        for (p in pieces) {
            val pieceEndOffset = offset + p.str.length
            if (startOffset < pieceEndOffset && endOffset - offset > 0) {
                processor(p.cut(startOffset - offset, endOffset - offset))
            }
            offset += p.str.length
        }
    }

    companion object {
        fun new(text: CharSequence, kind: TactDocKind): TactDocTextMap {
            val pieces = mutableListOf<Piece>()
            val mappedText = StringBuilder()
            val map = IntArray(text.length + 1)
            var textPosition = 0
            for (line in kind.removeDecorationToLines(text)) {
                if (line.hasPrefix) {
                    val prefix = line.prefix
                    textPosition += prefix.length
                    pieces.mergeOrAddGapWithWS(prefix)
                }

                if (line.hasContent) {
                    val content = line.content

                    for (i in content.indices) {
                        map[mappedText.length + i] = textPosition + i
                    }
                    map[mappedText.length + content.length] = textPosition + content.length
                    textPosition += content.length
                    mappedText.append(content)
                    pieces += Piece(content, PieceKind.TEXT)
                }

                val hasLineBreak = !line.isLastLine && !line.hasSuffix

                if (hasLineBreak) {
                    if (!line.isRemoved) {
                        map[mappedText.length] = textPosition
                        map[mappedText.length + 1] = textPosition + 1
                        mappedText.append("\n")
                    }
                    textPosition += 1
                    pieces.mergeOrAddWS("\n")
                }

                if (line.hasSuffix) {
                    val suffix = line.suffix
                    textPosition += suffix.length
                    pieces.mergeOrAddGapWithWS(suffix)
                }
            }

            check(mappedText.length <= text.length)
            check(mappedText.length < map.size)
            check(mappedText.indices.all { map[it + 1] > map[it] })

            return TactDocTextMap(text, mappedText, map, pieces)
        }

        private fun MutableList<Piece>.mergeOrAddGapWithWS(str: CharSequence) {
            val gapStart = CharArrayUtil.shiftForward(str, 0, "\n\t ")
            if (gapStart != 0) {
                mergeOrAddWS(str.subSequence(0, gapStart))
            }
            if (gapStart != str.length) {
                val gapEnd = CharArrayUtil.shiftBackward(str, gapStart, str.lastIndex, "\n\t ") + 1
                val gapText = str.subSequence(gapStart, gapEnd)
                check(gapText.isNotEmpty())
                this += Piece(gapText, PieceKind.GAP)
                if (gapEnd != str.length) {
                    mergeOrAddWS(str.subSequence(gapEnd, str.length))
                }
            }
        }

        private fun MutableList<Piece>.mergeOrAddWS(gap: CharSequence) {
            if (lastOrNull()?.kind == PieceKind.WHITESPACE) {
                this[lastIndex] = Piece(this[lastIndex].str.toString() + gap, PieceKind.WHITESPACE)
            } else {
                this += Piece(gap, PieceKind.WHITESPACE)
            }
        }
    }
}

private data class Piece(val str: CharSequence, val kind: PieceKind)

private enum class PieceKind { TEXT, GAP, WHITESPACE }

private fun Piece.cut(startOffset: Int, endOffset: Int): Piece {
    val newStr = str.subSequence(max(0, startOffset), min(endOffset, str.length))
    return Piece(newStr, kind)
}
