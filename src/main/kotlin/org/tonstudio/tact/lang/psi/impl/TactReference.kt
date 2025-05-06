package org.tonstudio.tact.lang.psi.impl

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.openapi.util.Conditions
import com.intellij.openapi.util.RecursionManager
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.resolveFromRootOrRelative
import com.intellij.psi.*
import com.intellij.psi.impl.source.resolve.ResolveCache
import com.intellij.psi.util.elementType
import com.intellij.psi.util.parentOfType
import com.intellij.psi.util.parentOfTypes
import com.intellij.util.ArrayUtil
import org.tonstudio.tact.configurations.TactConfiguration
import org.tonstudio.tact.lang.TactTypes
import org.tonstudio.tact.lang.psi.*
import org.tonstudio.tact.lang.psi.impl.TactPsiImplUtil.processNamedElements
import org.tonstudio.tact.lang.psi.types.*
import org.tonstudio.tact.lang.psi.types.TactBaseTypeEx.Companion.toEx
import org.tonstudio.tact.lang.stubs.StubWithText
import org.tonstudio.tact.lang.stubs.index.TactNamesIndex
import org.tonstudio.tact.toolchain.TactToolchainService.Companion.toolchainSettings

class TactReference(el: TactReferenceExpressionBase, val forTypes: Boolean = false) :
    TactReferenceBase<TactReferenceExpressionBase>(
        el,
        TextRange.from(
            el.getIdentifierBounds()?.first ?: 0,
            el.getIdentifierBounds()?.second ?: el.textLength,
        )
    ) {

    companion object {
        private val MY_RESOLVER: ResolveCache.PolyVariantResolver<TactReference> =
            ResolveCache.PolyVariantResolver { ref, _ -> ref.resolveInner() }
    }

    private val identifier: PsiElement?
        get() = myElement?.getIdentifier()

    private val identifierText: String?
        get() {
            val element = myElement
            if (element is StubBasedPsiElementBase<*>) {
                val stub = element.stub
                if (stub != null) {
                    if (stub is StubWithText) {
                        return stub.getText()
                    }
                }
            }

            return identifier?.text
        }

    private fun resolveInner(): Array<ResolveResult> {
        if (!myElement.isValid) return ResolveResult.EMPTY_ARRAY
        val result = mutableSetOf<ResolveResult>()
        processResolveVariants(createResolveProcessor(result, myElement))
        return result.toTypedArray()
    }

    fun processResolveVariants(processor: TactScopeProcessor): Boolean {
        val element = myElement
        val file = element.containingFile as? TactFile ?: return false

        val state = ResolveState.initial()

        val qualifier = element.getQualifier()
        return if (qualifier != null) {
            // foo.bar
            // ^^^ qualifier
            processQualifierExpression(qualifier, element, processor, state)
        } else {
            //  bar()
            // ^ no qualifier
            processUnqualifiedResolve(file, processor, state)
        }
    }

    private fun processQualifierExpression(
        qualifier: TactCompositeElement,
        element: TactReferenceExpressionBase,
        processor: TactScopeProcessor,
        state: ResolveState,
    ): Boolean {
        if (qualifier is TactExpression) {
            val type = qualifier.getType(null)

            if (type != null) {
                if (!processPseudoStaticCall(qualifier, element, type, processor, state)) return false

                if (!processType(type, processor, state)) {
                    return false
                }
            }
        }

        return true
    }

    private fun processPseudoStaticCall(
        qualifier: TactCompositeElement,
        element: TactReferenceExpressionBase,
        type: TactTypeEx?,
        processor: TactScopeProcessor,
        state: ResolveState,
    ): Boolean {
        if (qualifier.elementType != TactTypes.REFERENCE_EXPRESSION) {
            // complex qualifier: foo().bar()
            return true
        }

        if (type !is TactStructTypeEx && type !is TactMessageTypeEx) {
            // pseudo statics only defined on messages and structs
            return true
        }

        val resolvedQualifier = (qualifier as? TactReferenceExpression)?.resolve() ?: return true
        if (resolvedQualifier !is TactStructDeclaration && resolvedQualifier !is TactMessageDeclaration) {
            // case like:
            // let a: Foo = Foo {}
            // a.fromCell()
            // ^ not a static call
            return true
        }

        val searchedName = element.getIdentifier()?.text ?: ""

        val prefix = if (resolvedQualifier is TactStructDeclaration) "AnyStruct_" else "AnyMessage_"

        if (processPseudoStaticCandidate(prefix, "fromSlice", searchedName, processor, state)) return false
        if (processPseudoStaticCandidate(prefix, "fromCell", searchedName, processor, state)) return false
        if (processPseudoStaticCandidate(prefix, "opcode", searchedName, processor, state)) return false

        return false
    }

    private fun processPseudoStaticCandidate(
        prefix: String,
        name: String,
        searchedName: String,
        processor: TactScopeProcessor,
        state: ResolveState,
    ): Boolean {
        val fullName = prefix + name
        val func = findStubsFunction(fullName)
        if (func != null) {
            val newState = state.put(ACTUAL_NAME, fullName).put(SEARCH_NAME, prefix + searchedName)
            if (!processor.execute(func, newState)) return true
        }
        return false
    }

    private fun processType(type: TactTypeEx, processor: TactScopeProcessor, state: ResolveState): Boolean {
        val anchor = type.anchor(project)
        if (anchor != null && !anchor.isValid) {
            return true
        }
        return RecursionManager.doPreventingRecursion(type, true) {
            processExistingType(type, processor, state)
        } ?: true
    }

    private fun processExistingType(typ: TactTypeEx, processor: TactScopeProcessor, state: ResolveState): Boolean {
        if (typ is TactStructTypeEx) {
            val declaration = typ.resolve(project) ?: return true
            val structType = declaration.structType

            if (!processNamedElements(processor, state, structType.fieldList)) return false
            if (!processMethods(typ, processor, state)) return false

            return processAnyStruct(processor, state)
        }

        if (typ is TactMessageTypeEx) {
            val declaration = typ.resolve(project) ?: return true
            val messageType = declaration.messageType

            if (!processNamedElements(processor, state, messageType.fieldList)) return false
            if (!processMethods(typ, processor, state)) return false

            return processAnyMessage(processor, state)
        }

        if (typ is StorageMembersOwnerTy<*>) {
            if (!processNamedElements(processor, state, typ.ownFields())) return false
            if (!processNamedElements(processor, state, typ.methods())) return false
            if (!processNamedElements(processor, state, typ.ownConstants())) return false

            if (typ.name() != "BaseTrait") {
                val baseTrait = TactNamesIndex.find("BaseTrait", project, null).firstOrNull() as? TactTraitDeclaration ?: return true
                if (!processExistingType(baseTrait.traitType.toEx(), processor, state)) return false
            }
        }

        if (typ is TactBouncedTypeEx) {
            return processExistingType(typ.inner, processor, state)
        }

        if (typ is TactOptionTypeEx) {
            return processExistingType(typ.inner, processor, state)
        }

        if (!processMethods(typ, processor, state)) return false

        return true
    }

    private fun processMethods(type: TactTypeEx, processor: TactScopeProcessor, state: ResolveState): Boolean {
        return processNamedElements(processor, state, type.methodsList(project))
    }

    private fun processNativeMethods(type: TactType, processor: TactScopeProcessor, state: ResolveState): Boolean {
        val methods = TactLangUtil.getMethodListNative(project, type)
        return processNamedElements(processor, state, methods)
    }

    private fun processUnqualifiedResolve(
        file: TactFile,
        processor: TactScopeProcessor,
        state: ResolveState,
    ): Boolean {
        val identText = identifierText!!

        if (identText == "_") {
            return processor.execute(myElement, state)
        }

        when (myElement.parent) {
            is TactFieldName    -> {
                return processLiteralValueField(processor, state)
            }

            is TactDestructItem -> {
                val item = myElement.parent as TactDestructItem
                val destruct = myElement.parent.parent as? TactDestructStatement ?: return true
                val resolved = destruct.typeReferenceExpression.resolve() ?: return true

                val type = when (resolved) {
                    is TactStructDeclaration  -> resolved.structType
                    is TactMessageDeclaration -> resolved.messageType
                    else                      -> return false
                }
                val fields = type.fieldList

                val searchName =
                    if (item.referenceExpression != null)
                        item.referenceExpression?.getIdentifier()?.text
                    else
                        item.varDefinition.name

                if (!processNamedElements(processor, state.put(SEARCH_NAME, searchName), fields)) return false
            }

            is TactAsmShuffle   -> {
                val functionDecl = myElement.parentOfType<TactAsmFunctionDeclaration>() ?: return true
                val parameters = functionDecl.getSignature()?.parameters?.paramDefinitionList ?: emptyList()
                return processNamedElements(processor, state, parameters)
            }
        }

        if (!processBlock(processor, state)) return false
        if (!processImportedFiles(file, processor, state)) return false
        if (!processFileEntities(file, processor, state)) return false
        if (!processBuiltin(processor, state)) return false
        if (!processStubs(processor, state)) return false

        if (identText == "self") {
            val owner = identifier!!.parentOfTypes(TactTraitDeclaration::class, TactContractDeclaration::class) ?: return true
            if (!processor.execute(owner, state.put(SEARCH_NAME, owner.name))) return false
        }

        return true
    }

    private fun processBuiltin(processor: TactScopeProcessor, state: ResolveState): Boolean {
        val builtin = myElement.project.toolchainSettings.toolchain().stdlibDir() ?: return true
        val psiManager = PsiManager.getInstance(myElement.project)

        val std = builtin.resolveFromRootOrRelative("./std/internal/")?.children ?: arrayOf<VirtualFile>()
        std
            .map { psiManager.findFile(it) }
            .filterIsInstance<TactFile>()
            .forEach {
                if (!processFileEntities(it, processor, state))
                    return false
            }

        return true
    }

    private fun processAnyStruct(processor: TactScopeProcessor, state: ResolveState): Boolean {
        val stubsPsiFile = findStubsFile() ?: return true
        val anyStruct = stubsPsiFile.getPrimitives().find { it.name == "AnyStruct" } ?: return true
        return processNativeMethods(anyStruct.primitiveType, processor, state)
    }

    private fun processAnyMessage(processor: TactScopeProcessor, state: ResolveState): Boolean {
        val stubsPsiFile = findStubsFile() ?: return true
        val anyStruct = stubsPsiFile.getPrimitives().find { it.name == "AnyMessage" } ?: return true
        return processNativeMethods(anyStruct.primitiveType, processor, state)
    }

    private fun processStubs(processor: TactScopeProcessor, state: ResolveState): Boolean {
        val stubsPsiFile = findStubsFile() ?: return true

        return processNamedElements(
            processor,
            state,
            stubsPsiFile.getFunctions(),
            Conditions.alwaysTrue(),
            checkContainingFile = false
        )
    }

    private fun findStubsFunction(name: String): TactFunctionDeclaration? {
        val stubsPsiFile = findStubsFile() ?: return null
        val functions = stubsPsiFile.getFunctions()
        return functions.find { it.getIdentifier().text == name }
    }

    private fun findStubsFile(): TactFile? {
        val stdlib = TactConfiguration.getInstance(myElement.project).stubsLocation ?: return null
        val psiManager = PsiManager.getInstance(myElement.project)

        val stubsFile = stdlib.findChild("stubs.tact") ?: return null
        return psiManager.findFile(stubsFile) as? TactFile
    }

    private fun processImportedFiles(
        file: TactFile,
        processor: TactScopeProcessor,
        state: ResolveState,
    ): Boolean {
        val imports = file.getImports()
        for (import in imports) {
            val refs = import.stringLiteral?.references ?: continue
            val lastReference = refs.lastOrNull() ?: continue
            val importedFile = lastReference.resolve() as? TactFile ?: continue

            if (!processFileEntities(importedFile, processor, state)) {
                return false
            }
        }
        return true
    }

    private fun processFileEntities(
        file: TactFile,
        processor: TactScopeProcessor,
        state: ResolveState,
    ): Boolean {
        if (!processNamedElements(
                processor,
                state,
                file.getStructs(),
                Conditions.alwaysTrue(),
                false
            )
        ) return false

        if (!processNamedElements(
                processor,
                state,
                file.getMessages(),
                Conditions.alwaysTrue(),
                false
            )
        ) return false

        if (!processNamedElements(
                processor,
                state,
                file.getTraits(),
                Conditions.alwaysTrue(),
                false
            )
        ) return false

        if (!processNamedElements(
                processor,
                state,
                file.getFunctions(),
                Conditions.alwaysTrue(),
                false
            )
        ) return false

        if (!processNamedElements(
                processor,
                state,
                file.getAsmFunctions(),
                Conditions.alwaysTrue(),
                false
            )
        ) return false

        if (!processNamedElements(
                processor,
                state,
                file.getNativeFunctions(),
                Conditions.alwaysTrue(),
                false
            )
        ) return false

        if (!processNamedElements(
                processor,
                state,
                file.getContracts(),
                Conditions.alwaysTrue(),
                false
            )
        ) return false

        if (!processNamedElements(
                processor,
                state,
                file.getPrimitives(),
                Conditions.alwaysTrue(),
                false
            )
        ) return false

        return processNamedElements(
            processor,
            state,
            file.getConstants(),
            Conditions.alwaysTrue(),
            false
        )
    }

    fun processBlock(processor: TactScopeProcessor, state: ResolveState): Boolean {
        val context = myElement
        val delegate = createDelegate(processor)
        ResolveUtil.treeWalkUp(context, delegate)
        return processNamedElements(processor, state, delegate.getVariants())
    }

    private fun processLiteralValueField(processor: TactScopeProcessor, state: ResolveState): Boolean {
        val initExpr = element.parentOfType<TactLiteralValueExpression>()
        val type = initExpr?.type?.toEx() ?: return true
        return processType(type, processor, state)
    }

    private fun createDelegate(processor: TactScopeProcessor): TactVarProcessor {
        return object : TactVarProcessor(identifier!!, myElement, processor.isCompletion()) {
            override fun crossOff(e: PsiElement): Boolean {
                return if (e is TactFieldDefinition)
                    true
                else
                    super.crossOff(e)
            }
        }
    }

    private fun createResolveProcessor(
        result: MutableCollection<ResolveResult>,
        reference: TactReferenceExpressionBase,
    ): TactScopeProcessor {
        return object : TactScopeProcessor() {
            override fun execute(element: PsiElement, state: ResolveState): Boolean {
                if (element == reference) {
                    return !result.add(PsiElementResolveResult(element))
                }

                val name = state.get(ACTUAL_NAME) ?: when (element) {
                    is PsiNamedElement -> element.name
                    else               -> null
                }

                val ident = state.get(SEARCH_NAME) ?: reference.getIdentifier()?.text ?: return true

                if (name != null && ident == name) {
                    result.add(PsiElementResolveResult(element))
                    return false
                }
                return true
            }
        }
    }

    override fun isReferenceTo(element: PsiElement) = couldBeReferenceTo(element, myElement) && super.isReferenceTo(element)

    private fun couldBeReferenceTo(definition: PsiElement, reference: PsiElement): Boolean {
        if (definition is PsiDirectory && reference is TactReferenceExpressionBase) return true

        val definitionFile = definition.containingFile ?: return true
        val referenceFile = reference.containingFile

        val inSameFile = definitionFile.isEquivalentTo(referenceFile)
        if (inSameFile) return true
        return if (TactLangUtil.sameModule(referenceFile, definitionFile))
            true
        else
            reference !is TactNamedElement
    }

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult?> {
        if (!myElement.isValid) return ResolveResult.EMPTY_ARRAY

        return ResolveCache.getInstance(myElement.project)
            .resolveWithCaching(this, MY_RESOLVER, false, false)
    }

    override fun getVariants(): Array<Any> = ArrayUtil.EMPTY_OBJECT_ARRAY

    override fun handleElementRename(newElementName: String): PsiElement? {
        identifier?.replace(TactElementFactory.createIdentifier(myElement.project, newElementName))
        return myElement
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TactReference

        return element == other.element
    }

    override fun hashCode(): Int = element.hashCode()
}
