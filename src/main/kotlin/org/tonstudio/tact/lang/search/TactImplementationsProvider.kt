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
import org.tonstudio.tact.lang.psi.TactConstDefinition
import org.tonstudio.tact.lang.psi.TactFunctionDeclaration
import org.tonstudio.tact.lang.psi.TactNamedElement
import org.tonstudio.tact.lang.psi.TactTraitDeclaration

class TactImplementationsProvider : LineMarkerProviderDescriptor() {
    override fun getName() = "Go to implementations"

    override fun getIcon() = AllIcons.Gutter.ImplementedMethod

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
    ): Boolean {
        if (element !is LeafPsiElement || element.elementType != TactTypes.IDENTIFIER) return true

        val func = element.parent as? TactFunctionDeclaration
        if (func != null) {
            return processFunction(func, element, result)
        }

        val constant = element.parent as? TactConstDefinition
        if (constant != null) {
            return processConstant(constant, element, result)
        }

        return false
    }

    private fun processFunction(
        func: TactFunctionDeclaration,
        element: LeafPsiElement,
        result: MutableCollection<in LineMarkerInfo<*>?>,
    ): Boolean {
        val owner = func.getOwner() ?: return true
        if (owner !is TactTraitDeclaration) return true

        if (!hasImplementations(func, TactFunctionImplementationsSearch())) return true

        val (action, what) = if (!func.isAbstract) "Overrides" to "override" else "Implementations" to "implemented"

        val info = createInfo(element, { event, elem ->
            val named = elem.parent as? TactNamedElement ?: return@createInfo

            showPopup(
                "$action of $name",
                { size -> "Method '$name' $what in $size types" },
                event,
                DefinitionsScopedSearch.SearchParameters(named),
                methodRenderer(named),
                TactFunctionImplementationsSearch(),
            )
        }, "Go to $action", AllIcons.Gutter.ImplementedMethod, IdeActions.ACTION_GOTO_IMPLEMENTATION)

        result.add(info)
        return false
    }

    private fun processConstant(
        constant: TactConstDefinition,
        element: LeafPsiElement,
        result: MutableCollection<in LineMarkerInfo<*>?>,
    ): Boolean {
        val owner = constant.getOwner() ?: return true
        if (owner !is TactTraitDeclaration) return true

        if (!hasImplementations(constant, TactConstantImplementationsSearch())) return true

        val (action, what) = "Overrides" to "override"

        val info = createInfo(element, { event, elem ->
            val named = elem.parent as? TactNamedElement ?: return@createInfo

            showPopup(
                "$action of $name",
                { size -> "Constant '$name' $what in $size types" },
                event,
                DefinitionsScopedSearch.SearchParameters(named),
                methodRenderer(named),
                TactConstantImplementationsSearch(),
            )
        }, "Go to $action", AllIcons.Gutter.ImplementedMethod, IdeActions.ACTION_GOTO_IMPLEMENTATION)

        result.add(info)
        return false
    }
}
