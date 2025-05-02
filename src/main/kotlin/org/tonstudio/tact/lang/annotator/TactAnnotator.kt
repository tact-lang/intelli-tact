package org.tonstudio.tact.lang.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import org.tonstudio.tact.ide.colors.TactColor
import org.tonstudio.tact.lang.TactTypes
import org.tonstudio.tact.lang.psi.*
import org.tonstudio.tact.lang.psi.impl.TactReference
import org.tonstudio.tact.lang.psi.types.TactPrimitiveTypes

class TactAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (holder.isBatchMode) return

        val color = highlightLeaf(element) ?: return
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(element.textRange)
            .textAttributes(color.textAttributesKey).create()
    }

    private fun highlightLeaf(element: PsiElement): TactColor? {
        val parent = element.parent as? TactCompositeElement ?: return null

        if (
            element.elementType == TactTypes.IDENTIFIER &&
            parent is TactReferenceExpressionBase &&
            parent.reference != null
        ) {
            return highlightReference(parent, parent.reference as TactReference)
        }

        return null
    }

    private fun highlightReference(
        element: TactReferenceExpressionBase,
        reference: TactReference,
    ): TactColor? {
        if (TactPrimitiveTypes.isPrimitiveType(element.text)) {
            return null
        }

        val resolved = reference.resolve() ?: return null

        return when (resolved) {
            is TactPrimitiveDeclaration      -> TactColor.PRIMITIVE
            is TactFunctionDeclaration       -> TactColor.FUNCTION
            is TactAsmFunctionDeclaration    -> TactColor.FUNCTION
            is TactNativeFunctionDeclaration -> TactColor.FUNCTION
            is TactStructDeclaration         -> TactColor.STRUCT
            is TactContractDeclaration       -> TactColor.CONTRACT
            is TactTraitDeclaration          -> TactColor.TRAIT
            is TactMessageDeclaration        -> TactColor.MESSAGE
            is TactFieldDefinition           -> TactColor.FIELD
            is TactParamDefinition           -> TactColor.PARAMETER
            is TactVarDefinition             -> TactColor.VARIABLE
            is TactConstDefinition           -> TactColor.CONSTANT
            else                             -> null
        }
    }
}
