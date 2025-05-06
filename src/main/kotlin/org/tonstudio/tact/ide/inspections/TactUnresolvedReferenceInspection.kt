package org.tonstudio.tact.ide.inspections

import com.intellij.codeInspection.*
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiReference
import org.tonstudio.tact.lang.imports.TactImportFileQuickFix
import org.tonstudio.tact.lang.psi.TactAttribute
import org.tonstudio.tact.lang.psi.TactCompositeElement
import org.tonstudio.tact.lang.psi.TactFieldName
import org.tonstudio.tact.lang.psi.TactReferenceExpression
import org.tonstudio.tact.lang.psi.TactTlb
import org.tonstudio.tact.lang.psi.TactTypeReferenceExpression
import org.tonstudio.tact.lang.psi.TactVisitor
import org.tonstudio.tact.lang.psi.impl.TactReference
import org.tonstudio.tact.utils.inside

class TactUnresolvedReferenceInspection : TactBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : TactVisitor() {
            override fun visitTypeReferenceExpression(o: TactTypeReferenceExpression) {
                super.visitTypeReferenceExpression(o)

                if (o.parent is TactTlb) return
                val text = o.text
                if (text == "T" || text == "S" || text == "M" || text == "K" || text == "V") return // special placeholders until generics

                val reference = o.reference
                val qualifier = o.getQualifier()
                val id = o.getIdentifier()
                processReference(o, id, qualifier, reference)
            }

            override fun visitReferenceExpression(o: TactReferenceExpression) {
                super.visitReferenceExpression(o)

                if (o.parent is TactFieldName) return
                if (o.inside<TactAttribute>()) return

                val reference = o.reference
                val qualifier = o.getQualifier()
                val id = o.getIdentifier()
                processReference(o, id, qualifier, reference)
            }

            private fun processReference(
                expr: TactCompositeElement,
                id: PsiElement,
                qualifier: TactCompositeElement?,
                reference: TactReference,
            ) {
                if (qualifier != null) return

                if (reference.resolve() == null) {
                    val fixes = createImportFileFixes(expr, reference, holder.isOnTheFly)
                    holder.registerProblem(id, "Unresolved reference '${id.text}'", ProblemHighlightType.LIKE_UNKNOWN_SYMBOL, *fixes)
                }
            }
        }
    }

    private fun createImportFileFixes(target: TactCompositeElement, reference: PsiReference, onTheFly: Boolean): Array<LocalQuickFix> {
        if (onTheFly) {
            val importFix = TactImportFileQuickFix(reference)
            if (importFix.isAvailable(target.project, target.containingFile, target, target)) {
                return arrayOf(importFix)
            }
        }

        val filesToImport = TactImportFileQuickFix.getImportVariantsToImport(reference.canonicalText, target)
        if (filesToImport.isNotEmpty()) {
            val result = mutableListOf<LocalQuickFix>()
            for (importPath in filesToImport) {
                val importFix = TactImportFileQuickFix(target, importPath)
                if (importFix.isAvailable(target.project, target.containingFile, target, target)) {
                    result.add(importFix)
                }
            }
            return result.toTypedArray()
        }

        return LocalQuickFix.EMPTY_ARRAY
    }
}
