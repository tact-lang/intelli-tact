package org.tonstudio.tact.lang.imports

import com.intellij.lang.ImportOptimizer
import com.intellij.openapi.util.EmptyRunnable
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import org.tonstudio.tact.lang.psi.*

class TactImportOptimizer : ImportOptimizer {
    override fun supports(file: PsiFile) = file is TactFile

    override fun processFile(file: PsiFile): Runnable {
        if (file !is TactFile) {
            return EmptyRunnable.getInstance()
        }

        val imports = file.getImports()
        val duplicateImports = findDuplicateImports(imports)
        val unusedImports = collectUnusedImports(file, imports)

        val importsToDelete = duplicateImports + unusedImports.unusedImports

        if (importsToDelete.isEmpty()) {
            return EmptyRunnable.getInstance()
        }

        return Runnable {
            val manager = PsiDocumentManager.getInstance(file.project)
            val document = manager.getDocument(file)
            if (document != null) {
                manager.commitDocument(document)
            }
            for (importEntry in importsToDelete) {
                if (!importEntry.isValid) continue
                deleteImport(importEntry)
            }
        }
    }

    fun findDuplicateImports(imports: List<TactImportDeclaration>): MutableSet<TactImportDeclaration> {
        val importsToDelete = mutableSetOf<TactImportDeclaration>()

        for (spec in imports) {
            val importPath = spec.path
            if (importsToDelete.contains(spec)) {
                continue
            }

            val importsWithSamePath = imports.filter { it.path == importPath }
            if (importsWithSamePath.size <= 1) continue

            val duplicateImports = importsWithSamePath.slice(1..importsWithSamePath.lastIndex)
            importsToDelete.addAll(duplicateImports)
        }

        return importsToDelete
    }

    private fun deleteImport(importDecl: TactImportDeclaration) {
        importDecl.delete()
    }

    data class UnusedImports(
        val unusedImports: MutableSet<TactImportDeclaration>,
    )

    fun collectUnusedImports(file: TactFile, imports: List<TactImportDeclaration>): UnusedImports {
        val importsToDelete = mutableSetOf<TactImportDeclaration>()
        val usedImports = mutableMapOf<String, TactImportDeclaration>()

        val namesPerImport = mutableMapOf<TactImportDeclaration, Set<String>>()

        for (imp in imports) {
            val ref = imp.stringLiteral?.references?.lastOrNull() ?: continue
            val importedFile = ref.resolve() as? TactFile ?: continue
            val names = importedFile.getNames()

            namesPerImport[imp] = names
        }

        file.accept(object : TactRecursiveElementVisitor() {
            override fun visitReferenceExpression(o: TactReferenceExpression) {
                super.visitReferenceExpression(o)
                checkReferenceExpression(o)
            }

            override fun visitTypeReferenceExpression(o: TactTypeReferenceExpression) {
                super.visitTypeReferenceExpression(o)
                checkReferenceExpression(o)
            }

            private fun checkReferenceExpression(o: TactReferenceExpressionBase) {
                val name = o.getIdentifier()?.text ?: return

                for ((imp, importedFileNames) in namesPerImport) {
                    if (name in importedFileNames) {
                        usedImports[imp.path] = imp
                    }
                }
            }
        })

        for (spec in imports) {
            if (!usedImports.contains(spec.path)) {
                importsToDelete.add(spec)
            }
        }

        return UnusedImports(importsToDelete)
    }
}
