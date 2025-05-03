package org.tonstudio.tact.ide.inspections

import com.intellij.codeInspection.*
import com.intellij.psi.PsiElement

abstract class TactBaseInspection : LocalInspectionTool(), CustomSuppressableInspectionTool {
    override fun getSuppressActions(element: PsiElement?): Array<SuppressIntentionAction?> {
        return arrayOf()
    }
}
