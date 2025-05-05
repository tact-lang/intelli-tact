package org.tonstudio.tact.ide.hints

import com.intellij.codeInsight.hints.declarative.*
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.tonstudio.tact.lang.psi.*
import org.tonstudio.tact.lang.psi.types.TactBaseTypeEx.Companion.toEx
import org.tonstudio.tact.lang.psi.types.TactPrimitiveTypeEx
import org.tonstudio.tact.lang.psi.types.TactPrimitiveTypes
import org.tonstudio.tact.lang.psi.types.TactTypeEx

class TactImplicitAsInt257HintsProvider : InlayHintsProvider {
    override fun createCollector(file: PsiFile, editor: Editor) = Collector()

    class Collector : SharedBypassCollector {
        override fun collectFromElement(element: PsiElement, sink: InlayTreeSink) {
            if (element !is TactFieldDeclaration) return

            val typeNode = element.type ?: return
            val type = element.fieldDefinition.getType(null) ?: return

            // foo: Int
            if (needImplicitHint(type)) {
                addHint(sink, typeNode)
            }

            // foo: map<Int, Int>
            if (typeNode is TactMapType) {
                val key = typeNode.keyType.toEx()
                val value = typeNode.valueType.toEx()

                if (needImplicitHint(key)) {
                    addHint(sink, typeNode.keyType!!)
                }
                if (needImplicitHint(value)) {
                    addHint(sink, typeNode.valueType!!)
                }
            }
        }

        private fun needImplicitHint(type: TactTypeEx): Boolean =
            type is TactPrimitiveTypeEx && type.name == TactPrimitiveTypes.INT && type.tlbType == null

        private fun addHint(sink: InlayTreeSink, typeNode: TactType) {
            sink.addPresentation(
                InlineInlayPosition(typeNode.textRange.endOffset, true),
                listOf(),
                "Default TL-B serialization type for Int type.<br>Learn more in documentation: <a href=\"https://docs.tact-lang.org/book/integers/#common-serialization-types\">https://docs.tact-lang.org/book/integers/#common-serialization-types</a>",
                HintFormat.default
                    .withColorKind(HintColorKind.TextWithoutBackground)
                    .withHorizontalMargin(HintMarginPadding.MarginAndSmallerPadding)
                    .withFontSize(HintFontSize.AsInEditor)
            ) {
                text("as int257")
            }
        }
    }
}
