package org.tonstudio.tact.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.tonstudio.tact.lang.psi.TactSignature
import org.tonstudio.tact.lang.psi.TactSignatureOwner

class TactFunctionTypeEx(val params: List<TactTypeEx>, val result: TactTypeEx?, val signature: TactSignature) : TactBaseTypeEx(signature) {
    override fun toString() = buildString {
        append("fn ")
        append("(")
        append(params.joinToString(", ") { it.toString() })
        append(")")
        if (result != null) {
            append(": ")
            append(result)
        }
    }

    override fun qualifiedName(): String = buildString {
        append("fn ")
        append("(")
        append(params.joinToString(", ") { it.qualifiedName() })
        append(")")
        if (result != null) {
            append(": ")
            append(result.qualifiedName())
        }
    }

    override fun readableName(context: PsiElement, detailed: Boolean) = buildString {
        append("fn ")
        append("(")
        append(params.joinToString(", ") { it.readableName(context) })
        append(")")
        if (result != null) {
            append(": ")
            append(result.readableName(context))
        }
    }

    override fun isAssignableFrom(project: Project, rhs: TactTypeEx, kind: AssignableKind): Boolean {
        if (rhs.isAny) return true
        return true // isEqual(rhs) TODO
    }

    override fun isEqual(rhs: TactTypeEx): Boolean {
        if (rhs !is TactFunctionTypeEx) return false

        if (params.size != rhs.params.size) {
            return false
        }

        if (result != null && rhs.result == null) {
            return false
        }

        if (result == null && rhs.result != null) {
            return false
        }

        if (result != null && rhs.result != null && !result.isEqual(rhs.result)) {
            return false
        }

        return params.zip(rhs.params)
            .all { (left, right) ->
                left.isEqual(right)
            }
    }

    override fun accept(visitor: TactTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }

        for (param in params) {
            param.accept(visitor)
        }

        result?.accept(visitor)
    }

    override fun substituteGenerics(nameMap: Map<String, TactTypeEx>): TactTypeEx {
        return TactFunctionTypeEx(
            params.map { it.substituteGenerics(nameMap) },
            result?.substituteGenerics(nameMap),
            signature
        )
    }

    fun isSelfMethod(): Boolean {
        return params.isNotEmpty() && signature.parameters.paramDefinitionList[0].name?.contains("self") ?: false
    }

    companion object {
        fun from(signatureOwner: TactSignatureOwner): TactFunctionTypeEx? {
            val signature = signatureOwner.getSignature() ?: return null
            val params = signature.parameters.paramDefinitionList.map { it.type.toEx() }
            val result = signature.result?.type?.toEx()
            return TactFunctionTypeEx(params, result, signature)
        }
    }
}
