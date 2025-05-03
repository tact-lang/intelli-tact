package org.tonstudio.tact.lang

import com.intellij.lang.CodeDocumentationAwareCommenterEx
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import org.tonstudio.tact.lang.doc.psi.TactDocComment
import org.tonstudio.tact.lang.psi.TactDocElementTypes
import org.tonstudio.tact.lang.psi.TactTokenTypes

class TactCommenter : CodeDocumentationAwareCommenterEx {
    override fun getLineCommentPrefix() = "// "
    override fun getBlockCommentPrefix() = "/*"
    override fun getBlockCommentSuffix() = "*/"
    override fun getCommentedBlockCommentPrefix() = null
    override fun getCommentedBlockCommentSuffix() = null
    override fun getLineCommentTokenType() = TactTokenTypes.LINE_COMMENT
    override fun getBlockCommentTokenType() = TactTokenTypes.MULTI_LINE_COMMENT
    override fun getDocumentationCommentTokenType() = TactDocElementTypes.DOC_COMMENT
    override fun getDocumentationCommentPrefix() = "///"
    override fun getDocumentationCommentLinePrefix() = "/// "
    override fun getDocumentationCommentSuffix() = null
    override fun isDocumentationComment(element: PsiComment) = element is TactDocComment

    override fun isDocumentationCommentText(element: PsiElement): Boolean {
        val node = element.node ?: return false
        return node.elementType == TactDocElementTypes.DOC_COMMENT
    }
}
