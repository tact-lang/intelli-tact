package org.tonstudio.tact.ide.actions

import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.fileTypes.PlainTextLanguage
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.parentOfType
import org.tonstudio.tact.asm.TonBocDisassembler
import org.tonstudio.tact.ide.build.openFileInRightTab
import org.tonstudio.tact.ide.build.runBackground
import org.tonstudio.tact.ide.build.single.builcContract
import org.tonstudio.tact.lang.psi.TactCompositeElement
import org.tonstudio.tact.lang.psi.TactContractDeclaration
import org.tonstudio.tact.lang.psi.TactFile
import kotlin.io.path.Path

class ShowAssemblyForContractAction : SelectionBasedPsiElementAction<TactCompositeElement>(TactCompositeElement::class.java) {
    override fun actionPerformed(element: TactCompositeElement) {
        val project = element.project
        val contract = element.parentOfType<TactContractDeclaration>() ?: return
        val contractName = contract.name
        val contrainingFile = contract.containingFile as? TactFile ?: return
        val virtualFile = contrainingFile.virtualFile ?: return
        val filepath = virtualFile.path

        val handler = builcContract(project, filepath) {
            val filename = Path(filepath).fileName.toString()
            val artifactName = "${filename}_$contractName.code.boc"
            val artifactPath = virtualFile.parent?.findChild("output")?.findChild(artifactName) ?: return@builcContract

            val disasm = TonBocDisassembler(project)
            val disassembledText = disasm.disassemble(artifactPath.path) ?: "Cannot disassemble '$artifactPath'."

            val disasmFile = runReadAction { createFile(project, disassembledText) }
            val disasmVirtualFile = disasmFile?.virtualFile ?: return@builcContract

            openFileInRightTab(project, disasmVirtualFile) { openedEditorWindow, _ -> }
        }

        handler.startNotify()

        runBackground(project, "Building Tact contract '$contractName'...") {
            handler.waitFor(30_000)
        }
    }

    private fun createFile(project: Project, disassembledText: String): PsiFile? =
        PsiFileFactory.getInstance(project).createFileFromText("disasm.fif", PlainTextLanguage.INSTANCE, disassembledText, true, true)
}
