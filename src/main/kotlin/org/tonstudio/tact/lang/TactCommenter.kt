package org.tonstudio.tact.lang

import com.intellij.codeInsight.generation.CommenterDataHolder
import com.intellij.codeInsight.generation.SelfManagingCommenter
import com.intellij.codeInsight.generation.SelfManagingCommenterUtil
import com.intellij.lang.CodeDocumentationAwareCommenterEx
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.util.text.CharArrayUtil
import org.tonstudio.tact.lang.psi.TactDocElementTypes
import org.tonstudio.tact.lang.psi.TactTokenTypes

class TactCommenter : CodeDocumentationAwareCommenterEx, SelfManagingCommenter<TactCommenter.TactCommenterDataHolder> {
    class TactCommenterDataHolder : CommenterDataHolder()

    override fun getLineCommentPrefix() = "//"

    override fun getBlockCommentPrefix() = "/*"

    override fun getBlockCommentSuffix() = "*/"

    override fun getCommentedBlockCommentPrefix() = null

    override fun getCommentedBlockCommentSuffix() = null

    override fun getLineCommentTokenType() = TactTokenTypes.LINE_COMMENT

    override fun getBlockCommentTokenType() = TactTokenTypes.MULTI_LINE_COMMENT

    override fun getDocumentationCommentTokenType() = TactDocElementTypes.DOC_COMMENT

    override fun getDocumentationCommentPrefix() = "/**"

    override fun getDocumentationCommentLinePrefix() = "*"

    override fun getDocumentationCommentSuffix() = "*/"

    override fun isDocumentationComment(element: PsiComment) = false

    override fun isDocumentationCommentText(element: PsiElement): Boolean {
        val node = element.node ?: return false
        return node.elementType == TactDocElementTypes.DOC_COMMENT
    }

    override fun getBlockCommentPrefix(selectionStart: Int, document: Document, data: TactCommenterDataHolder): String {
        return blockCommentPrefix
    }

    override fun getBlockCommentSuffix(selectionEnd: Int, document: Document, data: TactCommenterDataHolder): String {
        return blockCommentSuffix
    }

    override fun insertBlockComment(startOffset: Int, endOffset: Int, document: Document, data: TactCommenterDataHolder): TextRange {
        return SelfManagingCommenterUtil.insertBlockComment(
            startOffset, endOffset,
            document, blockCommentPrefix, blockCommentSuffix,
        )
    }

    override fun uncommentBlockComment(startOffset: Int, endOffset: Int, document: Document?, data: TactCommenterDataHolder) {
        SelfManagingCommenterUtil.uncommentBlockComment(
            startOffset,
            endOffset,
            document!!,
            blockCommentPrefix,
            blockCommentSuffix,
        )
    }

    override fun getBlockCommentRange(
        selectionStart: Int,
        selectionEnd: Int,
        document: Document,
        data: TactCommenterDataHolder,
    ): TextRange? {
        return SelfManagingCommenterUtil.getBlockCommentRange(
                selectionStart,
                selectionEnd,
                document,
                blockCommentPrefix,
                blockCommentSuffix,
            )
    }

    override fun getCommentPrefix(line: Int, document: Document, data: TactCommenterDataHolder): String {
        return lineCommentPrefix
    }

    override fun isLineCommented(line: Int, offset: Int, document: Document, data: TactCommenterDataHolder): Boolean {
        return CharArrayUtil.regionMatches(document.charsSequence, offset, lineCommentPrefix)
    }

    override fun uncommentLine(line: Int, offset: Int, document: Document, data: TactCommenterDataHolder) {
        if (CharArrayUtil.regionMatches(document.charsSequence, offset, lineCommentPrefix)) {
            val hasSpaceAfterPrefix = document.charsSequence[offset + lineCommentPrefix.length] == ' '
            val endOffset = offset + lineCommentPrefix.length + if (hasSpaceAfterPrefix) 1 else 0
            document.deleteString(offset, endOffset)
        }
    }

    override fun commentLine(line: Int, offset: Int, document: Document, data: TactCommenterDataHolder) {
        document.insertString(offset, "$lineCommentPrefix ")
    }

    override fun createLineCommentingState(startLine: Int, endLine: Int, document: Document, file: PsiFile): TactCommenterDataHolder {
        return TactCommenterDataHolder()
    }

    override fun createBlockCommentingState(start: Int, end: Int, document: Document, file: PsiFile): TactCommenterDataHolder {
        return TactCommenterDataHolder()
    }
}
