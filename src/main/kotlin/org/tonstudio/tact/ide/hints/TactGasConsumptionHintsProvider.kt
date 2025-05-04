package org.tonstudio.tact.ide.hints

import com.intellij.codeInsight.hints.declarative.*
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.tonstudio.tact.asm.GasSettings
import org.tonstudio.tact.asm.computeSeqGasConsumption
import org.tonstudio.tact.asm.instructionPresentation
import org.tonstudio.tact.lang.psi.*
import org.tonstudio.tact.utils.line

class TactGasConsumptionHintsProvider : InlayHintsProvider {
    override fun createCollector(file: PsiFile, editor: Editor) = Collector()

    class Collector : SharedBypassCollector {
        override fun collectFromElement(element: PsiElement, sink: InlayTreeSink) {
            if (element !is TactAsmSequence) return

            val openBrace = element.firstChild.nextSibling ?: return
            val closeBrace = element.lastChild.prevSibling ?: return

            if (openBrace.line() == closeBrace.line()) return

            val gas = computeSeqGasConsumption(element, GasSettings(loopGasCoefficient = 3))

            sink.addPresentation(
                InlineInlayPosition(openBrace.textRange.endOffset, true),
                listOf(),
                null,
                HintFormat.default
                    .withColorKind(HintColorKind.TextWithoutBackground)
                    .withHorizontalMargin(HintMarginPadding.MarginAndSmallerPadding)
                    .withFontSize(HintFontSize.AsInEditor)
            ) {
                text(instructionPresentation(gas.value.toString(), "", "{gas}"))
            }
        }
    }
}
