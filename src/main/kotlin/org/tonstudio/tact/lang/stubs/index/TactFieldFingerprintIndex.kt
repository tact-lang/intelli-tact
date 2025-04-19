package org.tonstudio.tact.lang.stubs.index

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndex
import com.intellij.psi.stubs.StubIndexKey
import org.tonstudio.tact.lang.TactFileElementType
import org.tonstudio.tact.lang.psi.TactFieldDefinition

class TactFieldFingerprintIndex : StringStubIndexExtension<TactFieldDefinition>() {
    companion object {
        val KEY = StubIndexKey.createIndexKey<String, TactFieldDefinition>("tact.fields.fingerprint")

        fun find(
            name: String, project: Project,
            scope: GlobalSearchScope?,
        ): Collection<TactFieldDefinition> {
            return StubIndex.getElements(KEY, name, project, scope, null, TactFieldDefinition::class.java)
        }

    }

    override fun getVersion() = TactFileElementType.VERSION + 2

    override fun getKey() = KEY
}
