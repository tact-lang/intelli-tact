package org.tonstudio.tact.ide.inspections

import com.intellij.codeInspection.*
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

abstract class TactBaseInspection : LocalInspectionTool(), CustomSuppressableInspectionTool {
    override fun isAvailableForFile(file: PsiFile): Boolean {
        val path = file.virtualFile?.path ?: return super.isAvailableForFile(file)
        if (path.contains("node_modules")) {
            // don't check node_modules
            return false
        }

        val isInjected = InjectedLanguageManager.getInstance(file.project).isInjectedFragment(file)
        if (isInjected) {
            // don't check doc comments
            return false
        }
        return super.isAvailableForFile(file)
    }
    override fun getSuppressActions(element: PsiElement?): Array<SuppressIntentionAction?> {
        return arrayOf()
    }
}
