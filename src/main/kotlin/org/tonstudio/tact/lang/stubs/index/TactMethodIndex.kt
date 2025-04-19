package org.tonstudio.tact.lang.stubs.index

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndex
import com.intellij.psi.stubs.StubIndexKey
import com.intellij.util.Processor
import com.intellij.util.indexing.IdFilter
import org.tonstudio.tact.lang.TactFileElementType
import org.tonstudio.tact.lang.psi.TactNamedElement

class TactMethodIndex : StringStubIndexExtension<TactNamedElement>() {
    companion object {
        val KEY = StubIndexKey.createIndexKey<String, TactNamedElement>("tact.method")
        
        fun find(
            name: String, project: Project,
            scope: GlobalSearchScope?,
        ): Collection<TactNamedElement> {
            return StubIndex.getElements(KEY, name, project, scope, TactNamedElement::class.java)
        }

        fun process(
            name: String,
            project: Project,
            scope: GlobalSearchScope?,
            idFilter: IdFilter?,
            processor: Processor<TactNamedElement>,
        ): Boolean {
            return StubIndex.getInstance().processElements(
                KEY, name, project, scope, idFilter,
                TactNamedElement::class.java, processor
            )
        }
    }

    override fun getVersion() = TactFileElementType.VERSION + 2

    override fun getKey() = KEY
}
