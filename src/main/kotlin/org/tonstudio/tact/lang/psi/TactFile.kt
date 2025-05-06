package org.tonstudio.tact.lang.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.util.ArrayFactory
import org.tonstudio.tact.ide.ui.Icons
import org.tonstudio.tact.lang.TactFileType
import org.tonstudio.tact.lang.TactLanguage
import org.tonstudio.tact.lang.TactTypes
import org.tonstudio.tact.lang.psi.impl.ResolveUtil
import org.tonstudio.tact.lang.psi.impl.TactElementFactory
import org.tonstudio.tact.lang.stubs.*
import org.tonstudio.tact.lang.stubs.types.*

open class TactFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, TactLanguage) {
    override fun getFileType() = TactFileType

    override fun toString() = "Tact Language file"

    override fun getIcon(flags: Int) = Icons.Tact

    override fun processDeclarations(
        processor: PsiScopeProcessor,
        state: ResolveState,
        lastParent: PsiElement?,
        place: PsiElement,
    ): Boolean {
        return ResolveUtil.processChildren(this, processor, state, lastParent, place)
    }

    fun fromStubs(): Boolean {
        return virtualFile.path.endsWith("stubs.tact")
    }

    fun getModuleQualifiedName(): String = "" // TODO: remove

    fun addImport(path: String): TactImportDeclaration? {
        if (getImportedModulesMap().containsKey(path)) {
            return getImportedModulesMap()[path]!!
        }

        return addImportImpl(path)
    }

    private fun getImportedModulesMap(): Map<String, TactImportDeclaration> {
        return CachedValuesManager.getCachedValue(this) {
            val map = mutableMapOf<String, TactImportDeclaration>()
            for (spec in getImports()) {
                val path = spec.path
                map[path] = spec
            }
            CachedValueProvider.Result.create(map, this)
        }
    }

    fun getImportList(): TactImportList? {
        return findChildByClass(TactImportList::class.java)
    }

    fun getImports(): List<TactImportDeclaration> {
        return CachedValuesManager.getCachedValue(this) {
            CachedValueProvider.Result.create(
                getImportsImpl(),
                this,
            )
        }
    }

    private fun getImportsImpl(): List<TactImportDeclaration> {
        return findChildByClass(TactImportList::class.java)
            ?.importDeclarationList
            ?: emptyList()
    }

    fun getFunctions() = getNamedElements(TactTypes.FUNCTION_DECLARATION, getArrayFactory<TactFunctionDeclaration>())
    fun getAsmFunctions() = getNamedElements(TactTypes.ASM_FUNCTION_DECLARATION, getArrayFactory<TactAsmFunctionDeclaration>())
    fun getNativeFunctions() = getNamedElements(TactTypes.NATIVE_FUNCTION_DECLARATION, getArrayFactory<TactNativeFunctionDeclaration>())
    fun getStructs() = getNamedElements(TactTypes.STRUCT_DECLARATION, getArrayFactory<TactStructDeclaration>())
    fun getMessages() = getNamedElements(TactTypes.MESSAGE_DECLARATION, getArrayFactory<TactMessageDeclaration>())
    fun getContracts() = getNamedElements(TactTypes.CONTRACT_DECLARATION, getArrayFactory<TactContractDeclaration>())
    fun getTraits() = getNamedElements(TactTypes.TRAIT_DECLARATION, getArrayFactory<TactTraitDeclaration>())
    fun getPrimitives() = getNamedElements(TactTypes.PRIMITIVE_DECLARATION, getArrayFactory<TactPrimitiveDeclaration>())
    fun getConstants() = getNamedElements(TactTypes.CONST_DECLARATION, getArrayFactory<TactConstDeclaration>())

    private inline fun <reified T : PsiElement> getNamedElements(elementType: IElementType, arrayFactory: ArrayFactory<T>): List<T> {
        return CachedValuesManager.getCachedValue(this) {
            val stub = stub

            if (stub == null) {
                val elements = mutableListOf<T>()
                for (topLevelDeclaration in this.children) {
                    if (topLevelDeclaration is T) {
                        elements.add(topLevelDeclaration)
                    }
                }
                return@getCachedValue CachedValueProvider.Result.create(elements, this)
            }

            val elements = getChildrenByType(stub, elementType, arrayFactory)
            CachedValueProvider.Result.create(elements, this)
        }
    }

    private inline fun <reified T> getArrayFactory() = ArrayFactory<T> { count: Int ->
        if (count == 0) arrayOfNulls(0) else arrayOfNulls<T>(count)
    }

    fun getNames(): Set<String> {
        return CachedValuesManager.getCachedValue(this) {
            val stub = stub

            if (stub == null) {
                val names = mutableSetOf<String>()
                for (topLevelDeclaration in this.children) {
                    if (topLevelDeclaration is TactNamedElement && topLevelDeclaration.name != null) {
                        names.add(topLevelDeclaration.name!!)
                    }
                }
                return@getCachedValue CachedValueProvider.Result.create(names, this)
            }

            val names = stub.childrenStubs.mapNotNull { if (it is TactNamedStub<*>) it.name else null }.toSet()
            CachedValueProvider.Result.create(names, this)
        }
    }

    private fun <E : PsiElement?> getChildrenByType(
        stub: StubElement<out PsiElement>,
        elementType: IElementType,
        f: ArrayFactory<E>,
    ): List<E> {
        return listOf(*stub.getChildrenByType(elementType, f))
    }

    private fun addImportImpl(name: String): TactImportDeclaration {
        val list = getImportList()
        val project = project

        val decl = TactElementFactory.createImportDeclaration(project, name)!!
        if (list == null) {
            var importList = TactElementFactory.createImportList(project, name)!!
            addBefore(TactElementFactory.createDoubleNewLine(project), firstChild)
            importList = addBefore(importList, firstChild) as TactImportList
            return importList.importDeclarationList.first()!!
        }
        return addImportDeclaration(list, decl)
    }

    private fun addImportDeclaration(importList: TactImportList, newImportDeclaration: TactImportDeclaration): TactImportDeclaration {
        val lastImport = importList.importDeclarationList.last()
        val importDeclaration = importList.addAfter(newImportDeclaration, lastImport) as TactImportDeclaration
        val importListNextSibling = importList.nextSibling
        if (importListNextSibling !is PsiWhiteSpace) {
            importList.addAfter(TactElementFactory.createNewLine(importList.project), importDeclaration)
            if (importListNextSibling != null) {
                // double new line if there is something valuable after import list
                importList.addAfter(TactElementFactory.createNewLine(importList.project), importDeclaration)
            }
        }
        importList.addBefore(TactElementFactory.createNewLine(importList.project), importDeclaration)
        return importDeclaration
    }
}