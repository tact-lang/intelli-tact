package org.tonstudio.tact.lang.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import org.tonstudio.tact.ide.codeInsight.TactCodeInsightUtil
import org.tonstudio.tact.lang.psi.*
import org.tonstudio.tact.lang.psi.types.*
import org.tonstudio.tact.lang.psi.types.TactBaseTypeEx.Companion.toEx
import org.tonstudio.tact.utils.parentNth

fun TactExpression.inferType(context: ResolveState?): TactTypeEx? {
    return TactTypeInferer.getType(this, context)
}

fun PsiElement.contextType(): TactTypeEx? {
    return TactTypeInferer.getContextType(this)
}

object TactTypeInferer {
    fun getType(expr: TactExpression, context: ResolveState?): TactTypeEx? {
        if (isBoolExpr(expr)) {
            return TactPrimitiveTypeEx.BOOL
        }

        if (expr is TactUnaryExpr) {
            if (expr.expression == null) return null
            val exprType = expr.expression!!.getType(context) ?: return null
            when {
                expr.not != null -> return TactPrimitiveTypeEx.BOOL
                expr.mul != null -> return exprType
            }
            return exprType
        }

        // (type) -> type
        if (expr is TactParenthesesExpr) {
            return expr.expression?.getType(context)
        }

        // "" -> string
        if (expr is TactStringLiteral) {
            return TactPrimitiveTypeEx.STRING
        }

        if (expr is TactLiteral) {
            // 1 -> int
            if (expr.int != null || expr.hex != null || expr.oct != null || expr.bin != null) return TactPrimitiveTypeEx.INT
            // true -> bool
            if (expr.`true` != null || expr.`false` != null) return TactPrimitiveTypeEx.BOOL
            // nil -> null
            if (expr.`null` != null) return TactNullTypeEx.INSTANCE
        }

        // type1 + type2 -> type1
        if (expr is TactAddExpr) {
            return expr.left.getType(context)
        }

        if (expr is TactMulExpr) {
            val left = expr.left
            if (left !is TactLiteral) return left.getType(context)
            val right = expr.right
            if (right != null) return right.getType(context)
        }

        if (expr is TactReferenceExpression) {
            val resolved = expr.reference.resolve()
            if (resolved is TactTypeOwner) {
                return typeOrParameterType(resolved, context)
            }
        }

        // type{...} -> type
        if (expr is TactLiteralValueExpression) {
            return expr.type.toEx()
        }

        if (expr is TactCallExpr) {
            val calledExpr = expr.expression
            val exprType = calledExpr?.getType(context) ?: return null
            if (exprType !is TactFunctionTypeEx) {
                return exprType
            }
            return processSignatureReturnType(exprType.signature)
        }

        if (expr is TactDotExpression) {
            if (expr.assertNotNullExpression != null) {
                val type = expr.expression.getType(context)
                return unwrapOptionType(type)
            }
        }

        if (expr is TactTernaryExpr) {
            val thenBranch = expr.thenBranch ?: return null
            val elseBranch = expr.elseBranch ?: return null

            val thenTy = thenBranch.getType(context)
            val elseTy = elseBranch.getType(context)

            if (thenTy == null) return elseTy
            if (elseTy == null) return thenTy

            if (thenTy is TactNullTypeEx) return TactOptionTypeEx(elseTy, elseTy.anchor(expr.project) ?: expr)
            if (elseTy is TactNullTypeEx) return TactOptionTypeEx(thenTy, thenTy.anchor(expr.project) ?: expr)

            return thenTy
        }

        if (expr is TactInitOfExpr) {
            return try {
                TactBaseTypeEx.stateInitType(expr.project)
            } catch (e: Exception) {
                TactUnknownTypeEx.INSTANCE
            }
        }

        if (expr is TactCodeOfExpr) {
            return TactPrimitiveTypeEx.CELL
        }

        return null
    }

    private fun processSignatureReturnType(signature: TactSignature): TactTypeEx {
        val result = signature.result ?: return TactVoidTypeEx.INSTANCE
        return result.type.toEx()
    }

    private fun typeOrParameterType(typeOwner: TactTypeOwner, context: ResolveState?): TactTypeEx? {
        return typeOwner.getType(context)
    }

    fun TactVarDefinition.getVarType(context: ResolveState?): TactTypeEx? {
        val parent = PsiTreeUtil.getStubOrPsiParent(this)
//        if (parent is TactRangeClause) {
//            return processRangeClause(this, parent, context)
//        }

        if (parent is TactVarDeclaration) {
            val hint = parent.typeHint
            if (hint != null) {
                return hint.type.toEx()
            }

            return parent.expression?.getType(context)
        }

        if (parent is TactForEachStatement) {
            val expr = parent.expression ?: return null
            val exprType = expr.getType(context) as? TactMapTypeEx ?: return null
            val key = parent.key ?: return null
            val value = parent.value ?: return null
            if (key.isEquivalentTo(this)) {
                return exprType.key
            }
            if (value.isEquivalentTo(this)) {
                return exprType.value
            }
        }

        val literal = PsiTreeUtil.getNextSiblingOfType(this, TactLiteral::class.java)
        if (literal != null) {
            return literal.getType(context)
        }

        return null
    }

    private fun unwrapOptionType(type: TactTypeEx?): TactTypeEx? {
        if (type is TactOptionTypeEx) {
            return type.inner
        }
        return type
    }

    fun getContextType(element: PsiElement): TactTypeEx? {
        val parentStructInit = element.parentNth<TactLiteralValueExpression>(3)
        if (parentStructInit != null) {
            val withoutKeys = parentStructInit.elementList.any { it.key == null }
            if (withoutKeys) {
                val elem = parentStructInit.elementList.firstOrNull { PsiTreeUtil.isAncestor(it.value, element, false) }
                val key = elem?.key
                val fieldName = key?.fieldName?.text ?: return null
                val struct = parentStructInit.getType(null) as? TactStructTypeEx ?: return null
                val structDecl = struct.resolve(element.project) ?: return null
                val structType = structDecl.structType
                return structType.fieldList.firstOrNull { it.name == fieldName }?.getType(null)
            }
        }

        if (element.parent is TactValue) {
            val parentElement = element.parentNth<TactElement>(2)
            val key = parentElement?.key
            if (key?.fieldName != null) {
                val resolved = key.fieldName?.resolve() as? TactFieldDefinition
                val declaration = resolved?.parent as? TactFieldDeclaration
                val resolvedType = declaration?.type?.toEx()
                if (resolvedType != null) {
                    return resolvedType
                }
            }
        }

        if (element.parent is TactDefaultFieldValue) {
            val fieldDeclaration = element.parentOfType<TactFieldDeclaration>() ?: return null
            return fieldDeclaration.type.toEx()
        }

        if (element.parent is TactAddExpr) {
            val addExpr = element.parent as TactAddExpr
            if (addExpr.bitOr != null) {
                return getContextType(addExpr)
            }
        }

        if (element.parent is TactBinaryExpr) {
            val binaryExpr = element.parent as TactBinaryExpr
            val right = binaryExpr.right ?: return null
            if (binaryExpr.right != null && right.isEquivalentTo(element)) {
                val left = binaryExpr.left
                return left.getType(null)
            }
        }

        if (element.parent is TactAssignmentStatement) {
            val assign = element.parent as TactAssignmentStatement
            return assign.right?.getType(null)
        }

        val callExpr = TactCodeInsightUtil.findCallExpr(element)
        if (callExpr != null) {
            val index = callExpr.paramIndexOf(element)
            if (index == -1) return null

            val function = callExpr.resolve() as? TactSignatureOwner ?: return null
            val params = function.getSignature()?.parameters?.paramDefinitionList ?: return null

            val param = params.getOrNull(index) ?: return null
            val paramType = param.type.toEx()

            return paramType
        }

        return null
    }

    private fun isBoolExpr(expr: TactExpression) = expr is TactConditionalExpr ||
            expr is TactAndExpr ||
            expr is TactOrExpr

}
