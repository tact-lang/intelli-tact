package org.tonstudio.tact.ide.templates

import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiUtilCore
import com.intellij.psi.util.parentOfType
import org.tonstudio.tact.lang.TactLanguage
import org.tonstudio.tact.lang.completion.TactCompletionUtil
import org.tonstudio.tact.lang.psi.TactFile
import org.tonstudio.tact.lang.psi.TactImportDeclaration
import org.tonstudio.tact.lang.psi.TactSimpleStatement
import org.tonstudio.tact.utils.inside

abstract class TactTemplateContextType(presentableName: String) : TemplateContextType(presentableName) {
    override fun isInContext(templateActionContext: TemplateActionContext): Boolean {
        val file = templateActionContext.file
        val offset = templateActionContext.startOffset

        if (!PsiUtilCore.getLanguageAtOffset(file, offset).isKindOf(TactLanguage)) {
            return false
        }

        var element = file.findElementAt(offset)
        if (element == null) {
            element = file.findElementAt(offset - 1)
        }

        if (element == null) {
            return false
        }

        if (TactCompletionUtil.shouldSuppressCompletion(element)) {
            return false
        }

        when {
            element is PsiWhiteSpace                              -> return false
            element.parentOfType<PsiComment>() != null            -> return isCommentInContext()
            element.parentOfType<TactImportDeclaration>() != null -> return false
        }

        return isInContext(element)
    }

    protected abstract fun isInContext(element: PsiElement): Boolean

    protected open fun isCommentInContext() = false

    class Generic : TactTemplateContextType("Tact") {
        override fun isInContext(element: PsiElement) = element.parent is TactFile || element.parent.parent is TactFile
    }

    class TopLevel : TactTemplateContextType("Top-level") {
        override fun isInContext(element: PsiElement): Boolean {
            val simpleStatement = element.parentOfType<TactSimpleStatement>() ?: return false
            return simpleStatement.parent is TactFile
        }
    }

    class Statement : TactTemplateContextType("Statement") {
        override fun isInContext(element: PsiElement) = element.inside<TactSimpleStatement>()
    }

    class Comment : TactTemplateContextType("Comment") {
        override fun isInContext(element: PsiElement) = false
        override fun isCommentInContext() = true
    }
}
