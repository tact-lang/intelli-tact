package org.tonstudio.tact.ide.documentation

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiDocCommentBase
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.SyntaxTraverser
import org.tonstudio.tact.lang.doc.psi.TactDocComment
import org.tonstudio.tact.lang.psi.*
import java.util.function.Consumer

class TactDocumentationProvider : AbstractDocumentationProvider() {
    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?) = when (element) {
        is TactFunctionDeclaration       -> element.generateDoc()
        is TactAsmFunctionDeclaration    -> element.generateDoc()
        is TactNativeFunctionDeclaration -> element.generateDoc()
        is TactStructDeclaration         -> element.generateDoc()
        is TactMessageDeclaration        -> element.generateDoc()
        is TactTraitDeclaration          -> element.generateDoc()
        is TactContractDeclaration       -> element.generateDoc()
        is TactPrimitiveDeclaration      -> element.generateDoc()
        is TactConstDeclaration          -> element.generateDoc()
        is TactVarDefinition             -> element.generateDoc()
        is TactParamDefinition           -> element.generateDoc()
        is TactFieldDefinition           -> element.generateDoc()
        is TactAsmInstruction            -> element.generateDoc()
        is TactTlb                       -> element.generateDoc()
        else                             -> null
    }

    override fun getCustomDocumentationElement(editor: Editor, file: PsiFile, contextElement: PsiElement?, targetOffset: Int): PsiElement? {
        val parent = contextElement?.parent
        val grand = parent?.parent
        if (parent is TactAsmInstruction) {
            return parent
        }
        if (grand is TactTlb) {
            return grand
        }
        return super.getCustomDocumentationElement(editor, file, contextElement, targetOffset)
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
