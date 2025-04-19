package org.tonstudio.tact.lang.usages

import com.intellij.lang.HelpID
import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.ElementDescriptionUtil
import com.intellij.psi.PsiElement
import com.intellij.usageView.UsageViewLongNameLocation
import com.intellij.usageView.UsageViewShortNameLocation
import org.tonstudio.tact.lang.lexer.TactLexer
import org.tonstudio.tact.lang.psi.TactNamedElement
import org.tonstudio.tact.lang.psi.*
import org.tonstudio.tact.lang.psi.TactTokenTypes

class TactFindUsagesProvider : FindUsagesProvider {
    override fun getWordsScanner(): WordsScanner {
        return DefaultWordsScanner(
            TactLexer(),
            TactTokenTypes.IDENTIFIERS,
            TactTokenTypes.COMMENTS,
            TactTokenTypes.STRING_LITERALS,
        )
    }

    override fun canFindUsagesFor(element: PsiElement) = element is TactNamedElement

    @Suppress("UnstableApiUsage")
    override fun getHelpId(psiElement: PsiElement) = HelpID.FIND_OTHER_USAGES

    override fun getType(element: PsiElement) = when (element) {
        is TactStructDeclaration                        -> "struct"
        is TactMessageDeclaration                       -> "struct"
        is TactTraitDeclaration                         -> "trait"
        is TactContractDeclaration                      -> "contract"
        is TactFieldDefinition                          -> "field"
        is TactFunctionDeclaration                      -> "function"
        is TactConstDefinition, is TactConstDeclaration -> "constant"
        is TactVarDefinition, is TactVarDeclaration     -> "variable"
        is TactParamDefinition                          -> "parameter"
        is TactImportDeclaration                        -> "import"
        else                                            -> "declaration"
    }

    override fun getDescriptiveName(element: PsiElement): String {
        return ElementDescriptionUtil.getElementDescription(element, UsageViewLongNameLocation.INSTANCE)
    }

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String {
        return ElementDescriptionUtil.getElementDescription(element, UsageViewShortNameLocation.INSTANCE)
    }
}
