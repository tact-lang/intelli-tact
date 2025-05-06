package org.tonstudio.tact.lang.search

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.IdeActions
import com.intellij.openapi.progress.ProgressManager
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.search.searches.DefinitionsScopedSearch
import org.tonstudio.tact.lang.TactTypes
import org.tonstudio.tact.lang.psi.TactConstDeclaration
import org.tonstudio.tact.lang.psi.TactFunctionDeclaration
import java.awt.event.MouseEvent

class TactSuperMarkerProvider : LineMarkerProviderDescriptor() {
    override fun getName() = "Go to parent"

    override fun getIcon() = AllIcons.Gutter.OverridingMethod

    override fun isEnabledByDefault() = true

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<PsiElement>? = null

    override fun collectSlowLineMarkers(elements: List<PsiElement>, result: MutableCollection<in LineMarkerInfo<*>?>) {
        for (element in elements) {
            ProgressManager.checkCanceled()
            processElement(element, result)
        }
    }

    private fun processElement(
        element: PsiElement,
        result: MutableCollection<in LineMarkerInfo<*>?>,
    ) {
        if (element !is LeafPsiElement || element.elementType != TactTypes.IDENTIFIER) return

        val func = element.parent as? TactFunctionDeclaration

        if (func != null && hasSuperMethod(func)) {
            result.add(
                createInfo(
                    element,
                    { e, identifier -> showSuperMethodPopup(e, identifier) },
                    "Go to Parent Method",
                    AllIcons.Gutter.OverridingMethod,
                    IdeActions.ACTION_GOTO_SUPER,
                )
            )
        }

        val constant = element.parent as? TactConstDeclaration
        if (constant != null && hasSuperConstant(constant)) {
            result.add(
                createInfo(
                    element,
                    { e, identifier -> showSuperConstantPopup(e, identifier) },
                    "Go to Parent Constant",
                    AllIcons.Gutter.OverridingMethod,
                    IdeActions.ACTION_GOTO_SUPER,
                )
            )
        }
    }
}

fun showSuperMethodPopup(event: MouseEvent?, method: TactFunctionDeclaration) {
    val name = method.name
    showPopup(
        "Implement method $name",
        {
            "Method '$name' Overrides Method of Trait"
        },
        event,
        DefinitionsScopedSearch.SearchParameters(method),
        methodRenderer(method),
        TactSuperMethodSearch(),
    )
}

private fun showSuperMethodPopup(e: MouseEvent?, identifier: PsiElement) {
    val method = identifier.parent as? TactFunctionDeclaration ?: return
    showSuperMethodPopup(e, method)
}

fun showSuperConstantPopup(event: MouseEvent?, constant: TactConstDeclaration) {
    val name = constant.name
    showPopup(
        "Implement constant $name",
        {
            "Constant '$name' Overrides Constant of Trait"
        },
        event,
        DefinitionsScopedSearch.SearchParameters(constant),
        methodRenderer(constant),
        TactSuperConstantSearch(),
    )
}

private fun showSuperConstantPopup(e: MouseEvent?, identifier: PsiElement) {
    val method = identifier.parent as? TactConstDeclaration ?: return
    showSuperConstantPopup(e, method)
}
