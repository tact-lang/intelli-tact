package org.tonstudio.tact.lang.usages

import com.intellij.codeInsight.highlighting.HighlightUsagesDescriptionLocation
import com.intellij.psi.ElementDescriptionLocation
import com.intellij.psi.ElementDescriptionProvider
import com.intellij.psi.PsiElement
import com.intellij.usageView.UsageViewLongNameLocation
import com.intellij.usageView.UsageViewShortNameLocation
import org.tonstudio.tact.lang.psi.TactNamedElement

class TactDescriptionProvider : ElementDescriptionProvider {
    override fun getElementDescription(o: PsiElement, location: ElementDescriptionLocation): String? {
        if (location === UsageViewShortNameLocation.INSTANCE ||
            location === UsageViewLongNameLocation.INSTANCE ||
            location === HighlightUsagesDescriptionLocation.INSTANCE
        ) {
            if (o is TactNamedElement) return o.name
        }
        return null
    }
}
