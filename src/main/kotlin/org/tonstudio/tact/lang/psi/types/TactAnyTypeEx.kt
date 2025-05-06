package org.tonstudio.tact.lang.psi.types

import com.intellij.openapi.project.Project

class TactAnyTypeEx private constructor(): TactBaseTypeEx() {
    override fun toString(): String = name()

    override fun name(): String  = "any"

    override fun isAssignableFrom(project: Project, rhs: TactTypeEx, kind: AssignableKind): Boolean = true

    override fun isEqual(rhs: TactTypeEx): Boolean = true

    override fun accept(visitor: TactTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }
    }

    override fun substituteGenerics(nameMap: Map<String, TactTypeEx>): TactTypeEx = this

    companion object {
        val INSTANCE = TactAnyTypeEx()
    }
}
