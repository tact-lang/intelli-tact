package org.tonstudio.tact.lang.psi.impl

import com.intellij.openapi.util.Key
import com.intellij.openapi.util.KeyWithDefaultValue
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiPolyVariantReferenceBase
import org.tonstudio.tact.lang.psi.TactReferenceExpressionBase

abstract class TactReferenceBase<T : TactReferenceExpressionBase>(element: T, range: TextRange?) :
    PsiPolyVariantReferenceBase<T>(element, range) {

    protected val project = element.project

    companion object {
        val ACTUAL_NAME = Key.create<String>("ACTUAL_NAME")
        val SEARCH_NAME = Key.create<String>("SEARCH_NAME")
        val LOCAL_RESOLVE = KeyWithDefaultValue.create("LOCAL_RESOLVE", false)
        val NOT_PROCESS_METHODS = KeyWithDefaultValue.create("NOT_PROCESS_METHODS", false)
    }
}
