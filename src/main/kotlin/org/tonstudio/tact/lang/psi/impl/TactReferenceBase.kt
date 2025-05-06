package org.tonstudio.tact.lang.psi.impl

import com.intellij.openapi.util.Key
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiPolyVariantReferenceBase
import org.tonstudio.tact.lang.psi.TactReferenceExpressionBase

abstract class TactReferenceBase<T : TactReferenceExpressionBase>(element: T) : PsiPolyVariantReferenceBase<T>(element) {
    protected val project = element.project

    final override fun calculateDefaultRangeInElement(): TextRange {
        val anchor = element.getIdentifier()
        check(anchor.parent === element)
        return TextRange.from(anchor.startOffsetInParent, anchor.textLength)
    }

    companion object {
        val ACTUAL_NAME = Key.create<String>("ACTUAL_NAME")
        val SEARCH_NAME = Key.create<String>("SEARCH_NAME")
    }
}
