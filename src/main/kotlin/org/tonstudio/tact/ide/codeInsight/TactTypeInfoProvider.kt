package org.tonstudio.tact.ide.codeInsight

import com.intellij.lang.ExpressionTypeProvider
import com.intellij.openapi.util.Conditions
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.SyntaxTraverser
import com.intellij.psi.util.PsiTreeUtil
import io.ktor.util.*
import org.tonstudio.tact.lang.psi.TactFile
import org.tonstudio.tact.lang.psi.TactFunctionDeclaration
import org.tonstudio.tact.lang.psi.TactTypeOwner

class TactTypeInfoProvider : ExpressionTypeProvider<TactTypeOwner>() {
    override fun getInformationHint(element: TactTypeOwner): String {
        val type = element.getType(null) ?: return ""
        return type.readableName(element).escapeHTML().replace(" ", "&nbsp;")
    }

    override fun getErrorHint() = "Selection doesn't contain a Tact expression"

    override fun getExpressionsAt(at: PsiElement): List<TactTypeOwner?> {
        var element = at
        if (element is PsiWhiteSpace && element.textMatches("\n")) {
            element = PsiTreeUtil.prevLeaf(element)!!
        }
        return SyntaxTraverser.psiApi()
            .parents(element)
            .takeWhile(
                Conditions.notInstanceOf(TactFile::class.java),
            )
            .filter(TactTypeOwner::class.java)
            .filter(Conditions.notInstanceOf(TactFunctionDeclaration::class.java))
            .toList()
    }
}
