package org.tonstudio.tact.ide.documentation

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.psi.PsiDocCommentBase
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.SyntaxTraverser
import org.tonstudio.tact.lang.doc.psi.TactDocComment
import org.tonstudio.tact.lang.psi.TactAsmFunctionDeclaration
import org.tonstudio.tact.lang.psi.TactFile
import org.tonstudio.tact.lang.psi.TactFunctionDeclaration
import org.tonstudio.tact.lang.psi.TactNativeFunctionDeclaration
import java.util.function.Consumer

class TactDocumentationProvider : AbstractDocumentationProvider() {
    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?) = when (element) {
        is TactFunctionDeclaration       -> element.generateDoc()
        is TactAsmFunctionDeclaration    -> element.generateDoc()
        is TactNativeFunctionDeclaration -> element.generateDoc()
        else                             -> null
    }

    override fun collectDocComments(file: PsiFile, sink: Consumer<in PsiDocCommentBase>) {
        if (file !is TactFile) return
        for (element in SyntaxTraverser.psiTraverser(file)) {
            if (element is TactDocComment && element.owner != null) {
                sink.accept(element)
            }
        }
    }

    override fun generateRenderedDoc(comment: PsiDocCommentBase): String? {
        return (comment as? TactDocComment)?.documentationAsHtml(renderMode = TactDocRenderMode.INLINE_DOC_COMMENT)
    }
}
