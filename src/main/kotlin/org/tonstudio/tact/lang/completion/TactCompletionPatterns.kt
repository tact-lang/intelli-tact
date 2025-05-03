package org.tonstudio.tact.lang.completion

import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns.*
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import org.tonstudio.tact.lang.TactTypes.*
import org.tonstudio.tact.lang.psi.*
import org.tonstudio.tact.lang.psi.impl.TactCachedReference
import org.tonstudio.tact.utils.parentNth

object TactCompletionPatterns {
    private val whitespace: PsiElementPattern.Capture<PsiElement> = psiElement().whitespace()

    /**
     * Expressions like:
     *
     *     a = 200
     */
    fun onExpression(): PsiElementPattern.Capture<PsiElement> =
        psiElement()
            .withParent(TactReferenceExpression::class.java)
            .notAfterDot()
            .notAfterLiteral()
            .notInsideStructInitWithKeys()
            .notInsideTrailingStruct()
            .noTopLevelNext()

    /**
     * Statements like:
     *
     *     if (true) {}
     */
    fun onStatement(): PsiElementPattern.Capture<PsiElement> =
        psiElement()
            .withSuperParent(2, TactSimpleStatement::class.java)
            .notAfterDot()
            .notAfterLiteral()
            .noTopLevelNext()

    /**
     * Assembly instructions like:
     *
     *     ADD
     */
    fun onAsmInstruction(): PsiElementPattern.Capture<PsiElement> =
        psiElement()
            .withSuperParent(1, TactAsmInstruction::class.java)

    /**
     * Any top-level statements like:
     *
     *     fun bar() {}
     */
    fun onTopLevel(): PsiElementPattern.Capture<PsiElement> =
        psiElement()
            .withSuperParent(2, TactSimpleStatement::class.java)
            .withSuperParent(3, TactFile::class.java)
            .notAfterDot()
            .notAfterLiteral()

    /**
     * Any declarations inside trait or contract like:
     *
     *     fun bar() {}
     */
    fun onContractTraitTopLevel(): PsiElementPattern.Capture<PsiElement> =
        psiElement()
            .withSuperParent(4, or(instanceOf(TactTraitDeclaration::class.java), instanceOf(TactContractDeclaration::class.java)))
            .withSuperParent(5, TactFile::class.java)
            .notAfterDot()
            .notAfterLiteral()

    /**
     * Any place where type expected like:
     *
     *    fun foo(): <caret>
     */
    fun onType(): PsiElementPattern.Capture<PsiElement> =
        psiElement()
            .withParent(TactTypeReferenceExpression::class.java)
            .notAfterLiteral()

    /**
     * Element after if/else statement like:
     *
     *    if (true) { ... } <caret>
     */

    /**
     * Element after if/else statement like:
     *
     *    if true { ... } <caret>
     */
    fun onIfElse(): PsiElementPattern.Capture<PsiElement> {
        val braceAfterIf = psiElement(RBRACE).withSuperParent(2, psiElement(IF_STATEMENT))
        return psiElement().afterLeafSkipping(whitespace, braceAfterIf)
    }

    fun referenceExpression(): PsiElementPattern.Capture<PsiElement> {
        return psiElement()
            .withParent(TactReferenceExpressionBase::class.java)
            .notAfterLiteral()
            .noTopLevelNext()
    }

    fun cachedReferenceExpression(): PsiElementPattern.Capture<PsiElement> {
        return psiElement()
            .withParent(psiElement().withReference(TactCachedReference::class.java))
            .notAfterLiteral()
            .noTopLevelNext()
    }

    private fun PsiElementPattern.Capture<PsiElement>.notAfterDot(): PsiElementPattern.Capture<PsiElement> {
        return andNot(psiElement().afterLeafSkipping(whitespace, psiElement(DOT)))
    }

    private fun PsiElementPattern.Capture<PsiElement>.notAfterLiteral(): PsiElementPattern.Capture<PsiElement> {
        return andNot(psiElement().with(object : PatternCondition<PsiElement>("notAfterLiteral") {
            override fun accepts(t: PsiElement, context: ProcessingContext?): Boolean {
                // when autocomplete in position: `100<caret>`
                // then `t` is `intellijrulezzz` and prev leaf is error element, because
                // `100intellijrulezzz` is not a valid expression,
                // so we need to check that prev leaf is error element
                // and prev leaf of error element is literal
                // and return true to disable completion
                // true because this matcher used in andNot() matcher.
                // Other option when we have `age int = 10<caret>`, then prev element is TactTypeModifiers,
                // so we need to check that prev leaf is error element or TactTypeModifiers
                val rawPrevLeaf = PsiTreeUtil.prevLeaf(t)

                // in some cases prev leaf can be literal
                val prevParent = rawPrevLeaf?.parent
                if (prevParent is TactLiteral) return true

                val prevPrev = if (rawPrevLeaf is PsiErrorElement) {
                    PsiTreeUtil.prevLeaf(rawPrevLeaf)
                } else {
                    return false
                }

                return prevPrev?.parent is TactLiteral
            }
        }))
    }

    private fun PsiElementPattern.Capture<PsiElement>.notInsideStructInitWithKeys(): PsiElementPattern.Capture<PsiElement> {
        return andNot(psiElement().with(object : PatternCondition<PsiElement>("notInsideStructInitWithKeys") {
            override fun accepts(t: PsiElement, context: ProcessingContext?): Boolean {
                val element = t.parentNth<TactElement>(3) ?: return false
                // if 'key: <caret>'
                if (element.key != null) return false

                // foo(fn<caret>)
                if (element.parent is TactArgumentList) {
                    return false
                }

                val prevElement = PsiTreeUtil.findSiblingBackward(element, ELEMENT, null) as? TactElement
                // if 'value, <caret>'
                return !(prevElement != null && prevElement.key == null)
            }
        }))
    }

    private fun PsiElementPattern.Capture<PsiElement>.notInsideTrailingStruct(): PsiElementPattern.Capture<PsiElement> {
        return andNot(psiElement().with(object : PatternCondition<PsiElement>("notInsideTrailingStruct") {
            override fun accepts(t: PsiElement, context: ProcessingContext?): Boolean {
                val element = t.parentNth<TactElement>(3) ?: return false
                // if 'key: <caret>'
                if (element.key != null) return false

                // foo(fn<caret>)
                if (element.parent !is TactArgumentList) {
                    return false
                }

                val prevElement = PsiTreeUtil.findSiblingBackward(element, ELEMENT, null) as? TactElement ?: return false
                // if 'value, <caret>'
                return prevElement.key != null
            }
        }))
    }

    private fun PsiElementPattern.Capture<PsiElement>.noTopLevelNext(): PsiElementPattern.Capture<PsiElement> {
        return andNot(psiElement().with(object : PatternCondition<PsiElement>("noTopLevelNext") {
            override fun accepts(t: PsiElement, context: ProcessingContext?): Boolean {
                val statement = t.parentOfType<TactStatement>() ?: return false
                val next = PsiTreeUtil.skipWhitespacesAndCommentsForward(statement) ?: return false
                return next is TactFunctionOrMethodDeclaration ||
                        next is TactStructDeclaration ||
                        next is TactTraitDeclaration ||
                        next is TactMessageDeclaration ||
                        next is TactImportDeclaration ||
                        next is TactConstDeclaration ||
                        next is TactPrimitiveDeclaration ||
                        next is TactContractDeclaration
            }
        }))
    }
}
