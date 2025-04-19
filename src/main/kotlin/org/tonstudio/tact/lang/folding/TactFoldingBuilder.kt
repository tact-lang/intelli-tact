package org.tonstudio.tact.lang.folding

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.FoldingGroup
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.intellij.psi.util.endOffset
import com.intellij.psi.util.startOffset
import org.tonstudio.tact.lang.TactTypes
import org.tonstudio.tact.lang.psi.*

class TactFoldingBuilder : FoldingBuilderEx(), DumbAware {
    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val descriptors = mutableListOf<FoldingDescriptor>()

        root.accept(object : TactRecursiveElementVisitor() {
            override fun visitBlock(el: TactBlock) {
                val range = el.textRange
                val group = FoldingGroup.newGroup("TactFoldingGroup")
                descriptors.add(FoldingDescriptor(el.node, TextRange(range.startOffset, range.endOffset), group))
                super.visitElement(el)
            }

            override fun visitImportList(el: TactImportList) {
                if (el.importDeclarationList.size == 1) {
                    // don't add folding for lonely import
                    return
                }

                val first = el.importDeclarationList.first()
                val startIndex = first.stringLiteral?.startOffset ?: return

                val range = el.textRange
                val group = FoldingGroup.newGroup("TactFoldingGroup")
                descriptors.add(FoldingDescriptor(el.node, TextRange(startIndex, range.endOffset), group))
                super.visitElement(el)
            }

            override fun visitConstDeclaration(el: TactConstDeclaration) {
                genericFolding(el, TactTypes.LPAREN)
                super.visitElement(el)
            }

            override fun visitStructDeclaration(el: TactStructDeclaration) {
                genericFolding(el)
                super.visitElement(el)
            }

            override fun visitTraitDeclaration(el: TactTraitDeclaration) {
                genericFolding(el)
                super.visitElement(el)
            }

            override fun visitMessageDeclaration(el: TactMessageDeclaration) {
                genericFolding(el)
                super.visitElement(el)
            }

            override fun visitContractDeclaration(el: TactContractDeclaration) {
                genericFolding(el)
                super.visitElement(el)
            }

            override fun visitContractInitDeclaration(el: TactContractInitDeclaration) {
                genericFolding(el)
                super.visitElement(el)
            }

            override fun visitMessageFunctionDeclaration(el: TactMessageFunctionDeclaration) {
                genericFolding(el)
                super.visitElement(el)
            }

            override fun visitIfStatement(el: TactIfStatement) {
                genericFolding(el)
                super.visitElement(el)
            }

            override fun visitElseBranch(el: TactElseBranch) {
                genericFolding(el)
                super.visitElement(el)
            }

            override fun visitForEachStatement(el: TactForEachStatement) {
                genericFolding(el)
                super.visitElement(el)
            }

            override fun visitWhileStatement(el: TactWhileStatement) {
                genericFolding(el)
                super.visitElement(el)
            }

            override fun visitUntilStatement(el: TactUntilStatement) {
                genericFolding(el)
                super.visitElement(el)
            }

            override fun visitLiteralValueExpression(el: TactLiteralValueExpression) {
                genericFolding(el)
                super.visitElement(el)
            }

            override fun visitComment(comment: PsiComment) {
                if (comment.tokenType != TactTokenTypes.MULTI_LINE_COMMENT) {
                    return
                }

                val commentText = comment.text
                if (!commentText.startsWith("/*") || !commentText.endsWith("*/")) {
                    return
                }

                val textRange = comment.textRange
                val group = FoldingGroup.newGroup("TactFoldingGroup")
                val foldingRange = TextRange(textRange.startOffset + 2, textRange.endOffset - 2)
                descriptors.add(FoldingDescriptor(comment.node, foldingRange, group))
                super.visitComment(comment)
            }

            private fun genericFolding(el: PsiElement, start: IElementType = TactTypes.LBRACE) {
                var lbrack: PsiElement? = null
                PsiTreeUtil.processElements(el) {
                    if (it.elementType == start) {
                        lbrack = it
                        return@processElements false
                    }

                    true
                }
                if (lbrack == null) {
                    return
                }

                val endOffset = when (el) {
                    is TactIfStatement -> el.block?.endOffset ?: el.textRange.endOffset
                    else               -> el.textRange.endOffset
                }

                val range = TextRange(lbrack!!.startOffset, endOffset)
                val group = FoldingGroup.newGroup("TactFoldingGroup")
                descriptors.add(FoldingDescriptor(el.node, range, group))
            }
        })

        return descriptors.toTypedArray()
    }

    override fun getPlaceholderText(node: ASTNode) = when (node.elementType) {
        TactTypes.IMPORT_LIST,
        TactTokenTypes.MULTI_LINE_COMMENT,
            -> "..."

        else -> "{...}"
    }

    override fun isCollapsedByDefault(node: ASTNode) = node.elementType == TactTypes.IMPORT_LIST
}
