package org.tonstudio.tact.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

class TactOptionTypeEx(val inner: TactTypeEx, anchor: PsiElement) : TactBaseTypeEx(anchor) {
    override fun toString() = name()

    override fun name(): String = "${inner.name()}?"

    override fun isAssignableFrom(project: Project, rhs: TactTypeEx, kind: AssignableKind): Boolean {
        if (rhs.isAny) return true
        if (rhs is TactNullTypeEx) return true
        if (rhs is TactOptionTypeEx) {
            return inner.isEqual(rhs.inner)
        }
        // Int can be assigned to Int?
        if (inner.isAssignableFrom(project, rhs, AssignableKind.ASSIGNMENT)) {
            return true
        }
        return false
    }

    override fun isEqual(rhs: TactTypeEx): Boolean {
        if (rhs !is TactOptionTypeEx) {
            return false
        }

        return inner.isEqual(rhs.inner)
    }

    override fun accept(visitor: TactTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }

        inner.accept(visitor)
    }

    override fun substituteGenerics(nameMap: Map<String, TactTypeEx>): TactTypeEx {
        return TactOptionTypeEx(inner.substituteGenerics(nameMap), anchor!!)
    }
}
