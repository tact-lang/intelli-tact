package org.tonstudio.tact.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.UserDataHolder
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import org.tonstudio.tact.lang.psi.TactNamedElement

enum class AssignableKind {
    ASSIGNMENT,
    PLUS,
    BINARY,
}

interface TactTypeEx : UserDataHolder {
    fun name(): String
    fun qualifiedName(): String
    fun readableName(context: PsiElement, detailed: Boolean = false): String
    fun module(): String
    fun anchor(project: Project): PsiElement?
    fun accept(visitor: TactTypeVisitor)
    fun isAssignableFrom(project: Project, rhs: TactTypeEx, kind: AssignableKind): Boolean
    fun isEqual(rhs: TactTypeEx): Boolean
    fun isBuiltin(): Boolean
    fun substituteGenerics(nameMap: Map<String, TactTypeEx>): TactTypeEx
    fun ownMethodsList(project: Project): List<TactNamedElement>
    fun methodsList(project: Project, visited: MutableSet<TactTypeEx> = LinkedHashSet(5)): List<TactNamedElement>
    fun findMethod(project: Project, name: String): TactNamedElement?
    fun containingModule(project: Project): PsiDirectory?
}
