package org.tonstudio.tact.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.tonstudio.tact.lang.psi.TactStructDeclaration
import org.tonstudio.tact.lang.stubs.index.TactStructIndex

open class TactStructTypeEx(private val name: String, anchor: PsiElement?) :
    TactResolvableTypeEx<TactStructDeclaration>(anchor), TactImportableTypeEx {

    override fun toString() = name

    override fun qualifiedName(): String {
        if (moduleName.isEmpty()) {
            return name
        }
        return "$moduleName.$name"
    }

    override fun readableName(context: PsiElement, detailed: Boolean) =
        qualifiedName()

    override fun isAssignableFrom(project: Project, rhs: TactTypeEx, kind: AssignableKind): Boolean {
        if (rhs.isAny) return true
        if (rhs is TactStructTypeEx) {
            return this.qualifiedName() == rhs.qualifiedName()
        }
        return false
    }

    override fun isEqual(rhs: TactTypeEx): Boolean {
        return rhs is TactStructTypeEx && qualifiedName() == rhs.qualifiedName()
    }

    override fun accept(visitor: TactTypeVisitor) {
        visitor.enter(this)
    }

    override fun substituteGenerics(nameMap: Map<String, TactTypeEx>): TactTypeEx = this

    override fun resolveImpl(project: Project): TactStructDeclaration? {
        val anchor = anchor(project)
        if (anchor != null && anchor.isValid) {
            val parent = anchor.parent
            if (parent is TactStructDeclaration && parent.isValid) {
                return parent
            }
        }

        val variants = TactStructIndex.find(qualifiedName(), project)
        if (variants.size == 1) {
            return variants.first()
        }

        return prioritize(containingFile, variants) as? TactStructDeclaration
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TactStructTypeEx

        return name == other.name
    }

    override fun hashCode(): Int = name.hashCode()
}
