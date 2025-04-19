package org.tonstudio.tact.lang.stubs.index

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.findPsiFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndex
import com.intellij.psi.stubs.StubIndexKey
import com.intellij.util.indexing.IdFilter
import org.tonstudio.tact.lang.TactFileElementType
import org.tonstudio.tact.lang.psi.TactFile
import org.tonstudio.tact.utils.isTactFile

class TactModulesIndex : StringStubIndexExtension<TactFile>() {
    companion object {
        val KEY = StubIndexKey.createIndexKey<String, TactFile>("tact.modules")

        fun findDir(
            name: String,
            project: Project,
            scope: GlobalSearchScope,
        ): Pair<TactFile, PsiDirectory>? {
            val iter = StubIndex.getInstance().getContainingFilesIterator(KEY, name, project, scope)
            while (iter.hasNext()) {
                val file = iter.next()
                if (file.isTactFile) {
                    val psiFile = file.findPsiFile(project)
                    if (psiFile is TactFile) {
                        val directory = psiFile.containingDirectory ?: continue
                        return psiFile to directory
                    }
                }
            }

            return null
        }

        fun get(
            name: String,
            project: Project,
            scope: GlobalSearchScope?,
            idFilter: IdFilter? = null,
        ): Collection<TactFile> {
            return StubIndex.getElements(KEY, name, project, scope, idFilter, TactFile::class.java)
        }

        fun getSubmodulesOfAnyDepth(project: Project, module: String): List<TactFile> {
            val result = mutableListOf<TactFile>()
            val keys = StubIndex.getInstance().getAllKeys(KEY, project)
            for (key in keys) {
                if (!key.startsWith(module)) {
                    continue
                }

                val els = StubIndex.getElements(
                    KEY,
                    key,
                    project,
                    GlobalSearchScope.allScope(project),
                    IdFilter.getProjectIdFilter(project, true),
                    TactFile::class.java
                )
                result.addAll(els)
            }

            return result.toSet().toList()
        }

        fun getAll(project: Project): List<TactFile> {
            val result = mutableListOf<TactFile>()
            for (key in StubIndex.getInstance().getAllKeys(KEY, project)) {
                val els = StubIndex.getElements(
                    KEY,
                    key,
                    project,
                    null,
                    null,
                    TactFile::class.java
                )
                result.addAll(els)
            }

            return result
        }
    }

    override fun getVersion() = TactFileElementType.VERSION + 2

    override fun getKey() = KEY
}
