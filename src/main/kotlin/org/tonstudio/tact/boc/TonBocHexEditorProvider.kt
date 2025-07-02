package org.tonstudio.tact.boc

import com.intellij.lang.Language
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.fileEditor.impl.text.PsiAwareTextEditorImpl
import com.intellij.openapi.fileEditor.impl.text.TextEditorProvider
import com.intellij.openapi.fileTypes.PlainTextLanguage
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import java.io.File

class TonBocHexEditorProvider : FileEditorProvider {
    override fun accept(project: Project, file: VirtualFile) = file.fileType is TonBocFileType

    private fun createFileFragment(project: Project, dialect: Language, text: String): PsiFile? {
        return PsiFileFactory.getInstance(project).createFileFromText("__dummy.hex", dialect, text, true, true)
    }

    private fun convertToHex(filePath: String): String {
        return try {
            val file = File(filePath)
            if (!file.exists()) {
                "File not found: $filePath"
            } else {
                val bytes = file.readBytes()
                bytes.joinToString(separator = "") { "%02x".format(it.toInt() and 0xFF) }
            }
        } catch (e: Exception) {
            "Error reading file: ${e.message}"
        }
    }

    override fun createEditor(project: Project, file: VirtualFile): FileEditor {
        val hexContent = convertToHex(file.path)
        val fragment = createFileFragment(project, PlainTextLanguage.INSTANCE, hexContent)
        val fragmentFile = fragment?.virtualFile ?: file
        return TonBocHexEditor(project, fragmentFile, TextEditorProvider.getInstance())
    }

    override fun getEditorTypeId(): String = "ton-boc-hex-editor"

    override fun getPolicy(): FileEditorPolicy = FileEditorPolicy.PLACE_AFTER_DEFAULT_EDITOR
}

class TonBocHexEditor(project: Project, virtualFile: VirtualFile, provider: TextEditorProvider) : PsiAwareTextEditorImpl(project, virtualFile, provider) {
    override fun getName() = "Hex"
}