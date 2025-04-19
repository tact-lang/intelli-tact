package org.tonstudio.tact.lang.stubs.index

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndex
import com.intellij.psi.stubs.StubIndexKey
import org.tonstudio.tact.lang.TactFileElementType
import org.tonstudio.tact.lang.psi.TactFile

class TactModulesFingerprintIndex : StringStubIndexExtension<TactFile>() {
    companion object {
        val KEY = StubIndexKey.createIndexKey<String, TactFile>("tact.module.fingerprint")

        fun find(name: String, project: Project, scope: GlobalSearchScope?): Collection<TactFile> {
            return StubIndex.getElements(KEY, name, project, scope, null, TactFile::class.java)
        }
    }

    override fun getVersion() = TactFileElementType.VERSION + 2

    override fun getKey() = KEY
}
