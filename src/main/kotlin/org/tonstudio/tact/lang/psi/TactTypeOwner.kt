package org.tonstudio.tact.lang.psi

import com.intellij.psi.ResolveState
import org.tonstudio.tact.lang.psi.types.TactTypeEx

interface TactTypeOwner : TactCompositeElement {
    fun getType(context: ResolveState?): TactTypeEx?
}
