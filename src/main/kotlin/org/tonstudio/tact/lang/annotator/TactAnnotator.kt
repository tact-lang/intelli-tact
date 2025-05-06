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
            !element.textMatches("self") &&
            parent is TactReferenceExpressionBase &&
            parent.reference != null
        ) {
            return highlightReference(parent.reference as TactReference)
        }

        if (parent is TactVarDefinition && !parent.isReadonly) {
            return TactColor.MUTABLE_VARIABLE
        }

        return null
    }

    private fun highlightReference(reference: TactReference): TactColor? {
        val resolved = reference.resolve() ?: return null
        if (resolved is TactVarDefinition) {
            if (resolved.isReadonly) {
                return TactColor.VARIABLE
            }
            return TactColor.MUTABLE_VARIABLE
        }

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
            is TactConstDeclaration          -> TactColor.CONSTANT
            else                             -> null
        }
    }
}
