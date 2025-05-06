package org.tonstudio.tact.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import org.tonstudio.tact.configurations.TactConfiguration

class TactPrimitiveTypeEx(val name: TactPrimitiveTypes, anchor: PsiElement? = null) : TactBaseTypeEx(anchor) {
    var tlbType: String? = null

    override fun toString(): String = name()

    override fun name(): String = name.value

    override fun isAssignableFrom(project: Project, rhs: TactTypeEx, kind: AssignableKind): Boolean {
        if (rhs.isAny) return true
        if (rhs !is TactPrimitiveTypeEx) return false
        return name.value == rhs.name.value
    }

    override fun isEqual(rhs: TactTypeEx): Boolean {
        if (rhs !is TactPrimitiveTypeEx) return false
        return name.value == rhs.name.value
    }

    override fun accept(visitor: TactTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }
    }

    override fun substituteGenerics(nameMap: Map<String, TactTypeEx>): TactTypeEx = this

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TactPrimitiveTypeEx

        return name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun anchor(project: Project): PsiElement? {
        return anchor
    }

    companion object {
        val BOOL = TactPrimitiveTypeEx(TactPrimitiveTypes.BOOL)
        val INT = TactPrimitiveTypeEx(TactPrimitiveTypes.INT)
        val STRING = TactPrimitiveTypeEx(TactPrimitiveTypes.STRING)
        val CELL = TactPrimitiveTypeEx(TactPrimitiveTypes.CELL)
    }
}
