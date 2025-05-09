package org.tonstudio.tact.lang.psi.types

import com.intellij.psi.PsiElement
import org.tonstudio.tact.lang.psi.TactConstDeclaration
import org.tonstudio.tact.lang.psi.TactFieldDefinition
import org.tonstudio.tact.lang.psi.TactFunctionDeclaration
import org.tonstudio.tact.lang.psi.TactNamedElement
import org.tonstudio.tact.lang.psi.TactStorageMembersOwner
import org.tonstudio.tact.lang.psi.constants
import org.tonstudio.tact.lang.psi.fields
import org.tonstudio.tact.lang.psi.methods

abstract class StorageMembersOwnerTy<T : TactNamedElement>(private val name: String, anchor: PsiElement?) :
    TactResolvableTypeEx<T>(anchor) {

    override fun toString() = name()

    override fun name() = name

    fun ownMethods(): List<TactFunctionDeclaration> {
        return owner()?.getMethodsList() ?: emptyList()
    }

    fun ownFields(): List<TactFieldDefinition> {
        return owner()?.getFieldList() ?: emptyList()
    }

    fun ownConstants(): List<TactConstDeclaration> {
        return owner()?.getConstantsList() ?: emptyList()
    }

    fun methods(): List<TactFunctionDeclaration> {
        return owner()?.methods() ?: emptyList()
    }

    fun fields(): List<TactFieldDefinition> {
        return owner()?.fields() ?: emptyList()
    }

    fun constants(): List<TactConstDeclaration> {
        return owner()?.constants() ?: emptyList()
    }

    private fun owner(): TactStorageMembersOwner? {
        if (anchor is TactStorageMembersOwner) {
            return anchor
        }
        return null
    }
}
