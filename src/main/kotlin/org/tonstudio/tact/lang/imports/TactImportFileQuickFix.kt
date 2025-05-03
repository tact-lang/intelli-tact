package org.tonstudio.tact.lang.imports

import com.intellij.codeInsight.FileModificationService
import com.intellij.codeInsight.daemon.impl.DaemonListeners
import com.intellij.codeInsight.daemon.impl.ShowAutoImportPass
import com.intellij.codeInsight.hint.HintManager
import com.intellij.codeInsight.intention.HighPriorityAction
import com.intellij.codeInspection.HintAction
import com.intellij.codeInspection.LocalQuickFixAndIntentionActionOnPsiElement
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiReference
import com.intellij.ui.SimpleColoredComponent
import com.intellij.ui.SimpleTextAttributes
import com.intellij.ui.components.JBList
import com.intellij.util.IncorrectOperationException
import com.intellij.util.ThreeState
import com.intellij.util.ui.JBUI
import org.tonstudio.tact.ide.ui.Icons
import org.tonstudio.tact.lang.psi.TactFile
import org.tonstudio.tact.lang.psi.TactReferenceExpression
import org.tonstudio.tact.lang.psi.TactTypeReferenceExpression
import org.tonstudio.tact.lang.psi.impl.TactReference
import org.tonstudio.tact.lang.stubs.index.TactNamesIndex
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.pathString
import kotlin.io.path.relativeTo

class TactImportFileQuickFix : LocalQuickFixAndIntentionActionOnPsiElement, HintAction, HighPriorityAction {
    private val symbolToResolve: String
    private var filesToImport: List<String>? = null

    constructor(element: PsiElement, importPath: String) : super(element) {
        symbolToResolve = ""
        filesToImport = listOf(importPath)
    }

    constructor(reference: PsiReference) : super(reference.element) {
        symbolToResolve = reference.canonicalText
    }

    private fun getReference(element: PsiElement): PsiReference? {
        if (!element.isValid) return null

        for (reference in element.references) {
            if (isSupportedReference(reference)) {
                return reference
            }
        }

        return null
    }

    override fun showHint(editor: Editor) = doAutoImportOrShowHint(editor, true)

    override fun getText(): String {
        val element = startElement
        return if (element != null) {
            "Import " + getText(findImportVariants(element))
        } else {
            "Import file"
        }
    }

    override fun getFamilyName() = "Import file"

    override operator fun invoke(
        project: Project, file: PsiFile, editor: Editor?,
        startElement: PsiElement, endElement: PsiElement,
    ) {
        if (!FileModificationService.getInstance().prepareFileForWrite(file)) return
        perform(findImportVariants(startElement), file, editor)
    }

    override fun isAvailable(
        project: Project,
        file: PsiFile,
        startElement: PsiElement,
        endElement: PsiElement,
    ): Boolean {
        val reference = getReference(startElement)
        return file is TactFile &&
                file.manager.isInProject(file) &&
                reference != null &&
                reference.resolve() == null &&
                findImportVariants(startElement).isNotEmpty() &&
                notQualified(startElement)
    }

    private fun findImportVariants(element: PsiElement): List<String> {
        if (filesToImport == null) {
            filesToImport = getImportVariantsToImport(symbolToResolve, element)
        }
        return filesToImport!!
    }

    fun doAutoImportOrShowHint(editor: Editor, showHint: Boolean): Boolean {
        val element: PsiElement? = startElement
        if (element == null || !element.isValid) {
            return false
        }

        val reference = getReference(element)
        if (reference == null || reference.resolve() != null) {
            return false
        }

        val filesToImport = findImportVariants(element)
        if (filesToImport.isEmpty()) {
            return false
        }

        val file = element.containingFile
        val firstFileToImport = filesToImport.firstOrNull()

        if (filesToImport.size == 1) {
            if (ApplicationManager.getApplication().isUnitTestMode) {
                CommandProcessor.getInstance().runUndoTransparentAction { perform(file, firstFileToImport) }
                return true
            }
        }

        if (!showHint) return false

        if (ApplicationManager.getApplication().isUnitTestMode) return false
        if (HintManager.getInstance().hasShownHintsThatWillHideByOtherHint(true)) return false
        val referenceRange = reference.rangeInElement.shiftRight(element.textRange.startOffset)

        HintManager.getInstance().showQuestionHint(
            editor,
            ShowAutoImportPass.getMessage(filesToImport.size > 1, filesToImport.first()),
            referenceRange.startOffset,
            referenceRange.endOffset
        ) {
            if (file.isValid && !editor.isDisposed) {
                perform(filesToImport, file, editor)
            }
            true
        }
        return true
    }

    private fun perform(filesToImport: List<String>, file: PsiFile, editor: Editor?) {
        LOG.assertTrue(
            editor != null || filesToImport.size == 1,
            "Cannot invoke fix with ambiguous imports on null editor"
        )

        if (ApplicationManager.getApplication().isUnitTestMode) {
            perform(file, filesToImport.minBy { it.length })
        }

        if (filesToImport.size == 1) {
            perform(file, filesToImport.first())
            return
        }

        if (filesToImport.size > 1 && editor != null) {
            val list = JBList(filesToImport)

            list.installCellRenderer { name ->
                val parts = name.split('.')
                val shortName = parts.last()

                SimpleColoredComponent().apply {
                    icon = Icons.Tact
                    append(shortName)
                    append(" ($name)", SimpleTextAttributes.GRAY_ATTRIBUTES)
                    border = JBUI.Borders.empty(2, 4)
                }
            }

            @Suppress("DEPRECATION")
            val builder = JBPopupFactory.getInstance().createListPopupBuilder(list).setRequestFocus(true)
                .setTitle("Files to Import")
                .setItemChoosenCallback {
                    val i = list.selectedIndex
                    if (i < 0) {
                        return@setItemChoosenCallback
                    }

                    perform(file, filesToImport[i])
                }
                .setFilteringEnabled { element: Any -> if (element is String) element else element.toString() }

            val popup = builder.createPopup()
            builder.scrollPane.border = null
            builder.scrollPane.viewportBorder = null
            popup.showInBestPositionFor(editor)
            return
        }

        val files = filesToImport.joinToString(", ")
        throw IncorrectOperationException("Cannot invoke fix with ambiguous imports on editor ()$editor. Files: $files")
    }

    private fun perform(file: PsiFile, pathToImport: String?) {
        if (file !is TactFile || pathToImport == null) return
        if (!canBeAutoImported(pathToImport)) return

        CommandProcessor.getInstance().executeCommand(file.project, {
            runWriteAction {
                if (!isAvailable) return@runWriteAction
                file.addImport(pathToImport)
            }
        }, "Add Import", null)
    }

    companion object {
        private fun canBeAutoImported(path: String): Boolean {
            val normalizedPath = path.replace(File.separatorChar, '/')
            return !normalizedPath.contains("stdlib/std") &&
                    !normalizedPath.contains("test/") &&
                    !normalizedPath.contains("types/")
        }

        private fun isSupportedReference(reference: PsiReference?) = reference is TactReference

        private fun getText(modulesToImport: List<String>): String {
            if (modulesToImport.isEmpty()) return ""
            return modulesToImport.first() + "? " + if (modulesToImport.size > 1) "(multiple choices...) " else ""
        }

        private fun notQualified(startElement: PsiElement?): Boolean {
            return startElement is TactReferenceExpression && startElement.getQualifier() == null ||
                    startElement is TactTypeReferenceExpression && startElement.getQualifier() == null
        }

        fun getImportVariantsToImport(symbolToImport: String, context: PsiElement): List<String> {
            val containingFile = context.containingFile.virtualFile.parent?.path ?: return emptyList()
            val candidates = TactNamesIndex.find(symbolToImport, context.project, null)
            val files = candidates.mapNotNull { it.containingFile.virtualFile }
            return files
                .map { Path(it.path).relativeTo(Path(containingFile)).pathString } // abs path -> relative path
                .map { // prepend ./ if needed
                    if (it.startsWith("..") || it.startsWith(".") || it.startsWith("/") || it.startsWith("\\")) it
                    else "./$it"
                }
                .map { it.removeSuffix(".tact") } // we don't need .tact suffix
                .filter { canBeAutoImported(it) } // filter non-importable files
        }
    }
}
