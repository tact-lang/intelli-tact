package org.tonstudio.tact.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

class TactNullTypeEx private constructor() : TactBaseTypeEx() {
    override fun toString(): String = "null"

    override fun qualifiedName(): String = "null"

    override fun readableName(context: PsiElement, detailed: Boolean): String = "null"

    override fun isAssignableFrom(project: Project, rhs: TactTypeEx, kind: AssignableKind): Boolean =
        rhs is TactNullTypeEx

    override fun isEqual(rhs: TactTypeEx): Boolean = rhs is TactNullTypeEx

    override fun accept(visitor: TactTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }
    }

    override fun substituteGenerics(nameMap: Map<String, TactTypeEx>): TactTypeEx = this

    companion object {
        val INSTANCE = TactNullTypeEx()
    }
}
