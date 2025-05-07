package org.tonstudio.tact.lang.psi

import com.intellij.psi.PsiElement

/**
 * A common interface for fields and parameters needed to handle both
 * contract parameters (effectively fields) and initialization parameters (simple parameters).
 */
interface TactFieldOrParameter : TactCompositeElement {
    val identifier: PsiElement?
    val type: TactType?
}
