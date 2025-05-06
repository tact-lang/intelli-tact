package org.tonstudio.tact.lang.stubs

import org.tonstudio.tact.lang.psi.impl.*

object TactStubFactory {
    @JvmStatic
    fun stubFactory(name: String) = when (name) {
        "FUNCTION_DECLARATION"         -> TactFunctionDeclarationStub.Type(name)
        "FIELD_DEFINITION"             -> TactFieldDefinitionStub.Type(name)
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
        "CONST_DECLARATION"            -> TactConstDeclarationStub.Type(name)
        "VAR_DEFINITION"               -> TactVarDefinitionStub.Type(name)
        "SIGNATURE"                    -> TactSignatureStub.Type(name)
        "PARAMETERS"                   -> TactParametersStub.Type(name)
        "CONTRACT_PARAMETERS"          -> TactContractParametersStub.Type(name)
        "RESULT"                       -> TactResultStub.Type(name)
        "TYPE_REFERENCE_EXPRESSION"    -> TactTypeReferenceExpressionStub.Type(name)
        "ATTRIBUTES"                   -> TactAttributesStub.Type(name)
        "ATTRIBUTE"                    -> TactAttributeStub.Type(name)
        "WITH_CLAUSE"                  -> TactWithClauseStub.Type(name)
        "TUPLE_TYPE"                   -> TactTypeStub.Type(name, ::TactTupleTypeImpl)
        "BOUNCED_TYPE"                 -> TactTypeStub.Type(name, ::TactBouncedTypeImpl)
        "MAP_TYPE"                     -> TactTypeStub.Type(name, ::TactMapTypeImpl)
        "SET_TYPE"                     -> TactTypeStub.Type(name, ::TactSetTypeImpl)
        "STRUCT_TYPE"                  -> TactTypeStub.Type(name, ::TactStructTypeImpl)
        "TYPE"                         -> TactTypeStub.Type(name, ::TactTypeImpl)
        "CONTRACT_TYPE"                -> TactTypeStub.Type(name, ::TactContractTypeImpl)
        "TRAIT_TYPE"                   -> TactTypeStub.Type(name, ::TactTraitTypeImpl)
        "MESSAGE_TYPE"                 -> TactTypeStub.Type(name, ::TactMessageTypeImpl)
        "PRIMITIVE_TYPE"               -> TactTypeStub.Type(name, ::TactPrimitiveTypeImpl)

        else                           -> error("Unknown element type: $name")
    }
}
