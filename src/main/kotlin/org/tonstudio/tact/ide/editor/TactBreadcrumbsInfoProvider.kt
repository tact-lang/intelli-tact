package org.tonstudio.tact.ide.editor

import com.intellij.lang.Language
import com.intellij.psi.PsiElement
import com.intellij.ui.breadcrumbs.BreadcrumbsProvider
import org.tonstudio.tact.ide.editor.TactBreadcrumbsInfoProvider.Util.truncate
import org.tonstudio.tact.lang.TactLanguage
import org.tonstudio.tact.lang.psi.*

class TactBreadcrumbsInfoProvider : BreadcrumbsProvider {
    private interface ElementHandler<T : TactCompositeElement> {
        fun accepts(e: PsiElement): Boolean

        fun elementInfo(e: T): String
    }

    private val handlers = listOf<ElementHandler<*>>(
        TactFunctionHandler,
        TactAsmFunctionHandler,
        TactNativeFunctionHandler,
        TactReceiverFunctionHandler,
        TactStructHandler,
        TactMessageHandler,
        TactTraitHandler,
        TactContractHandler,
        TactIfHandler,
        TactElseHandler,
        TactForeachHandler,
        TactWhileHandler,
        TactUntilHandler,
        TactLiteralValueHandler,
        TactFieldNameHandler,
    )

    private object TactFunctionHandler : ElementHandler<TactFunctionDeclaration> {
        override fun accepts(e: PsiElement): Boolean = e is TactFunctionDeclaration

        override fun elementInfo(e: TactFunctionDeclaration): String = "${e.name}()"
    }

    private object TactAsmFunctionHandler : ElementHandler<TactAsmFunctionDeclaration> {
        override fun accepts(e: PsiElement): Boolean = e is TactAsmFunctionDeclaration

        override fun elementInfo(e: TactAsmFunctionDeclaration): String = "asm ${e.name}()"
    }

    private object TactNativeFunctionHandler : ElementHandler<TactNativeFunctionDeclaration> {
        override fun accepts(e: PsiElement): Boolean = e is TactNativeFunctionDeclaration

        override fun elementInfo(e: TactNativeFunctionDeclaration): String = "native ${e.name}()"
    }

    private object TactReceiverFunctionHandler : ElementHandler<TactMessageFunctionDeclaration> {
        override fun accepts(e: PsiElement): Boolean = e is TactMessageFunctionDeclaration

        override fun elementInfo(e: TactMessageFunctionDeclaration): String = e.nameLike()
    }

    private object TactStructHandler : ElementHandler<TactStructDeclaration> {
        override fun accepts(e: PsiElement): Boolean = e is TactStructDeclaration

        override fun elementInfo(e: TactStructDeclaration): String = "struct ${e.name}"
    }

    private object TactMessageHandler : ElementHandler<TactMessageDeclaration> {
        override fun accepts(e: PsiElement): Boolean = e is TactMessageDeclaration

        override fun elementInfo(e: TactMessageDeclaration): String = "message ${e.name}"
    }

    private object TactTraitHandler : ElementHandler<TactTraitDeclaration> {
        override fun accepts(e: PsiElement): Boolean = e is TactTraitDeclaration

        override fun elementInfo(e: TactTraitDeclaration): String = "trait ${e.name}"
    }

    private object TactContractHandler : ElementHandler<TactContractDeclaration> {
        override fun accepts(e: PsiElement): Boolean = e is TactContractDeclaration

        override fun elementInfo(e: TactContractDeclaration): String = "contract ${e.name}"
    }

    private object TactIfHandler : ElementHandler<TactIfStatement> {
        override fun accepts(e: PsiElement): Boolean = e is TactIfStatement

        override fun elementInfo(e: TactIfStatement): String = buildString {
            append("if ")
            val condition = e.expression
            if (condition != null) {
                append(condition.text.truncate())
            } else {
                append("?")
            }
        }
    }

    private object TactElseHandler : ElementHandler<TactElseBranch> {
        override fun accepts(e: PsiElement): Boolean = e is TactElseBranch

        override fun elementInfo(e: TactElseBranch): String = buildString {
            append("else")
        }
    }

    private object TactForeachHandler : ElementHandler<TactForEachStatement> {
        override fun accepts(e: PsiElement): Boolean = e is TactForEachStatement

        override fun elementInfo(e: TactForEachStatement): String = buildString {
            append("foreach ")
        }
    }

    private object TactWhileHandler : ElementHandler<TactWhileStatement> {
        override fun accepts(e: PsiElement): Boolean = e is TactWhileStatement

        override fun elementInfo(e: TactWhileStatement): String = buildString {
            append("while ")
        }
    }

    private object TactUntilHandler : ElementHandler<TactUntilStatement> {
        override fun accepts(e: PsiElement): Boolean = e is TactUntilStatement

        override fun elementInfo(e: TactUntilStatement): String = buildString {
            append("until ")
        }
    }

    private object TactLiteralValueHandler : ElementHandler<TactLiteralValueExpression> {
        override fun accepts(e: PsiElement): Boolean = e is TactLiteralValueExpression

        override fun elementInfo(e: TactLiteralValueExpression): String = buildString {
            val type = e.type
            append(type.text.truncate())
            append("{...}")
        }
    }

    private object TactFieldNameHandler : ElementHandler<TactElement> {
        override fun accepts(e: PsiElement): Boolean = e is TactElement && e.key != null && e.key?.fieldName != null

        override fun elementInfo(e: TactElement): String = buildString {
            val fieldName = e.key!!.fieldName!!
            append(fieldName.text)
            append(":")
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun handler(e: PsiElement): ElementHandler<in TactCompositeElement>? {
        return if (e is TactCompositeElement)
            handlers.firstOrNull { it.accepts(e) } as ElementHandler<in TactCompositeElement>?
        else null
    }

    override fun getLanguages(): Array<Language> = arrayOf(TactLanguage)

    override fun acceptElement(e: PsiElement): Boolean = handler(e) != null

    override fun getElementInfo(e: PsiElement): String = handler(e)!!.elementInfo(e as TactCompositeElement)

    object Util {
        fun String.truncate(): String {
            return if (length > 16) "${substring(0, 16 - ELLIPSIS.length)}$ELLIPSIS"
            else this
        }
    }

    companion object {
        private const val ELLIPSIS = "${Typography.ellipsis}"
    }
}
