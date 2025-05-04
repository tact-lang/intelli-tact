package org.tonstudio.tact.lang.psi.impl

import com.intellij.psi.ResolveState
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import org.tonstudio.tact.lang.TactTypes
import org.tonstudio.tact.lang.psi.*
import org.tonstudio.tact.lang.psi.types.*
import org.tonstudio.tact.lang.psi.types.TactBaseTypeEx.Companion.toEx
import org.tonstudio.tact.utils.childOfType

fun TactExpression.inferType(context: ResolveState?): TactTypeEx? {
    return TactTypeInferer.getType(this, context)
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

            val qualifier = (calledExpr as? TactReferenceExpression)?.getQualifier()
            if (qualifier is TactExpression) {
                val type = qualifier.getType(null)
                val resultType = processPseudoStaticCall(qualifier, calledExpr as TactReferenceExpression, type)
                if (resultType != null) {
                    return resultType
                }
            }

            val exprType = calledExpr?.getType(context) ?: return null
            if (exprType !is TactFunctionTypeEx) {
                return exprType
            }
            return processSignatureReturnType(exprType.signature)
        }

        if (expr is TactAssertNotNullExpr) {
            val referenceExpression = expr.childOfType<TactReferenceExpression>() ?: return null
            val type = referenceExpression.getType(context)
            return unwrapOptionType(type)
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

    private fun processPseudoStaticCall(
        qualifier: TactCompositeElement,
        element: TactReferenceExpressionBase,
        type: TactTypeEx?,
    ): TactTypeEx? {
        if (qualifier.elementType != TactTypes.REFERENCE_EXPRESSION) {
            // complex qualifier: foo().bar()
            return null
        }

        if (type !is TactStructTypeEx && type !is TactMessageTypeEx) {
            // pseudo statics only defined on messages and structs
            return null
        }

        val resolvedQualifier = (qualifier as? TactReferenceExpression)?.resolve() ?: return null
        if (resolvedQualifier !is TactStructDeclaration && resolvedQualifier !is TactMessageDeclaration) {
            // case like:
            // let a: Foo = Foo {}
            // a.fromCell()
            // ^ not a static call
            return null
        }

        val searchedName = element.getIdentifier()?.text ?: ""
        if (searchedName == "fromSlice" || searchedName == "fromCell") {
            return type
        }

        return null
    }

    private fun typeOrParameterType(typeOwner: TactTypeOwner, context: ResolveState?): TactTypeEx? {
        return typeOwner.getType(context)
    }

    fun TactVarDefinition.getVarType(context: ResolveState?): TactTypeEx? {
        val parent = PsiTreeUtil.getStubOrPsiParent(this)

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

        if (parent is TactCatchClause) {
            return TactPrimitiveTypeEx.INT
        }

        if (parent is TactDestructItem) {
            val grand = parent.parent as? TactDestructStatement ?: return null
            val resolved = grand.typeReferenceExpression.resolve() ?: return null
            val type = when (resolved) {
                is TactStructDeclaration  -> resolved.structType
                is TactMessageDeclaration -> resolved.messageType
                else                      -> return null
            }
            val fields = type.fieldList
            val searchName =
                if (parent.referenceExpression != null) parent.referenceExpression?.getIdentifier()?.text else parent.varDefinition.name
            val field = fields.find { it.name == searchName }
            return field?.getType(null)
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

    private fun isBoolExpr(expr: TactExpression) = expr is TactConditionalExpr ||
            expr is TactAndExpr ||
            expr is TactOrExpr

}
