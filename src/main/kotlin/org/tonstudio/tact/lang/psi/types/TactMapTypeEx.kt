package org.tonstudio.tact.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

class TactMapTypeEx(val key: TactTypeEx, val value: TactTypeEx, anchor: PsiElement) : TactBaseTypeEx(anchor) {
    override fun toString() = name()

   override fun name(): String = buildString {
        append("map<")
        append(key.name())
        append(", ")
        append(value.name())
        append(">")
    }

    override fun isAssignableFrom(project: Project, rhs: TactTypeEx, kind: AssignableKind): Boolean {
        if (rhs.isAny) return true
        if (rhs is TactMapTypeEx) {
            return key.isEqual(rhs.key) && value.isEqual(rhs.value)
        }
        return false
    }

    override fun isEqual(rhs: TactTypeEx): Boolean {
        return rhs is TactMapTypeEx && key.isEqual(rhs.key) && value.isEqual(rhs.value)
    }

    override fun accept(visitor: TactTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }

        key.accept(visitor)
        value.accept(visitor)
    }

    override fun substituteGenerics(nameMap: Map<String, TactTypeEx>): TactTypeEx {
        return TactMapTypeEx(key.substituteGenerics(nameMap), value.substituteGenerics(nameMap), anchor!!)
    }
}
