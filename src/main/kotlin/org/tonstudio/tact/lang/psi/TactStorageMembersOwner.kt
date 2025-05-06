package org.tonstudio.tact.lang.psi

interface TactStorageMembersOwner : TactCompositeElement {
    fun getWithClause(): TactWithClause?
    fun getFieldList(): List<TactFieldDefinition>
    fun getMethodsList(): List<TactFunctionDeclaration>
    fun getConstantsList(): List<TactConstDeclaration>
    fun getInheritedTraits(): List<TactTraitDeclaration>
}

fun TactStorageMembersOwner.methods(): List<TactFunctionDeclaration> {
    val own = getMethodsList()
    val inherited = getInheritedTraits().flatMap { it.traitType.getMethodsList() }
    return own + inherited
}

fun TactStorageMembersOwner.fields(): List<TactFieldDefinition> {
    val own = getFieldList()
    val inherited = getInheritedTraits().flatMap { it.traitType.getFieldList() }
    return own + inherited
}

fun TactStorageMembersOwner.constants(): List<TactConstDeclaration> {
    val own = getConstantsList()
    val inherited = getInheritedTraits().flatMap { it.traitType.getConstantsList() }
    return own + inherited
}

fun TactStorageMembersOwner.name(): String {
    val parent = this.parent
    if (parent is TactNamedElement) {
        return parent.name ?: ""
    }
    return ""
}