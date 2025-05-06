package org.tonstudio.tact.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

class TactBouncedTypeEx(val inner: TactTypeEx, anchor: PsiElement) : TactBaseTypeEx(anchor) {
    override fun toString() = name()

     override fun name(): String = buildString {
        append("bounced<")
        append(inner.name())
        append(">")
    }

    override fun isAssignableFrom(project: Project, rhs: TactTypeEx, kind: AssignableKind): Boolean {
        if (rhs.isAny) return true
        if (rhs is TactBouncedTypeEx) {
            return inner.isEqual(rhs.inner)
        }
        return false
    }

    override fun isEqual(rhs: TactTypeEx): Boolean {
        return rhs is TactBouncedTypeEx && inner.isEqual(rhs.inner)
    }

    override fun accept(visitor: TactTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }

        inner.accept(visitor)
    }

    override fun substituteGenerics(nameMap: Map<String, TactTypeEx>): TactTypeEx {
        return TactBouncedTypeEx(inner.substituteGenerics(nameMap), anchor!!)
    }
}
