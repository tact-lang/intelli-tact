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
import org.tonstudio.tact.asm.TonBocDisassembler

class TonBocEditorProvider : FileEditorProvider {
    override fun accept(project: Project, file: VirtualFile) = file.fileType is TonBocFileType

    private fun createFileFragment(project: Project, dialect: Language, text: String): PsiFile? {
        return PsiFileFactory.getInstance(project).createFileFromText("__dummy.fif", dialect, text, true, true)
    }

    override fun createEditor(project: Project, file: VirtualFile): FileEditor {
        val disassembler = TonBocDisassembler(project)
        val disassembledText = disassembler.disassemble(file.path) ?: "Cannot decompile, toolchain not found."
        val fraagment = createFileFragment(project, PlainTextLanguage.INSTANCE, disassembledText)
        val fragmentFile = fraagment?.virtualFile ?: file
        return TonBocEditor(project, fragmentFile, TextEditorProvider.getInstance())
    }

    override fun getEditorTypeId(): String = "ton-boc-editor"

    override fun getPolicy(): FileEditorPolicy = FileEditorPolicy.PLACE_BEFORE_DEFAULT_EDITOR
}

class TonBocEditor(project: Project, virtualFile: VirtualFile, provider: TextEditorProvider) : PsiAwareTextEditorImpl(project, virtualFile, provider) {
    override fun getName() = "Decompiled"
}
