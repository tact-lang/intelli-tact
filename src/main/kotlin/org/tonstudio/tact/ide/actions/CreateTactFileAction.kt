package org.tonstudio.tact.ide.actions

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import org.tonstudio.tact.ide.ui.Icons

class CreateTactFileAction : CreateFileFromTemplateAction(
    "Tact File",
    "Create new Tact file",
    Icons.Tact
) {
    override fun isAvailable(dataContext: DataContext): Boolean {
        val project = CommonDataKeys.PROJECT.getData(dataContext)
        return super.isAvailable(dataContext) && project != null
    }

    override fun getActionName(dir: PsiDirectory, newName: String, tpl: String) = "Create Tact File '$newName'"

    override fun buildDialog(
        project: Project,
        directory: PsiDirectory,
        builder: CreateFileFromTemplateDialog.Builder,
    ) {
        builder
            .setTitle("New Tact File")
            .addKind("Tact file", Icons.Tact, "Tact File")
    }
}
