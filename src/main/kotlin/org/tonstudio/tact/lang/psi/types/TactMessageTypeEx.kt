package org.tonstudio.tact.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.tonstudio.tact.lang.psi.TactMessageDeclaration
import org.tonstudio.tact.lang.stubs.index.TactClassLikeIndex

open class TactMessageTypeEx(private val name: String, anchor: PsiElement?) : TactResolvableTypeEx<TactMessageDeclaration>(anchor),
    TactImportableType {

    override fun toString() = name

    override fun name() = name

    override fun isAssignableFrom(project: Project, rhs: TactTypeEx, kind: AssignableKind): Boolean {
        if (rhs.isAny) return true
        if (rhs is TactMessageTypeEx) {
            return this.name() == rhs.name()
        }
        return false
    }

    override fun isEqual(rhs: TactTypeEx): Boolean {
        return rhs is TactMessageTypeEx && name() == rhs.name()
    }

    override fun accept(visitor: TactTypeVisitor) {
        visitor.enter(this)
    }

    override fun substituteGenerics(nameMap: Map<String, TactTypeEx>): TactTypeEx = this

    override fun resolveImpl(project: Project): TactMessageDeclaration? {
        val anchor = anchor(project)
        if (anchor != null && anchor.isValid) {
            val parent = anchor.parent
            if (parent is TactMessageDeclaration && parent.isValid) {
                return parent
            }
        }

        val variants = TactClassLikeIndex.find(name(), project, null)
        if (variants.size == 1) {
            return variants.first() as? TactMessageDeclaration
        }

        return prioritize(containingFile, variants) as? TactMessageDeclaration
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TactMessageTypeEx

        return name == other.name
    }

    override fun hashCode(): Int = name.hashCode()
}
