package org.tonstudio.tact.ide.hints

import com.intellij.codeInsight.hints.declarative.*
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.tonstudio.tact.lang.psi.*
import org.tonstudio.tact.lang.psi.types.TactUnknownTypeEx

class TactVariableTypeHintsProvider : InlayHintsProvider {
    override fun createCollector(file: PsiFile, editor: Editor) = Collector()

    class Collector : SharedBypassCollector {
        override fun collectFromElement(element: PsiElement, sink: InlayTreeSink) {
            if (element is TactVarDefinition) {
                handleVarDefinition(element, sink)
            }
        }

        private fun handleVarDefinition(element: TactVarDefinition, sink: InlayTreeSink) {
            if (element.isBlank()) {
                // don't show a hint for "_" variables
                return
            }

            val parent = element.parent as? TactVarDeclaration
            if (parent?.typeHint != null) {
                // no need to show a hint if there is a type hint
                return
            }

            if (isObviousCase(parent?.expression)) {
                return
            }

            val type = element.getType(null) ?: return
            if (type is TactUnknownTypeEx) {
                // no need to show a hint if type is unknown
                return
            }

            sink.addPresentation(
                InlineInlayPosition(element.textRange.endOffset, true),
                listOf(),
                null,
                HintFormat.default.withColorKind(HintColorKind.Parameter).withHorizontalMargin(HintMarginPadding.MarginAndSmallerPadding)
            ) {
                text(": ")
                text(type.readableName(element))
            }
        }

        private fun isObviousCase(element: PsiElement?): Boolean {
            if (element == null) return false

            if (element is TactStringLiteral) {
                // let a = "Hello";
                return true
            }

            if (element is TactLiteral && element.isBoolean) {
                // let a = true;
                // let a = false;
                return true
            }

            if (element is TactLiteralValueExpression) {
                // let a = Foo {};
                return true
            }

            if (element is TactCallExpr) {
                if (element.qualifier == null) {
                    return false
                }
                val name = element.identifier?.text ?: return false
                // let a = Foo.fromCell(cell);
                return name == "fromCell" || name == "fromSlice"
            }

            return false
        }
    }
}