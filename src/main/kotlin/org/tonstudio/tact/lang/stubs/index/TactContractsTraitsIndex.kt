package org.tonstudio.tact.lang.stubs.index

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndex
import com.intellij.psi.stubs.StubIndexKey
import com.intellij.util.Processor
import org.tonstudio.tact.lang.TactFileElementType
import org.tonstudio.tact.lang.psi.TactNamedElement

class TactContractsTraitsIndex : StringStubIndexExtension<TactNamedElement>() {
    companion object {
        val KEY = StubIndexKey.createIndexKey<String, TactNamedElement>("tact.contracts.or.traits")

        fun find(
            fqn: String,
            project: Project,
            scope: GlobalSearchScope?,
        ): Collection<TactNamedElement> {
            return StubIndex.getElements(KEY, fqn, project, scope, TactNamedElement::class.java)
        }

        fun processAll(project: Project, processor: Processor<TactNamedElement>) {
            val keys = StubIndex.getInstance().getAllKeys(KEY, project)
            for (key in keys) {
                StubIndex.getInstance().processElements(
                    KEY,
                    key,
                    project,
                    GlobalSearchScope.allScope(project),
                    null,
                    TactNamedElement::class.java,
                    processor,
                )
            }
        }
    }

    override fun getVersion() = TactFileElementType.VERSION + 3

    override fun getKey() = KEY
}
