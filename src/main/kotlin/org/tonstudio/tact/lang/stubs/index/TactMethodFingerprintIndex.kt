package org.tonstudio.tact.lang.stubs.index

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndex
import com.intellij.psi.stubs.StubIndexKey
import com.intellij.util.Processor
import org.tonstudio.tact.lang.TactFileElementType
import org.tonstudio.tact.lang.psi.TactFunctionDeclaration

class TactMethodFingerprintIndex : StringStubIndexExtension<TactFunctionDeclaration>() {
    companion object {
        val KEY = StubIndexKey.createIndexKey<String, TactFunctionDeclaration>("tact.method.fingerprint")

        fun find(
            name: String, project: Project,
            scope: GlobalSearchScope?,
        ): Collection<TactFunctionDeclaration> {
            return StubIndex.getElements(KEY, name, project, scope, null, TactFunctionDeclaration::class.java)
        }

        fun process(
            name: String, project: Project,
            scope: GlobalSearchScope?, processor: Processor<TactFunctionDeclaration>,
        ): Boolean {
            return StubIndex.getInstance().processElements(
                KEY, name, project, scope, null,
                TactFunctionDeclaration::class.java, processor
            )
        }
    }

    override fun getVersion() = TactFileElementType.VERSION + 2

    override fun getKey() = KEY
}
