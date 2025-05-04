package org.tonstudio.tact.ide.highlight.exitpoint

import com.intellij.codeInsight.highlighting.HighlightUsagesHandlerBase
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.parentOfTypes
import com.intellij.util.Consumer
import org.tonstudio.tact.lang.psi.*

class TactFunctionExitPointHandler(
    editor: Editor,
    file: PsiFile,
    private val target: PsiElement,
    private val function: TactTypeOwner,
) : HighlightUsagesHandlerBase<PsiElement>(editor, file) {

    override fun getTargets() = listOf(target)

    override fun selectTargets(targets: List<PsiElement>, selectionConsumer: Consumer<in MutableList<out PsiElement>?>) {
        selectionConsumer.consume(targets.toMutableList())
    }

    override fun computeUsages(targets: List<PsiElement>) {
        if (function is TactFunctionOrMethodDeclaration) {
            val identifier = function.getIdentifier()
            if (identifier != null) {
                addOccurrence(identifier)
            }
        }

        if (function is TactMessageFunctionDeclaration) {
            val identifier = function.getIdentifier()
            if (identifier != null) {
                addOccurrence(identifier)
            }
        }

        val visitor = object : TactRecursiveElementVisitor() {
            override fun visitReturnStatement(expr: TactReturnStatement) {
                addOccurrence(expr)
            }

            override fun visitCallExpr(o: TactCallExpr) {
                val callerName = o.expression?.text
                if (callerName == "throw") {
                    addOccurrence(o)
                }
            }

            override fun visitAssertNotNullExpr(o: TactAssertNotNullExpr) {
                addOccurrence(o.assertOp)
            }
        }

        visitor.visitTypeOwner(function)
    }

    companion object {
        fun createForElement(editor: Editor, file: PsiFile, element: PsiElement): TactFunctionExitPointHandler? {
            val function = element.parentOfTypes(
                TactFunctionOrMethodDeclaration::class,
                TactMessageFunctionDeclaration::class,
                TactContractInitDeclaration::class
            )
            return function?.let { TactFunctionExitPointHandler(editor, file, element, it) }
        }
    }
}
