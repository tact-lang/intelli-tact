package org.tonstudio.tact.lang.psi

import org.tonstudio.tact.lang.psi.types.TactBaseTypeEx.Companion.toEx
import org.tonstudio.tact.lang.psi.types.TactTraitTypeEx
import org.tonstudio.tact.lang.stubs.index.TactNamesIndex

interface TactStorageMembersOwner : TactCompositeElement {
    val name: String?

    fun getWithClause(): TactWithClause?
    fun getFieldList(): List<TactFieldDefinition>
    fun getMethodsList(): List<TactFunctionDeclaration>
    fun getConstantsList(): List<TactConstDefinition>
    fun getInheritedTraits(): List<TactTraitDeclaration>
}

fun TactStorageMembersOwner.methods(): List<TactFunctionDeclaration> {
    val own = getMethodsList()
    val inherited = inheritTraits().flatMap { it.getMethodsList() }
    return own + inherited
}

fun TactStorageMembersOwner.fields(): List<TactFieldDefinition> {
    val own = getFieldList()
    val inherited = inheritTraits().flatMap { it.getFieldList() }
    return own + inherited
}

fun TactStorageMembersOwner.inheritTraits(): List<TactTraitType> {
    if (this.name() == "BaseTrait") {
        return emptyList()
    }

    val baseTrait = TactNamesIndex.find("BaseTrait", project, null).firstOrNull() as? TactTraitDeclaration ?: return emptyList()

    val inheritedTraitsList = this.getWithClause()?.typeList ?: return emptyList()
    val inheritedTraits = inheritedTraitsList
        .map { it.toEx() }
        .filterIsInstance<TactTraitTypeEx>()
        .mapNotNull { it.resolve(project)?.traitType }

    return inheritedTraits + baseTrait.traitType
}

fun TactStorageMembersOwner.name(): String {
    val parent = this.parent
    if (parent is TactNamedElement) {
        return parent.name ?: ""
    }
    return ""
}