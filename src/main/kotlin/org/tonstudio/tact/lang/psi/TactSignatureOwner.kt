package org.tonstudio.tact.lang.psi

interface TactSignatureOwner : TactCompositeElement {
    fun getSignature(): TactSignature?
}

fun TactSignatureOwner.getBlockIfAny(): TactBlock? {
    return when (this) {
        is TactFunctionDeclaration -> this.getBlock()
        else                       -> null
    }
}
