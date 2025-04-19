package org.tonstudio.tact.ide.hints

import com.intellij.codeInsight.hints.declarative.*
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.tonstudio.tact.lang.psi.*

class TactGetMethodIdHintsProvider : InlayHintsProvider {
    override fun createCollector(file: PsiFile, editor: Editor) = Collector()

    class Collector : SharedBypassCollector {
        override fun collectFromElement(element: PsiElement, sink: InlayTreeSink) {
            if (element is TactFunctionDeclaration && element.isGet()) {
                val (id, explicit) = element.computeMethodId()
                if (explicit) {
                    return
                }

                val anchor = element.functionAttributeList.firstOrNull() ?: element.firstChild

                sink.addPresentation(
                    InlineInlayPosition(anchor.textRange.endOffset, true),
                    listOf(),
                    null, HintFormat.default.withColorKind(HintColorKind.Parameter).withHorizontalMargin(HintMarginPadding.MarginAndSmallerPadding)
                ) {
                    text("(")
                    text(id)
                    text(")")
                }
            }
        }
    }
}
