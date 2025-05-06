package org.tonstudio.tact.lang.psi.types

import com.intellij.openapi.project.Project
import org.tonstudio.tact.lang.psi.TactSignature
import org.tonstudio.tact.lang.psi.TactSignatureOwner

class TactFunctionTypeEx(val params: List<TactTypeEx>, val result: TactTypeEx?, val signature: TactSignature) : TactBaseTypeEx(signature) {
    override fun toString() = name()

    override fun name(): String = buildString {
        append("fun ")
        append("(")
        append(params.joinToString(", ") { it.name() })
        append(")")
        if (result != null) {
            append(": ")
            append(result.name())
        }
    }

    override fun isAssignableFrom(project: Project, rhs: TactTypeEx, kind: AssignableKind): Boolean {
        if (rhs.isAny) return true
        if (rhs !is TactFunctionTypeEx) return false
        return name() == rhs.name()
    }

    override fun isEqual(rhs: TactTypeEx): Boolean {
        if (rhs !is TactFunctionTypeEx) return false
        return name() == rhs.name()
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
