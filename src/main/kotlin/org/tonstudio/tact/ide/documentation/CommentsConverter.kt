package org.tonstudio.tact.ide.documentation

import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import org.tonstudio.tact.lang.psi.TactConstDeclaration
import org.tonstudio.tact.lang.psi.TactFieldDefinition
import org.tonstudio.tact.lang.psi.TactTokenTypes

object CommentsConverter {
    fun toHtml(comments: List<PsiComment>): String {
        return comments.joinToString("<br>") {
            if (it.tokenType == TactTokenTypes.LINE_COMMENT) {
                it.text.removePrefix("///").removePrefix("//")
            } else {
                it.text
                    .removePrefix("/*")
                    .removeSuffix("*/")
                    .lines().joinToString("<br>") { line ->
                        line.removePrefix(" * ")
                    }
            }
        }
    }

    fun getCommentsForElement(element: PsiElement?): List<PsiComment> {
        var comments = getCommentsInner(adjustElement(element))
        if (comments.isEmpty()) {
            if (element is TactConstDeclaration) {
                val parent = element.parent // spec
                comments = getCommentsInner(parent)
                if (comments.isEmpty() && parent != null) {
                    return getCommentsInner(parent.parent) // declaration
                }
            } else if (element is TactFieldDefinition) {
                // comments after field definition
                // ```
                // struct Foo {
                //    value: Int // comment
                // }
                // ```
                val parent = element.parent
                val lastChild = parent?.lastChild
                if (lastChild is PsiComment) {
                    return listOf(lastChild)
                }
            }
        }
        return comments
    }

    private fun getCommentsInner(element: PsiElement?): List<PsiComment> {
        if (element == null) {
            return emptyList()
        }
        val result = mutableListOf<PsiComment>()
        var e: PsiElement?
        e = element.prevSibling
        while (e != null) {
            if (e is PsiWhiteSpace) {
                if (e.getText().contains("\n\n")) return result
                e = e.getPrevSibling()
                continue
            }
            if (e is PsiComment) {
                result.add(0, e)
            } else {
                return result
            }
            e = e.prevSibling
        }
        return result
    }

    private fun adjustElement(element: PsiElement?): PsiElement? {
        if (element is TactFieldDefinition) {
            return element.parent
        }

        return element
    }
}
