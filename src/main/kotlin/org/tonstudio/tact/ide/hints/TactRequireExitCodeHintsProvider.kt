package org.tonstudio.tact.ide.hints

import com.intellij.codeInsight.hints.declarative.*
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.tonstudio.tact.compiler.ExitCodeFormat
import org.tonstudio.tact.compiler.requireFunctionExitCode
import org.tonstudio.tact.lang.psi.*

class TactRequireExitCodeHintsProvider : InlayHintsProvider {
    override fun createCollector(file: PsiFile, editor: Editor) = Collector()

    class Collector : SharedBypassCollector {
        override fun collectFromElement(element: PsiElement, sink: InlayTreeSink) {
            if (element !is TactCallExpr) return

            val calledName = element.identifier?.text
            if (calledName != "require") return
            val secondArgument = element.arguments.lastOrNull() ?: return
            val stringLiteral = secondArgument as? TactStringLiteral ?: return
            val stringContent = stringLiteral.contents
            val exitCode = requireFunctionExitCode(stringContent, ExitCodeFormat.HEX)

            sink.addPresentation(
                InlineInlayPosition(element.textRange.endOffset, true),
                listOf(),
                null,
                HintFormat.default
                    .withColorKind(HintColorKind.TextWithoutBackground)
                    .withHorizontalMargin(HintMarginPadding.MarginAndSmallerPadding)
                    .withFontSize(HintFontSize.AsInEditor)
            ) {
                text("exit code: $exitCode")
            }
        }
    }
}
