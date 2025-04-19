package org.tonstudio.tact.lang

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.ParserDefinition.SpaceRequirements
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.tonstudio.tact.lang.TactTypes.Factory
import org.tonstudio.tact.lang.lexer.TactLexer
import org.tonstudio.tact.lang.psi.TactFile
import org.tonstudio.tact.lang.psi.TactTokenTypes

class TactParserDefinition : ParserDefinition {
    override fun createLexer(project: Project) = TactLexer()

    override fun createParser(project: Project) = TactParser()

    override fun getWhitespaceTokens() = TactTokenTypes.WHITE_SPACES

    override fun getCommentTokens() = TactTokenTypes.COMMENTS

    override fun getStringLiteralElements() = TactTokenTypes.STRING_LITERALS

    override fun getFileNodeType() = TactFileElementType.INSTANCE

    override fun createFile(viewProvider: FileViewProvider): PsiFile = TactFile(viewProvider)

    override fun spaceExistenceTypeBetweenTokens(left: ASTNode, right: ASTNode) = SpaceRequirements.MAY

    override fun createElement(node: ASTNode): PsiElement {
        return Factory.createElement(node)
    }
}
