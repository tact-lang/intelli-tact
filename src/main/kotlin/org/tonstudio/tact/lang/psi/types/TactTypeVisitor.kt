package org.tonstudio.tact.lang.psi.types

interface TactTypeVisitor {
    fun enter(type: TactTypeEx): Boolean
}
