package org.tonstudio.tact.ide.hints

import com.intellij.codeInsight.hints.declarative.*
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.tonstudio.tact.asm.findInstruction
import org.tonstudio.tact.asm.instructionPresentation
import org.tonstudio.tact.lang.psi.*

class TactAssemblyInfoHintsProvider : InlayHintsProvider {
    override fun createCollector(file: PsiFile, editor: Editor) = Collector()

    class Collector : SharedBypassCollector {
        override fun collectFromElement(element: PsiElement, sink: InlayTreeSink) {
            if (element !is TactAsmExpression) return

            val instr = element.asmInstruction
            val arguments = element.asmArguments.asmPrimitiveList

            val info = findInstruction(instr.identifier.text, arguments)

            val presentation = instructionPresentation(info?.doc?.gas, info?.doc?.stack, "{gas} {stack}")

            sink.addPresentation(
                InlineInlayPosition(element.textRange.endOffset, true),
                listOf(),
                null,
                HintFormat.default
                    .withColorKind(HintColorKind.TextWithoutBackground)
                    .withHorizontalMargin(HintMarginPadding.MarginAndSmallerPadding)
                    .withFontSize(HintFontSize.AsInEditor)
            ) {
                text(presentation)
            }
        }
    }
}
