package org.tonstudio.tact.lang.completion

import com.intellij.psi.PsiElement
import org.jetbrains.annotations.Contract
import org.tonstudio.tact.lang.psi.*
import org.tonstudio.tact.lang.psi.impl.TactLangUtil
import org.tonstudio.tact.lang.psi.impl.TactPsiImplUtil
import org.tonstudio.tact.lang.psi.types.TactBaseTypeEx.Companion.toEx
import org.tonstudio.tact.utils.parentNth

internal object TactStructLiteralCompletion {
    fun allowedVariants(structFieldReference: TactReferenceExpression?, refElement: PsiElement): Variants {
        if (structFieldReference == null) {
            return Variants.NONE
        }

        if (TactPsiImplUtil.prevDot(refElement)) {
            return Variants.NONE
        }

        var value = structFieldReference.parent
        while (value is TactUnaryExpr) {
            value = value.parent
        }
        if (value !is TactValue) {
            return Variants.NONE
        }

        val element = parent<TactElement>(value)
        if (element?.key != null) {
            return Variants.NONE
        }

        var hasValueInitializers = false
        var hasFieldValueInitializers = false

        val fieldInitializers: List<TactElement> = getFieldInitializers(structFieldReference)

        for (initializer in fieldInitializers) {
            if (initializer === element) {
                continue
            }
            val colon = initializer.colon
            hasFieldValueInitializers = hasFieldValueInitializers or (colon != null)
            hasValueInitializers = hasValueInitializers or (colon == null)
        }
        return if (hasFieldValueInitializers && !hasValueInitializers) {
            Variants.FIELD_NAME_ONLY
        } else if (!hasFieldValueInitializers && hasValueInitializers) {
            Variants.VALUE_ONLY
        } else {
            Variants.BOTH
        }
    }

    private fun getFieldInitializers(element: PsiElement): List<TactElement> {
        val literalValue = element.parentNth<TactLiteralValueExpression>(3)
        if (literalValue != null) {
            return literalValue.elementList
        }

        val callExpr = TactLangUtil.findCallExpr(element)
        val resolved = callExpr?.resolve() as? TactSignatureOwner
        val params = resolved?.getSignature()?.parameters?.paramDefinitionList
        val paramTypes = params?.map { it.type.toEx() }

        if (paramTypes != null) {
            val startIndex = paramTypes.lastIndex

            val list = callExpr.argumentList.elementList
            if (startIndex == -1 || startIndex >= list.size) return emptyList()

            return list.subList(startIndex, list.size)
        }

        return emptyList()
    }

    fun alreadyAssignedFields(elements: List<TactElement>): Set<String> {
        return elements.mapNotNull {
            val identifier = it.key?.fieldName?.getIdentifier()
            identifier?.text
        }.toSet()
    }

    @Contract("null,_->null")
    private inline fun <reified T> parent(of: PsiElement?): T? {
        val parent = of?.parent
        return if (parent is T) parent else null
    }

    /**
     * Describes struct literal completion variants that should be suggested.
     */
    internal enum class Variants {
        /**
         * Only struct field names should be suggested.
         * Indicates that field:value initializers are used in this struct literal.
         * For example, `Struct { field1: "", <caret> }`.
         */
        FIELD_NAME_ONLY,

        /**
         * Only values should be suggested.
         * Indicates that value initializers are used in this struct literal.
         * For example, `Struct { "", <caret> }`.
         */
        VALUE_ONLY,

        /**
         * Both struct field names and values should be suggested.
         * Indicates that there's no reliable way to determine whether field:value or value initializers are used.
         * Example 1: `Struct { <caret> }`.
         * Example 2: `Struct { field1: "", name, <caret> }`
         */
        BOTH,

        /**
         * Indicates that struct literal completion should not be available.
         */
        NONE
    }
}
