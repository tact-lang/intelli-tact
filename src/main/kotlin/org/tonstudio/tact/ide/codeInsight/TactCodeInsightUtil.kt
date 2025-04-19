package org.tonstudio.tact.ide.codeInsight

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.tonstudio.tact.lang.psi.*
import org.tonstudio.tact.utils.parentNth
import org.tonstudio.tact.utils.parentOfTypeWithStop

// TODO: move to lang
object TactCodeInsightUtil {
    fun findCallExpr(element: PsiElement): TactCallExpr? {
        if (element.parent is TactFieldName) {
            return element.parent.parentNth(4)
        }

        val parentValue = element.parentOfTypeWithStop<TactValue>(TactSignatureOwner::class)
        if (parentValue != null) {
            return parentValue.parentNth(3) ?: element.parentOfTypeWithStop(TactSignatureOwner::class)
        }
        return element.parentOfTypeWithStop(TactSignatureOwner::class)
    }

    fun sameModule(firstFile: PsiFile, secondFile: PsiFile): Boolean {
        if (firstFile == secondFile) return true

        val containingDirectory = firstFile.containingDirectory
        return !(containingDirectory == null || containingDirectory != secondFile.containingDirectory)
    }
}
