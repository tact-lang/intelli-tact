package org.tonstudio.tact.ide.inspections

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.tonstudio.tact.ide.inspections.unused.OPTIMIZE_QUICK_FIX
import org.tonstudio.tact.lang.imports.TactImportOptimizer
import org.tonstudio.tact.lang.psi.TactImportList
import org.tonstudio.tact.lang.psi.TactVisitor

class TactAmbiguousImportInspection : TactBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : TactVisitor() {
            override fun visitImportList(o: TactImportList) {
                super.visitImportList(o)

                val imports = o.importDeclarationList
                val duplicates = TactImportOptimizer().findDuplicateImports(imports)

                for (duplicate in duplicates) {
                    holder.registerProblem(
                        duplicate,
                        "Ambiguous import '${duplicate.path}', already imported earlier",
                        ProblemHighlightType.GENERIC_ERROR,
                        OPTIMIZE_QUICK_FIX
                    )
                }
            }
        }
    }
}
