package org.tonstudio.tact.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.tonstudio.tact.lang.psi.*
import org.tonstudio.tact.lang.stubs.index.TactNamesIndex

open class TactContractTypeEx(private val name: String, anchor: PsiElement?) : StorageMembersOwnerTy<TactContractDeclaration>(name, anchor),
    TactImportableType {

    override fun isAssignableFrom(project: Project, rhs: TactTypeEx, kind: AssignableKind): Boolean {
        if (rhs.isAny) return true
        if (rhs is TactContractTypeEx) {
            return this.name() == rhs.name()
        }
        return false
    }

    override fun isEqual(rhs: TactTypeEx): Boolean {
        return rhs is TactContractTypeEx && name() == rhs.name()
    }

    override fun accept(visitor: TactTypeVisitor) {
        visitor.enter(this)
    }

    override fun substituteGenerics(nameMap: Map<String, TactTypeEx>): TactTypeEx = this

    override fun resolveImpl(project: Project): TactContractDeclaration? {
        val anchor = anchor(project)
        if (anchor != null && anchor.isValid) {
            val parent = anchor.parent
            if (parent is TactContractDeclaration && parent.isValid) {
                return parent
            }
        }

        val variants = TactNamesIndex.find(name(), project, null)
        if (variants.size == 1) {
            return variants.first() as? TactContractDeclaration
        }

        return prioritize(containingFile, variants) as? TactContractDeclaration
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TactContractDeclaration

        return name == other.name
    }

    override fun hashCode(): Int = name.hashCode()
}
