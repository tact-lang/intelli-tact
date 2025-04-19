package org.tonstudio.tact.ide.hints

import com.intellij.codeInsight.hints.VcsCodeVisionLanguageContext
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import org.tonstudio.tact.lang.psi.*
import java.awt.event.MouseEvent

@Suppress("UnstableApiUsage")
class TactCodeVisionLanguageContext : VcsCodeVisionLanguageContext {
    override fun isAccepted(element: PsiElement) = element is TactFunctionDeclaration ||
            element is TactAsmFunctionDeclaration ||
            element is TactNativeFunctionDeclaration ||
            element is TactStructDeclaration ||
            element is TactTraitDeclaration ||
            element is TactMessageDeclaration ||
            element is TactConstDeclaration

    override fun handleClick(mouseEvent: MouseEvent, editor: Editor, element: PsiElement) {}
}
