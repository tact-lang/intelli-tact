@file:Suppress("UNUSED_PARAMETER")

package org.tonstudio.tact.lang.psi.impl

import com.intellij.codeInsight.highlighting.ReadWriteAccessDetector.Access
import com.intellij.openapi.util.*
import com.intellij.psi.*
import com.intellij.psi.impl.source.tree.LeafElement
import com.intellij.psi.scope.PsiScopeProcessor
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.*
import org.tonstudio.tact.compiler.crc16
import org.tonstudio.tact.lang.TactTypes.DOT
import org.tonstudio.tact.lang.psi.*
import org.tonstudio.tact.lang.psi.impl.TactTypeInferer.inferVariableType
import org.tonstudio.tact.lang.psi.types.TactBaseTypeEx.Companion.toEx
import org.tonstudio.tact.lang.psi.types.TactFunctionTypeEx
import org.tonstudio.tact.lang.psi.types.TactTypeEx
import org.tonstudio.tact.lang.stubs.index.TactContractsTraitsIndex
import org.tonstudio.tact.lang.usages.TactReadWriteAccessDetector
import org.tonstudio.tact.utils.stubOrPsiParentOfType
import kotlin.Pair

object TactPsiImplUtil {
    private val detector = TactReadWriteAccessDetector()

    @JvmStatic
    fun getName(o: TactFunctionDeclaration): String {
        val stub = o.stub
        if (stub != null) {
            return stub.name ?: ""
        }

        val name = o.getIdentifier().text ?: ""
        return name.removePrefix("AnyMessage_").removePrefix("AnyStruct_")
    }

    @JvmStatic
    fun getName(o: TactStructDeclaration): String {
        val stub = o.stub
        if (stub != null) {
            return stub.name ?: ""
        }

        return o.getIdentifier()?.text ?: ""
    }

    @JvmStatic
    fun getName(o: TactMessageDeclaration): String {
        val stub = o.stub
        if (stub != null) {
            return stub.name ?: ""
        }

        return o.getIdentifier()?.text ?: ""
    }

    @JvmStatic
    fun getName(o: TactTraitDeclaration): String {
        val stub = o.stub
        if (stub != null) {
            return stub.name ?: ""
        }

        return o.getIdentifier()?.text ?: ""
    }

    @JvmStatic
    fun getName(o: TactPrimitiveDeclaration): String {
        val stub = o.stub
        if (stub != null) {
            return stub.name ?: ""
        }

        return o.getIdentifier()?.text ?: ""
    }

    @JvmStatic
    fun getName(o: TactContractDeclaration): String {
        val stub = o.stub
        if (stub != null) {
            return stub.name ?: ""
        }

        return o.getIdentifier()?.text ?: ""
    }

    @JvmStatic
    fun getName(o: TactMessageFunctionDeclaration): String {
        val stub = o.stub
        if (stub != null) {
            return stub.name ?: ""
        }

        return ""
    }

    @JvmStatic
    fun nameLike(o: TactMessageFunctionDeclaration): String {
        val kind = o.messageKind.text
        val parameters = o.parameters?.paramDefinitionList ?: emptyList()
        if (kind == null) {
            return "unknown()"
        }

        if (parameters.isEmpty()) {
            return "$kind()"
        }

        return "$kind(${parameters.joinToString(", ") { it.text }})"
    }

    @JvmStatic
    fun getName(o: TactContractInitDeclaration): String {
        val stub = o.stub
        if (stub != null) {
            return stub.name ?: ""
        }

        return ""
    }

    @JvmStatic
    fun getName(o: TactConstDeclaration): String {
        val stub = o.stub
        if (stub != null) {
            return stub.name ?: ""
        }

        return o.getIdentifier()?.text ?: ""
    }

    @JvmStatic
    fun getExpressionText(o: TactConstDeclaration): String {
        val stub = o.stub
        if (stub != null) {
            return stub.value
        }

        return o.expression?.text ?: ""
    }

    @JvmStatic
    fun getExpressionType(o: TactConstDeclaration): String {
        val stub = o.stub
        if (stub != null) {
            return stub.type
        }

        return o.expression?.getType(null)?.name() ?: ""
    }

    @JvmStatic
    fun getIdentifier(o: TactStructDeclaration): PsiElement? {
        return o.structType.identifier
    }

    @JvmStatic
    fun getIdentifier(o: TactMessageDeclaration): PsiElement? {
        return o.messageType.identifier
    }

    @JvmStatic
    fun getIdentifier(o: TactMessageType): PsiElement? {
        return o.ident?.identifier
    }

    @JvmStatic
    fun getIdentifier(o: TactTraitDeclaration): PsiElement? {
        return o.traitType.identifier
    }

    @JvmStatic
    fun getIdentifier(o: TactPrimitiveDeclaration): PsiElement? {
        return o.primitiveType.identifier
    }

    @JvmStatic
    fun getIdentifier(o: TactContractDeclaration): PsiElement? {
        return o.contractType.identifier
    }

    @JvmStatic
    fun getIdentifier(o: TactMessageFunctionDeclaration): PsiElement? {
        return null
    }

    @JvmStatic
    fun getIdentifier(o: TactContractInitDeclaration): PsiElement? {
        return null
    }

    @JvmStatic
    fun getIdentifier(o: TactType): PsiElement? {
        return o.typeReferenceExpression
    }

    @JvmStatic
    fun getIdentifier(o: TactFieldName): PsiElement {
        return o.referenceExpression.getIdentifier()
    }

    @JvmStatic
    fun getQualifier(o: TactFieldName): TactCompositeElement? {
        return null
    }

    @JvmStatic
    fun resolve(o: TactFieldName): PsiElement? {
        return o.referenceExpression.resolve()
    }

    @JvmStatic
    fun getReference(o: TactReferenceExpression): TactReference {
        return TactReference(o)
    }

    @JvmStatic
    fun getReference(o: TactTypeReferenceExpression): TactReference {
        return TactReference(o, forTypes = true)
    }

    @JvmStatic
    fun getFieldList(o: TactStructType): List<TactFieldDefinition> {
        return o.fieldDefinitionList
    }

    @JvmStatic
    fun getFieldList(o: TactContractType): List<TactFieldDefinition> {
        val parameters = o.contractParameters?.fieldDefinitionList ?: emptyList()
        return o.fieldDefinitionList + parameters
    }

    @JvmStatic
    fun getFieldList(o: TactTraitType): List<TactFieldDefinition> {
        return o.fieldDefinitionList
    }

    @JvmStatic
    fun getMethodsList(o: TactTraitType): List<TactFunctionDeclaration> {
        return o.functionDeclarationList
    }

    @JvmStatic
    fun getMethodsList(o: TactContractType): List<TactFunctionDeclaration> {
        return o.functionDeclarationList
    }

    @JvmStatic
    fun getConstantsList(o: TactTraitType): List<TactConstDeclaration> {
        return o.constDeclarationList
    }

    @JvmStatic
    fun getConstantsList(o: TactContractType): List<TactConstDeclaration> {
        return o.constDeclarationList
    }

    @JvmStatic
    fun getInheritedTraits(o: TactTraitType): List<TactTraitDeclaration> {
        return o.getInheritedTraitsBase()
    }

    @JvmStatic
    fun getInheritedTraits(o: TactContractType): List<TactTraitDeclaration> {
        return o.getInheritedTraitsBase()
    }

    @JvmStatic
    fun TactStorageMembersOwner.getInheritedTraitsBase(visited: MutableSet<String> = mutableSetOf()): List<TactTraitDeclaration> {
        val parent = parent as? TactNamedElement ?: return emptyList()
        val name = parent.name ?: return emptyList()
        if (name == "BaseTrait") return emptyList()

        if (visited.contains(name)) {
            return emptyList()
        }
        visited.add(name)

        val baseTrait = findBaseTrait(this)

        val traitsNode = getWithClause()
        if (traitsNode == null) {
            if (baseTrait != null) {
                return listOf(baseTrait)
            }
            return emptyList()
        }

        val traitsList = traitsNode.typeList
        val traits = traitsList
            .mapNotNull { it.typeReferenceExpression?.resolve() as? TactTraitDeclaration }
            .filter { it.name != name }

        return traits + traits.flatMap { it.traitType.getInheritedTraitsBase(visited) }
    }

    private fun findBaseTrait(anchor: PsiElement): TactTraitDeclaration? {
        return CachedValuesManager.getCachedValue(anchor) {
            val result = TactContractsTraitsIndex.find("BaseTrait", anchor.project, null).firstOrNull() as? TactTraitDeclaration
            CachedValueProvider.Result(result, PsiModificationTracker.MODIFICATION_COUNT)
        }
    }

    @JvmStatic
    fun getFieldList(o: TactMessageType): List<TactFieldDefinition> {
        return o.fieldDefinitionList
    }

    @JvmStatic
    fun isExported(o: TactFieldDefinition): Boolean = true

    @JvmStatic
    fun getOwner(o: TactFieldDefinition): TactNamedElement {
        return o.stubOrPsiParentOfType()
            ?: error("Can't find owner for field ${o.name}, field definition must be inside a struct, union or interface")
    }

    @JvmStatic
    fun getQualifier(o: TactReferenceExpression): TactCompositeElement? {
        return PsiTreeUtil.getChildOfType(o, TactExpression::class.java)
    }

    @JvmStatic
    fun getQualifier(o: TactTypeReferenceExpression): TactCompositeElement? {
        return PsiTreeUtil.getStubChildOfType(o, TactTypeReferenceExpression::class.java)
    }

    @JvmStatic
    fun isConcreteType(type: TactType): Boolean {
        return type.javaClass != TactTypeImpl::class.java
    }

    @JvmStatic
    fun resolveType(type: TactType): Pair<TactType, List<TactTypeExtra>> {
        if (isConcreteType(type)) {
            return type to emptyList()
        }

        val resolved = type.typeReferenceExpression?.resolve() ?: return type to emptyList()
        val resolvedType = elementToType(resolved) ?: type
        return resolvedType to type.typeExtraList
    }

    private fun elementToType(resolved: PsiElement): TactType? = when (resolved) {
        is TactStructDeclaration    -> resolved.structType
        is TactMessageDeclaration   -> resolved.messageType
        is TactContractDeclaration  -> resolved.contractType
        is TactTraitDeclaration     -> resolved.traitType
        is TactPrimitiveDeclaration -> resolved.primitiveType
        else                        -> null
    }

    @JvmStatic
    fun resolve(o: TactTypeReferenceExpression): PsiElement? {
        return o.reference.resolve()
    }

    @JvmStatic
    fun getQualifier(o: TactFieldDefinition): TactCompositeElement? {
        return null
    }

    @JvmStatic
    fun getQualifiedName(o: TactFieldDefinition): String? {
        val owner = o.parentOfTypes(TactStructDeclaration::class)

        if (owner is TactNamedElement) {
            return owner.getName() + "." + o.name
        }

        return null
    }

    @JvmStatic
    fun getReadWriteAccess(o: TactReferenceExpression): Access {
        val expression = getConsiderableExpression(o)
        val parent = expression.parent
        if (parent is TactAssignmentStatement && PsiTreeUtil.isAncestor(parent.left, expression, false)) {
            // += or =
            return if (parent.assignOp.assign == null) Access.ReadWrite else Access.Write
        }
        return Access.Read
    }

    private fun getConsiderableExpression(element: TactExpression): TactExpression {
        var result = element
        while (true) {
            val parent = result.parent ?: return result
            if (parent is TactParenthesesExpr) {
                result = parent
                continue
            }
            if (parent is TactUnaryExpr) {
                if (parent.mul != null) {
                    result = parent
                    continue
                }
            }
            return result
        }
    }

    @JvmStatic
    fun resolve(o: TactReferenceExpression): PsiElement? {
        return o.reference.resolve()
    }

    @JvmStatic
    fun getIdentifierBounds(o: TactFieldName): Pair<Int, Int> {
        val identifier = o.getIdentifier()
        return Pair(identifier.startOffsetInParent, identifier.textLength)
    }

    @JvmStatic
    fun getIdentifierBounds(o: TactReferenceExpression): Pair<Int, Int> {
        val identifier = o.getIdentifier()
        return Pair(identifier.startOffsetInParent, identifier.textLength)
    }

    @JvmStatic
    fun getIdentifierBounds(o: TactTypeReferenceExpression): Pair<Int, Int> {
        val stub = o.stub
        if (stub != null) {
            return Pair(stub.startOffsetInParent, stub.textLength)
        }
        val identifier = o.getIdentifier()
        return Pair(identifier.startOffsetInParent, identifier.textLength)
    }

    @JvmStatic
    fun getPath(o: TactImportDeclaration): String {
        val stub = o.stub
        if (stub != null) {
            return stub.getText() ?: ""
        }

        return o.stringLiteral?.contents ?: "<unknown>"
    }

    @JvmStatic
    fun isExported(o: TactParamDefinition): Boolean = true

    @JvmStatic
    fun getReferences(literal: TactStringLiteral): Array<out PsiReference> {
        if (literal.textLength < 2) return PsiReference.EMPTY_ARRAY
        val fs = literal.containingFile.originalFile.virtualFile.fileSystem
        val literalValue = literal.contents
        val fromStdlib = literalValue.startsWith("@stdlib")
        val set = TactLiteralFileReferenceSet(literalValue, fromStdlib, literal, 1, fs.isCaseSensitive)
        return set.allReferences
    }

    @JvmStatic
    fun isValidHost(o: TactStringLiteral): Boolean = true

    @JvmStatic
    fun updateText(o: TactStringLiteral, text: String): TactStringLiteral {
        if (text.length <= 2) {
            return o
        }
        val newLiteral = TactElementFactory.createStringLiteral(o.project, text)
        o.replace(newLiteral)
        return newLiteral
    }

    @JvmStatic
    fun createLiteralTextEscaper(o: TactStringLiteral): StringLiteralEscaper<TactStringLiteral> {
        return StringLiteralEscaper(o)
    }

    @JvmStatic
    fun getContents(o: TactStringLiteral): String {
        return o.text.substring(1, o.text.length - 1)
    }

    @JvmStatic
    fun withSelf(o: TactSignature): Boolean {
        return o.parameters.paramDefinitionList.firstOrNull()?.name == "self"
    }

    @JvmStatic
    fun getType(o: TactResult, context: ResolveState?): TactTypeEx {
        return o.type.toEx()
    }

    @JvmStatic
    fun getArguments(o: TactCallExpr): List<TactExpression> {
        return o.argumentList.expressionList
    }

    /**
     * For example, for `foo.bar()` it returns `bar`
     *
     * For `foo()` it returns `foo`
     */
    @JvmStatic
    fun getIdentifier(o: TactCallExpr): PsiElement? {
        val refExpr = o.expression as? TactReferenceExpression ?: return null
        return refExpr.getIdentifier()
    }

    /**
     * For example, for `foo.bar()` it returns `foo`
     *
     * For `foo()` it returns null
     *
     * For `foo.bar.baz()` it returns `foo.bar`
     */
    @JvmStatic
    fun getQualifier(o: TactCallExpr): TactReferenceExpression? {
        val refExpr = o.expression as? TactReferenceExpression ?: return null
        return refExpr.getQualifier() as? TactReferenceExpression
    }

    @JvmStatic
    fun resolve(o: TactCallExpr): PsiElement? {
        return (o.expression as? TactReferenceExpression)?.resolve()
    }

    @JvmStatic
    fun paramIndexOf(o: TactCallExpr, pos: PsiElement): Int {
        val element = pos.parentOfType<TactExpression>()
        val args = o.argumentList.expressionList
        return args.indexOf(element)
    }

    @JvmStatic
    fun resolveSignature(o: TactCallExpr): Pair<TactSignature, TactSignatureOwner>? {
        val ty = o.expression?.getType(null)
        if (ty is TactFunctionTypeEx) {
            val owner = ty.signature.parentOfType<TactSignatureOwner>() ?: return null
            return ty.signature to owner
        }

        val resolved = o.resolve() ?: return null
        if (resolved !is TactSignatureOwner) return null
        val signature = resolved.getSignature() ?: return null
        return signature to resolved
    }

    @JvmStatic
    fun resolve(o: TactInitOfExpr): List<TactFieldOrParameter>? {
        val contractDecl = o.typeReferenceExpression?.resolve() as? TactContractDeclaration ?: return null

        val contractParameters = contractDecl.contractType.contractParameters
        val init = contractDecl.contractType.contractInitDeclarationList.firstOrNull()

        if (contractParameters != null) {
            return contractParameters.fieldDefinitionList
        }

        if (init != null) {
            return init.parameters?.paramDefinitionList
        }

        return null
    }

    private fun getNotNullElement(vararg elements: PsiElement?): PsiElement? {
        for (e in elements) {
            if (e != null) return e
        }
        return null
    }

    @JvmStatic
    fun getOperator(o: TactBinaryExpr): PsiElement? {
        if (o is TactAndExpr) return o.condAnd
        if (o is TactOrExpr) return o.condOr

        if (o is TactMulExpr) {
            return getNotNullElement(o.mul, o.quotient, o.remainder, o.bitAnd, o.shiftLeft, o.shiftRight)
        }
        if (o is TactAddExpr) {
            return getNotNullElement(o.bitXor, o.bitOr, o.minus, o.plus)
        }
        if (o is TactConditionalExpr) {
            return getNotNullElement(o.eq, o.notEq, o.greater, o.greaterOrEqual, o.less, o.lessOrEqual)
        }

        return null
    }

    @JvmStatic
    fun getTypeInner(o: TactFieldDefinition, context: ResolveState?): TactTypeEx {
        return o.type.toEx()
    }

    @JvmStatic
    fun getTypeInner(o: TactConstDeclaration, context: ResolveState?): TactTypeEx? {
        val expr = o.expression ?: return null
        if (expr.text == o.name) return null
        return expr.inferType(context)
    }

    @JvmStatic
    fun getTypeInner(o: TactStructDeclaration, context: ResolveState?): TactTypeEx {
        return o.structType.toEx()
    }

    @JvmStatic
    fun getTypeInner(o: TactMessageDeclaration, context: ResolveState?): TactTypeEx {
        return o.messageType.toEx()
    }

    @JvmStatic
    fun getTypeInner(o: TactTraitDeclaration, context: ResolveState?): TactTypeEx {
        return o.traitType.toEx()
    }

    @JvmStatic
    fun getTypeInner(o: TactContractDeclaration, context: ResolveState?): TactTypeEx {
        return o.contractType.toEx()
    }

    @JvmStatic
    fun getTypeInner(o: TactPrimitiveDeclaration, context: ResolveState?): TactTypeEx {
        return o.primitiveType.toEx()
    }

    @JvmStatic
    fun getType(expr: TactExpression, context: ResolveState?): TactTypeEx? {
        return RecursionManager.doPreventingRecursion(expr, true) {
            if (context != null) return@doPreventingRecursion expr.inferType(context)

            CachedValuesManager.getCachedValue(expr) {
                CachedValueProvider.Result
                    .create(
                        expr.inferType(ResolveState.initial()),
                        PsiModificationTracker.MODIFICATION_COUNT,
                    )
            }
        }
    }

    @JvmStatic
    fun isNumeric(o: TactLiteral): Boolean {
        return o.int != null || o.bin != null || o.hex != null || o.oct != null
    }

    @JvmStatic
    fun isBoolean(o: TactLiteral): Boolean {
        return o.`true` != null || o.`false` != null
    }

    @JvmStatic
    fun intValue(o: TactLiteral): Int {
        val text = o.text.replace("_", "").trim()
        if (o.bin != null) {
            return text.removePrefix("0b").toInt(2)
        }
        if (o.oct != null) {
            return text.removePrefix("0o").toInt(8)
        }
        if (o.hex != null) {
            return text.removePrefix("0x").toInt(16)
        }
        if (o.int != null) {
            return text.toInt()
        }
        return 0
    }

    @JvmStatic
    fun getName(o: TactVarDefinition): String {
        return o.getIdentifier().text
    }

    @JvmStatic
    fun isExported(o: TactVarDefinition): Boolean = true

    @JvmStatic
    fun getReference(o: TactVarDefinition): PsiReference {
        return TactVarReference(o)
    }

    @JvmStatic
    fun isReadonly(def: TactVarDefinition): Boolean {
        return CachedValuesManager.getCachedValue(def) {
            val result = isReadonlyImpl(def)
            CachedValueProvider.Result(result, PsiModificationTracker.MODIFICATION_COUNT)
        }
    }

    @JvmStatic
    fun isReadonlyImpl(def: TactVarDefinition): Boolean {
        val usages = ReferencesSearch.search(def, def.useScope)
        return usages.all {
            detector.getReferenceAccess(def, it) == Access.Read
        }
    }

    @JvmStatic
    fun getTypeInner(o: TactSignatureOwner, context: ResolveState?): TactTypeEx? {
        return TactFunctionTypeEx.from(o)
    }

    @JvmStatic
    fun isGet(o: TactFunctionDeclaration): Boolean {
        return o.functionAttributeList.any { it.getAttribute != null }
    }

    @JvmStatic
    fun isAbstract(o: TactFunctionDeclaration): Boolean {
        return o.functionAttributeList.any { it.abstract != null }
    }

    @JvmStatic
    fun isVirtual(o: TactFunctionDeclaration): Boolean {
        return o.functionAttributeList.any { it.virtual != null }
    }

    @JvmStatic
    fun computeMethodId(o: TactFunctionDeclaration): Pair<String, Boolean> {
        val getAttribute = o.functionAttributeList.map { it.getAttribute }.firstOrNull()
        if (getAttribute != null) {
            val value = getAttribute.expression
            if (value != null) {
                // explicit ID
                return value.text to true
            }
        }

        return "0x" + ((crc16(o.name) and 0xFF_FF) or 0x1_00_00).toString(16) to false
    }

    @JvmStatic
    fun getTypeInner(def: TactVarDefinition, context: ResolveState?): TactTypeEx? {
        return inferVariableType(def, context)
    }

    fun processSignatureOwner(o: TactSignatureOwner, processor: TactScopeProcessorBase): Boolean {
        val signature = o.getSignature() ?: return true
        return processParameters(processor, signature.parameters)
    }

    private fun processParameters(processor: TactScopeProcessorBase, parameters: TactParameters): Boolean {
        return processNamedElements(processor, ResolveState.initial(), parameters.paramDefinitionList)
    }

    fun processNamedElements(
        processor: PsiScopeProcessor,
        state: ResolveState,
        elements: Collection<TactNamedElement>,
    ): Boolean {
        return processNamedElements(processor, state, elements, Conditions.alwaysTrue(), false)
    }

    fun processNamedElements(
        processor: PsiScopeProcessor,
        state: ResolveState,
        elements: Collection<TactNamedElement>,
        condition: Condition<Any>,
        checkContainingFile: Boolean,
    ): Boolean {
        for (definition in elements) {
            if (!condition.value(definition))
                continue
            if (!definition.isValid || checkContainingFile)
                continue
            if (!processor.execute(definition, state))
                return false
        }
        return true
    }

    fun prevDot(e: PsiElement?): Boolean {
        val prev = if (e == null) null else PsiTreeUtil.prevVisibleLeaf(e)
        return prev is LeafElement && (prev as LeafElement).elementType === DOT
    }
}
