package org.tonstudio.tact.ide.highlight.exitpoint

import com.intellij.codeInsight.highlighting.HighlightUsagesHandlerBase
import com.intellij.codeInsight.highlighting.HighlightUsagesHandlerFactoryBase
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import org.tonstudio.tact.lang.TactTypes
import org.tonstudio.tact.lang.psi.TactAssertNotNullExpr
import org.tonstudio.tact.lang.psi.TactContractInitDeclaration

class TactHighlightExitPointsHandlerFactory : HighlightUsagesHandlerFactoryBase() {
    override fun createHighlightUsagesHandler(editor: Editor, file: PsiFile, target: PsiElement): HighlightUsagesHandlerBase<*>? {
        if (target !is LeafPsiElement) return null

        val prevToken = PsiTreeUtil.prevLeaf(target) ?: return null
        return createHandler(editor, file, target) ?: createHandler(editor, file, prevToken)
    }

    private fun createHandler(editor: Editor, file: PsiFile, target: PsiElement): HighlightUsagesHandlerBase<*>? {
        val elementType = target.elementType

        if (
            elementType == TactTypes.IDENTIFIER &&
            target.parent is TactContractInitDeclaration &&
            target.textMatches("init")
        ) {
            return TactFunctionExitPointHandler.createForElement(editor, file, target)
        }

        if (
            elementType == TactTypes.RETURN ||
            elementType == TactTypes.FUN ||
            elementType == TactTypes.RECEIVE ||
            elementType == TactTypes.EXTERNAL ||
            elementType == TactTypes.BOUNCED
        ) {
            return TactFunctionExitPointHandler.createForElement(editor, file, target)
        }

        if (elementType == TactTypes.ASSERT_OP) {
            if (target.parent is TactAssertNotNullExpr) {
                return TactFunctionExitPointHandler.createForElement(editor, file, target)
            }
        }

        return null
    }

}
