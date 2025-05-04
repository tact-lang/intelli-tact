package org.tonstudio.tact.lang.psi.impl

import com.intellij.codeInsight.completion.CompletionUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import org.tonstudio.tact.lang.psi.*
import org.tonstudio.tact.lang.psi.types.*
import org.tonstudio.tact.lang.stubs.index.TactMethodIndex
import org.tonstudio.tact.utils.parentNth
import org.tonstudio.tact.utils.parentOfTypeWithStop

object TactLangUtil {
    fun takeZeroArguments(owner: TactSignatureOwner): Boolean {
        return owner.getSignature()?.parameters?.paramDefinitionList?.isEmpty() ?: false
    }

    fun takeSingleArgument(owner: TactSignatureOwner): Boolean {
        return owner.getSignature()?.parameters?.paramDefinitionList?.size == 1
    }

    fun findCallExpr(element: PsiElement): TactCallExpr? {
        if (element.parent is TactFieldName) {
            return element.parent.parentNth(4)
        }

        val parentValue = element.parentOfTypeWithStop<TactValue>(TactSignatureOwner::class)
        if (parentValue != null) {
            return parentValue.parentNth(3) ?: element.parentOfTypeWithStop(TactSignatureOwner::class)
        }
        return element.parentOfTypeWithStop(TactSignatureOwner::class)
    }

    fun sameModule(firstFile: PsiFile, secondFile: PsiFile): Boolean {
        if (firstFile == secondFile) return true

        val containingDirectory = firstFile.containingDirectory
        return !(containingDirectory == null || containingDirectory != secondFile.containingDirectory)
    }

    /**
     * Use TactTypeEx.methodsList(project, visited)
     */
    fun getMethodList(project: Project, type: TactTypeEx): List<TactNamedElement> {
        return CachedValuesManager.getManager(project).getCachedValue(type) {
            CachedValueProvider.Result.create(
                calcMethods(project, type), PsiModificationTracker.MODIFICATION_COUNT
            )
        }
    }

    private fun calcMethods(project: Project, type: TactTypeEx): List<TactNamedElement> {
        val typeName = getTypeName(type)

        val scope = GlobalSearchScope.allScope(project)

        val declarations = TactMethodIndex.find(typeName, project, scope)
        if (declarations.isEmpty()) return emptyList()
        if (declarations.size == 1 || type !is TactResolvableTypeEx<*>) {
            return declarations.toList()
        }
        return declarations.toList()
    }

    fun getMethodListNative(project: Project, type: TactType): List<TactNamedElement> {
        val spec = CompletionUtil.getOriginalOrSelf(type)
        return CachedValuesManager.getCachedValue(spec) {
            CachedValueProvider.Result.create(
                calcMethodsNative(project, spec), PsiModificationTracker.MODIFICATION_COUNT
            )
        }
    }

    private fun calcMethodsNative(project: Project, type: TactType): List<TactNamedElement> {
        val typeName = getTypeNameNative(type)
        if (typeName.isEmpty()) return emptyList()

        val scope = GlobalSearchScope.allScope(project)

        val declarations = TactMethodIndex.find(typeName, project, scope)
        if (declarations.isEmpty()) return emptyList()
        if (declarations.size == 1 || type !is TactResolvableTypeEx<*>) {
            return declarations.toList()
        }

        return declarations.toList()
    }

    private fun getTypeNameNative(o: TactType): String {
        if (o is TactMapType) {
            return "map"
        }

        return o.identifier?.text ?: ""
    }

    private fun getTypeName(o: TactTypeEx): String {
        if (o is TactOptionTypeEx) {
            return getTypeName(o.inner)
        }

        return o.toString()
    }

    fun getDefaultValue(element: PsiElement, type: TactTypeEx?): String = when (type) {
        is TactPrimitiveTypeEx -> {
            when (type.name) {
                TactPrimitiveTypes.BOOL           -> "false"
                TactPrimitiveTypes.INT            -> "0"
                TactPrimitiveTypes.BUILDER        -> "beginCell()"
                TactPrimitiveTypes.CELL           -> "emptyCell()"
                TactPrimitiveTypes.ADDRESS        -> "sender()"
                TactPrimitiveTypes.SLICE          -> "emptySlice()"
                TactPrimitiveTypes.STRING         -> "\"\""
                TactPrimitiveTypes.STRING_BUILDER -> "beginString()"
            }
        }

        is TactMapTypeEx       -> "emptyMap()"
        is TactFunctionTypeEx  -> "null"
        is TactStructTypeEx    -> type.readableName(element) + " {}"
        is TactTraitTypeEx     -> type.readableName(element) + " {}"
        is TactNullTypeEx      -> "null"
        is TactOptionTypeEx    -> "null"
        is TactAnyTypeEx       -> "0"
        else                   -> "0"
    }
}
