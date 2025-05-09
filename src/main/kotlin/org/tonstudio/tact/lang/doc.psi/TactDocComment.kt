package org.tonstudio.tact.lang.doc.psi

import com.intellij.psi.PsiDocCommentBase
import org.tonstudio.tact.lang.psi.TactNamedElement

interface TactDocComment : PsiDocCommentBase {
    override fun getOwner(): TactNamedElement?

    val codeFences: List<TactDocCodeFence>
    val linkDefinitions: List<TactDocLinkDefinition>
    val linkReferenceMap: Map<String, TactDocLinkDefinition>
}
