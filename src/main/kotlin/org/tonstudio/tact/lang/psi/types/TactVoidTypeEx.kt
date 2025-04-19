package org.tonstudio.tact.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

class TactVoidTypeEx private constructor() : TactBaseTypeEx() {
    override fun toString(): String = "void"

    override fun qualifiedName(): String  = "void"

    override fun readableName(context: PsiElement, detailed: Boolean): String = "void"

    override fun isAssignableFrom(project: Project, rhs: TactTypeEx, kind: AssignableKind): Boolean {
        return rhs.isAny
    }

    override fun isEqual(rhs: TactTypeEx): Boolean {
        return rhs is TactVoidTypeEx
    }

    override fun accept(visitor: TactTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }
    }

    override fun substituteGenerics(nameMap: Map<String, TactTypeEx>): TactTypeEx = this

    companion object {
        val INSTANCE = TactVoidTypeEx()
    }
}
