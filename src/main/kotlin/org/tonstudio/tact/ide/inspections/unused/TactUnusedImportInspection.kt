package org.tonstudio.tact.ide.inspections.unused

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementVisitor
import org.tonstudio.tact.ide.inspections.TactBaseInspection
import org.tonstudio.tact.lang.imports.TactImportOptimizer
import org.tonstudio.tact.lang.psi.TactFile
import org.tonstudio.tact.lang.psi.TactImportList
import org.tonstudio.tact.lang.psi.TactVisitor

class TactUnusedImportInspection : TactBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        val file = holder.file as? TactFile ?: return PsiElementVisitor.EMPTY_VISITOR
        val imports = file.getImports()
        val unusedImports = TactImportOptimizer().collectUnusedImports(file, imports)

        return object : TactVisitor() {
            override fun visitImportList(o: TactImportList) {
                super.visitImportList(o)

                for (importDeclaration in o.importDeclarationList) {
                    importDeclaration.stringLiteral?.references?.lastOrNull()?.resolve()
                        ?: // don't warn if import is unresolved
                        continue

                    if (!unusedImports.unusedImports.contains(importDeclaration)) {
                        continue
                    }

                    holder.registerProblem(
                        importDeclaration,
                        "Unused import '${importDeclaration.path}'",
                        ProblemHighlightType.LIKE_UNUSED_SYMBOL,
                        OPTIMIZE_QUICK_FIX
                    )
                }
            }
        }
    }
}

val OPTIMIZE_QUICK_FIX = object : LocalQuickFix {
    override fun getFamilyName() = "Optimize imports"

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val element = descriptor.psiElement ?: return
        val file = element.containingFile
        TactImportOptimizer().processFile(file).run()
    }
}
