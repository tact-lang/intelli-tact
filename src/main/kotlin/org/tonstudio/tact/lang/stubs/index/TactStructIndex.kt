package org.tonstudio.tact.lang.stubs.index

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndex
import com.intellij.psi.stubs.StubIndexKey
import org.tonstudio.tact.lang.TactFileElementType
import org.tonstudio.tact.lang.psi.TactStructDeclaration

class TactStructIndex : StringStubIndexExtension<TactStructDeclaration>() {
    companion object {
        val KEY = StubIndexKey.createIndexKey<String, TactStructDeclaration>("tact.struct")

        fun find(
            name: String,
            project: Project,
            scope: GlobalSearchScope? = GlobalSearchScope.allScope(project),
        ): Collection<TactStructDeclaration> {
            return StubIndex.getElements(KEY, name, project, scope, TactStructDeclaration::class.java)
        }
    }

    override fun getVersion() = TactFileElementType.VERSION + 3

    override fun getKey() = KEY
}
