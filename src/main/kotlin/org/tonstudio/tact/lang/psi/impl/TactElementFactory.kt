package org.tonstudio.tact.lang.psi.impl

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiParserFacade
import com.intellij.psi.util.PsiTreeUtil
import org.tonstudio.tact.lang.TactLanguage
import org.tonstudio.tact.lang.psi.*

object TactElementFactory {
    fun createFileFromText(project: Project, text: String): TactFile {
        return PsiFileFactory.getInstance(project).createFileFromText("dummy.sp", TactLanguage, text) as TactFile
    }

    fun createIdentifier(project: Project, text: String): PsiElement {
        val file = createFileFromText(project, "let $text = 10;")
        return PsiTreeUtil.findChildOfType(file, TactVarDeclaration::class.java)!!.varDefinition.getIdentifier()
    }

    fun createImportDeclaration(project: Project, name: String): TactImportDeclaration? {
        return createImportList(project, name)?.importDeclarationList?.firstOrNull()
    }

    fun createStringLiteral(project: Project, text: String): TactStringLiteral {
        return PsiTreeUtil.findChildOfType(
            createFileFromText(project, "fun main() { $text }"),
            TactStringLiteral::class.java
        )!!
    }

    fun createNewLine(project: Project): PsiElement {
        return PsiParserFacade.getInstance(project).createWhiteSpaceFromText("\n")
    }

    fun createDoubleNewLine(project: Project): PsiElement {
        return PsiParserFacade.getInstance(project).createWhiteSpaceFromText("\n\n")
    }

    fun createImportList(project: Project, name: String): TactImportList? {
        val file = createFileFromText(project, "import \"$name\";")
        return file.getImportList()
    }
}
