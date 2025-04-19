package org.tonstudio.tact.lang.psi

import org.tonstudio.tact.lang.doc.psi.TactDocComment

interface TactDocumentationOwner : TactCompositeElement {
    fun getDocumentation(): TactDocComment?
}
