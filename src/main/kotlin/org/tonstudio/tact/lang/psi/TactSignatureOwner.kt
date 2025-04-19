package org.tonstudio.tact.lang.psi

interface TactSignatureOwner : TactCompositeElement {
    fun getSignature(): TactSignature?
}

fun TactSignatureOwner.getBlockIfAny(): TactBlock? {
    return when (this) {
        is TactFunctionOrMethodDeclaration -> this.getBlock()
        else                               -> null
    }
}
