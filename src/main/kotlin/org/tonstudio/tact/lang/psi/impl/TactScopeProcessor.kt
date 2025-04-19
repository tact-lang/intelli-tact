package org.tonstudio.tact.lang.psi.impl

import com.intellij.psi.scope.PsiScopeProcessor

abstract class TactScopeProcessor : PsiScopeProcessor {
    open fun isCompletion(): Boolean = false
}
