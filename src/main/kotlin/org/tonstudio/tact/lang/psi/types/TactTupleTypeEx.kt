package org.tonstudio.tact.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

class TactTupleTypeEx(val types: List<TactTypeEx>, anchor: PsiElement) : TactBaseTypeEx(anchor) {
    override fun toString() = buildString {
        append("(")
        append(types.joinToString(", ") { it.toString() })
        append(")")
    }

    override fun qualifiedName(): String = buildString {
        append("(")
        append(types.joinToString(", ") { it.qualifiedName() })
        append(")")
    }

    override fun readableName(context: PsiElement, detailed: Boolean) = buildString {
        append("(")
        append(types.joinToString(", ") { it.readableName(context) })
        append(")")
    }

    override fun module() = types.firstOrNull()?.module() ?: super.module()

    override fun isAssignableFrom(project: Project, rhs: TactTypeEx, kind: AssignableKind): Boolean {
        if (rhs !is TactTupleTypeEx) return false
        if (types.size != rhs.types.size) {
            return false
        }

        return types.zip(rhs.types).all { (lhs, rhs) -> lhs.isEqual(rhs) }
    }

    override fun isEqual(rhs: TactTypeEx): Boolean {
        return rhs is TactTupleTypeEx && types.size == rhs.types.size && types.zip(rhs.types).all { (lhs, rhs) -> lhs.isEqual(rhs) }
    }

    override fun accept(visitor: TactTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }

        for (type in types) {
            type.accept(visitor)
        }
    }

    override fun substituteGenerics(nameMap: Map<String, TactTypeEx>): TactTypeEx {
        return TactTupleTypeEx(types.map { it.substituteGenerics(nameMap) }, anchor!!)
    }
}
