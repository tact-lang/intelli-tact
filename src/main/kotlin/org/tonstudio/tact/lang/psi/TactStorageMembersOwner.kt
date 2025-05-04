package org.tonstudio.tact.lang.psi

interface TactStorageMembersOwner : TactCompositeElement {
    fun getWithClause(): TactWithClause?
    fun getFieldList(): List<TactFieldDefinition>
    fun getMethodsList(): List<TactFunctionDeclaration>
    fun getConstantsList(): List<TactConstDefinition>
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

fun TactStorageMembersOwner.name(): String {
    val parent = this.parent
    if (parent is TactNamedElement) {
        return parent.name ?: ""
    }
    return ""
}