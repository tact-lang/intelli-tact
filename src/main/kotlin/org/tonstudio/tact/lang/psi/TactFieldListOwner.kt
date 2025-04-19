package org.tonstudio.tact.lang.psi

interface TactFieldListOwner {
    /**
     * list of fields declared in this element and in embedded elements.
     */
    val fieldList: List<TactFieldDefinition>
}
