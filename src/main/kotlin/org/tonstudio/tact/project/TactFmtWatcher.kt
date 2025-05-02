package org.tonstudio.tact.project

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.components.serviceIfCreated
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManagerListener
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.util.containers.ContainerUtil
import org.tonstudio.tact.configurations.TactFmtSettingsState.Companion.tactfmtSettings
import org.tonstudio.tact.toolchain.TactFmt
import org.tonstudio.tact.utils.guessProjectForFile
import org.tonstudio.tact.utils.isNotTactFile
import org.tonstudio.tact.utils.virtualFile

@Service
class TactFmtWatcher {
    private val documentsToReformatLater = ContainerUtil.newConcurrentSet<Document>()
    private var isSuppressed = false

    fun reformatDocumentLater(document: Document): Boolean {
        val file = document.virtualFile ?: return false
        if (file.isNotTactFile) return false
        val project = file.guessProjectForFile ?: return false
        if (!project.tactfmtSettings.runTactfmtOnSave) return false
        return documentsToReformatLater.add(document)
    }

    class TactFmtListener : FileDocumentManagerListener {
        override fun beforeAllDocumentsSaving() {
            val documentsToReformatLater = getInstanceIfCreated()?.documentsToReformatLater
                ?: return
            val documentsToReformat = documentsToReformatLater.toList()
            documentsToReformatLater.clear()

            if (documentsToReformatLater.isEmpty()) return

            val project = documentsToReformatLater.first().virtualFile?.guessProjectForFile ?: return

            if (DumbService.isDumb(project)) {
                documentsToReformatLater += documentsToReformat
            } else {
                reformatDocuments(project, documentsToReformat)
            }
        }

        override fun beforeDocumentSaving(document: Document) {
            val isSuppressed = getInstanceIfCreated()?.isSuppressed == true
            if (!isSuppressed) {
                val project = document.virtualFile?.guessProjectForFile ?: return
                if (DumbService.isDumb(project)) {
                    getInstance().reformatDocumentLater(document)
                } else {
                    reformatDocuments(project, listOf(document))
                }
            }
        }

        override fun unsavedDocumentsDropped() {
            getInstanceIfCreated()?.documentsToReformatLater?.clear()
        }
    }

    companion object {
        fun getInstance() = service<TactFmtWatcher>()
        private fun getInstanceIfCreated() = serviceIfCreated<TactFmtWatcher>()

        private fun reformatDocuments(project: Project, documents: List<Document>) {
            if (!project.tactfmtSettings.runTactfmtOnSave) return
            documents.forEach { TactFmt.reformatDocument(project, it) }
        }
    }
}
