package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.util.ReflectionUtil
import org.tonstudio.tact.lang.psi.TactType
import org.tonstudio.tact.lang.psi.impl.*
import org.tonstudio.tact.lang.stubs.types.*

object TactElementTypeFactory {
    private val TYPES: Map<String, Class<*>> = object : HashMap<String, Class<*>>() {
        init {
            put("TUPLE_TYPE", TactTupleTypeImpl::class.java)
            put("BOUNCED_TYPE", TactBouncedTypeImpl::class.java)
            put("MAP_TYPE", TactMapTypeImpl::class.java)
            put("SET_TYPE", TactSetTypeImpl::class.java)
            put("STRUCT_TYPE", TactStructTypeImpl::class.java)
            put("TYPE", TactTypeImpl::class.java)
            put("CONTRACT_TYPE", TactContractTypeImpl::class.java)
            put("TRAIT_TYPE", TactTraitTypeImpl::class.java)
            put("MESSAGE_TYPE", TactMessageTypeImpl::class.java)
            put("PRIMITIVE_TYPE", TactPrimitiveTypeImpl::class.java)
        }
    }

    @JvmStatic
    fun stubFactory(name: String) = when (name) {
        "FUNCTION_DECLARATION"         -> TactFunctionDeclarationStub.Type(name)
        "FIELD_DEFINITION"             -> TactFieldDefinitionStub.Type(name)
        "FIELD_DECLARATION"            -> TactFieldDeclarationStub.Type(name)
        "STRUCT_DECLARATION"           -> TactStructDeclarationStub.Type(name)
        "MESSAGE_DECLARATION"          -> TactMessageDeclarationStub.Type(name)
        "CONTRACT_DECLARATION"         -> TactContractDeclarationStub.Type(name)
        "TRAIT_DECLARATION"            -> TactTraitDeclarationStub.Type(name)
        "PRIMITIVE_DECLARATION"        -> TactPrimitiveDeclarationStub.Type(name)
        "MESSAGE_FUNCTION_DECLARATION" -> TactMessageFunctionDeclarationStub.Type(name)
        "CONTRACT_INIT_DECLARATION"    -> TactContractInitDeclarationStub.Type(name)
        "ASM_FUNCTION_DECLARATION"     -> TactAsmFunctionDeclarationStub.Type(name)
        "NATIVE_FUNCTION_DECLARATION"  -> TactNativeFunctionDeclarationStub.Type(name)
        "IMPORT_DECLARATION"           -> TactImportDeclarationStub.Type(name)
        "PARAM_DEFINITION"             -> TactParamDefinitionStub.Type(name)
        "CONST_DEFINITION"             -> TactConstDefinitionStub.Type(name)
        "VAR_DEFINITION"               -> TactVarDefinitionStub.Type(name)
        "SIGNATURE"                    -> TactSignatureStub.Type(name)
        "PARAMETERS"                   -> TactParametersStub.Type(name)
        "CONTRACT_PARAMETERS"          -> TactContractParametersStub.Type(name)
        "RESULT"                       -> TactResultStub.Type(name)
        "TYPE_REFERENCE_EXPRESSION"    -> TactTypeReferenceExpressionStub.Type(name)
        "ATTRIBUTES"                   -> TactAttributesStub.Type(name)
        "ATTRIBUTE"                    -> TactAttributeStub.Type(name)
        "ATTRIBUTE_EXPRESSION"         -> TactAttributeExpressionStub.Type(name)
        "PLAIN_ATTRIBUTE"              -> TactPlainAttributeStub.Type(name)
        "ATTRIBUTE_KEY"                -> TactAttributeKeyStub.Type(name)
        "WITH_CLAUSE"                  -> TactWithClauseStub.Type(name)

        else                           -> {
            val c = TYPES[name] ?: throw RuntimeException("Unknown element type: $name")

            object : TactTypeStubElementType(name) {
                override fun createPsi(stub: TactTypeStub): TactType {
                    return try {
                        ReflectionUtil.createInstance(
                            c.getConstructor(stub::class.java, IStubElementType::class.java),
                            stub,
                            this
                        ) as TactType
                    } catch (e: NoSuchMethodException) {
                        throw RuntimeException(e)
                    }
                }
            }
        }
    }
}
