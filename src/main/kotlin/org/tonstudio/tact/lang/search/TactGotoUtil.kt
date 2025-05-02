package org.tonstudio.tact.lang.search

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.NavigateAction
import com.intellij.codeInsight.daemon.impl.PsiElementListNavigator
import com.intellij.codeInsight.navigation.GotoImplementationHandler
import com.intellij.ide.util.DefaultPsiElementCellRenderer
import com.intellij.ide.util.PsiElementListCellRenderer
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.keymap.KeymapUtil
import com.intellij.openapi.progress.ProgressManager
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.search.PsiElementProcessor
import com.intellij.psi.search.searches.DefinitionsScopedSearch
import com.intellij.util.FunctionUtil
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet
import org.tonstudio.tact.lang.psi.TactFunctionDeclaration
import java.awt.event.MouseEvent
import javax.swing.Icon
import javax.swing.JComponent

fun <T : PsiElement> showPopup(
    findUsagesTitle: String,
    popupTitleCb: (Int) -> String,
    event: MouseEvent?,
    param: DefinitionsScopedSearch.SearchParameters,
    renderer: PsiElementListCellRenderer<PsiElement>,
    search: QueryExecutorBase<T, in DefinitionsScopedSearch.SearchParameters>,
) {
    val collector = PsiElementProcessor.CollectElementsWithLimit(-1, ReferenceOpenHashSet())
    val progressTitle = "Searching for $findUsagesTitle..."
    val component = event?.component as? JComponent

    val findAny = ProgressManager.getInstance().runProcessWithProgressSynchronously({
        search.execute(param) { collector.execute(it) }
    }, progressTitle, true, param.project, component)

    if (!findAny) {
        return
    }

    val elements = collector.collection
    val popupTitle = popupTitleCb(elements.size)
    val targets = elements.filterIsInstance<NavigatablePsiElement>().toTypedArray()
    if (event != null) {
        PsiElementListNavigator.openTargets(event, targets, popupTitle, findUsagesTitle, renderer, null)
    } else {
        val editor = FileEditorManager.getInstance(param.project).selectedTextEditor ?: return
        PsiElementListNavigator.openTargets(editor, targets, popupTitle, findUsagesTitle, renderer, null)
    }
}

fun methodRenderer(context: PsiElement?): DefaultPsiElementCellRenderer {
    return if (context == null) DefaultPsiElementCellRenderer() else object : DefaultPsiElementCellRenderer() {
        override fun getComparator(): Comparator<PsiElement> =
            GotoImplementationHandler.projectElementsFirst(context.project).thenComparing(super.getComparator())

        override fun getElementText(element: PsiElement?): String {
            val method = element as? TactFunctionDeclaration ?: return super.getElementText(element)
            val owner = method.getOwner() ?: return method.name
            return "${owner.name}.${method.name}"
        }
    }
}

fun <T : PsiElement> createInfo(
    element: T,
    navigationHandler: GutterIconNavigationHandler<T>,
    text: String,
    icon: Icon,
    actionName: String,
): LineMarkerInfo<T> {
    val tooltip = buildTooltipText(text, actionName)

    val info = LineMarkerInfo(
        element, element.textRange, icon,
        FunctionUtil.constant(tooltip),
        navigationHandler,
        GutterIconRenderer.Alignment.RIGHT
    ) { tooltip }

    return NavigateAction.setNavigateAction(info, text, actionName)
}

private fun buildTooltipText(title: String, actionId: String): String {
    val shortcut = ActionManager.getInstance().getAction(actionId).shortcutSet.shortcuts.firstOrNull()
    val shortcutText = if (shortcut == null) "" else " or press ${KeymapUtil.getShortcutText(shortcut)}"
    return """
        <html>$title<br>
            <div style='margin-top: 5px'>
                <font size='2'>Click $shortcutText to navigate</font>
            </div
        ></html>
    """.trimIndent()
}
