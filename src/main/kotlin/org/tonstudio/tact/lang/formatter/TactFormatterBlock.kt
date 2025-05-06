package org.tonstudio.tact.lang.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock
import org.tonstudio.tact.lang.TactTypes

class TactFormatterBlock(
    node: ASTNode,
    wrap: Wrap?,
    alignment: Alignment?,
    private val indent: Indent? = null,
    private val spacingBuilder: SpacingBuilder,
) : AbstractBlock(node, null, null) {
    private val childIndent = when (node.elementType) {
        TactTypes.CONTRACT_TYPE,
        TactTypes.TRAIT_TYPE,
        TactTypes.STRUCT_TYPE,
        TactTypes.MESSAGE_TYPE,
        TactTypes.BLOCK,
        TactTypes.ASM_FUNCTION_BODY,
        TactTypes.LITERAL_VALUE_EXPRESSION,
        TactTypes.CONTRACT_PARAMETERS,
             -> Indent.getNormalIndent()

        else -> Indent.getNoneIndent()
    }

    override fun getSpacing(child1: Block?, child2: Block): Spacing? = spacingBuilder.getSpacing(this, child1, child2)

    override fun getIndent(): Indent? = indent

    override fun buildChildren(): List<TactFormatterBlock> {
        val blocks = ArrayList<TactFormatterBlock>()
        var child = myNode.firstChildNode
        while (child != null) {
            if (child.elementType != TokenType.WHITE_SPACE) {
                blocks.add(TactFormatterBlock(child, null, null, computeIndent(child), spacingBuilder))
            }
            child = child.treeNext
        }
        return blocks
    }

    override fun getChildIndent(): Indent? = childIndent

    override fun isLeaf(): Boolean = myNode.firstChildNode == null

    private fun computeIndent(child: ASTNode): Indent? {
        val parentType = node.elementType
        val childType = child.elementType

        return when (parentType) {
            TactTypes.CONTRACT_TYPE,
            TactTypes.TRAIT_TYPE,
            TactTypes.STRUCT_TYPE,
            TactTypes.MESSAGE_TYPE,
                 -> {
                when (childType) {
                    TactTypes.FIELD_DECLARATION            -> Indent.getNormalIndent()
                    TactTypes.CONST_DEFINITION             -> Indent.getNormalIndent()
                    TactTypes.FUNCTION_DECLARATION         -> Indent.getNormalIndent()
                    TactTypes.MESSAGE_FUNCTION_DECLARATION -> Indent.getNormalIndent()
                    TactTypes.CONTRACT_INIT_DECLARATION    -> Indent.getNormalIndent()
                    else                                   -> Indent.getNoneIndent()
                }
            }

            TactTypes.LITERAL_VALUE_EXPRESSION,
                 -> {
                when (childType) {
                    TactTypes.INSTANCE_ARGUMENTS -> Indent.getNormalIndent()
                    else                         -> Indent.getNoneIndent()
                }
            }

            TactTypes.CONTRACT_PARAMETERS,
                 -> {
                when (childType) {
                    TactTypes.FIELD_DECLARATION -> Indent.getNormalIndent()
                    else                        -> Indent.getNoneIndent()
                }
            }

            TactTypes.BLOCK,
                 -> {
                when (childType) {
                    TactTypes.LBRACE, TactTypes.RBRACE -> Indent.getNoneIndent()
                    else                               -> Indent.getNormalIndent()
                }
            }

            else -> Indent.getNoneIndent()
        }
    }
}
