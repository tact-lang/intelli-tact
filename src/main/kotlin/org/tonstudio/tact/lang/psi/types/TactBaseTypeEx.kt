package org.tonstudio.tact.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.findPsiFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import org.tonstudio.tact.lang.psi.*
import org.tonstudio.tact.lang.psi.impl.TactLangUtil
import org.tonstudio.tact.toolchain.TactToolchainService.Companion.toolchainSettings

@Suppress("PropertyName")
abstract class TactBaseTypeEx(protected val anchor: PsiElement? = null) : UserDataHolderBase(), TactTypeEx {
    protected val UNKNOWN_TYPE = "unknown"
    protected val ANON = "anon"
    protected val containingFile = anchor?.containingFile as? TactFile
    protected open val moduleName = containingFile?.getModuleQualifiedName() ?: ""

    override fun anchor(project: Project) = anchor

    override fun module() = moduleName

    override fun name(): String {
        return qualifiedName().removePrefix(moduleName).removePrefix(".")
    }

    override fun isBuiltin() = moduleName == "stubs"

    override fun containingModule(project: Project): PsiDirectory? {
        val anchor = anchor ?: return null
        return anchor.containingFile.containingDirectory
    }

    protected fun String?.safeAppend(str: String?): String {
        if (str == null) return this ?: ""
        return if (this == null) str else this + str
    }

    protected fun String?.safeAppendIf(cond: Boolean, str: String?): String {
        if (!cond) return this ?: ""
        if (str == null) return this ?: ""
        return if (this == null) str else this + str
    }

    protected fun String?.safeAppend(type: TactTypeEx?): String {
        return this.safeAppend(type?.toString())
    }

    protected fun TactTypeEx?.safeAppend(str: String): String {
        return this?.toString().safeAppend(str)
    }

    protected fun prioritize(context: PsiElement?, variants: Collection<TactNamedElement>): TactNamedElement? {
        val containingFile = context?.containingFile?.originalFile
        val containingDir = containingFile?.containingDirectory
        val priorityMap = mutableMapOf<Int, TactNamedElement>()

        variants.forEach { variant ->
            val variantContainingFile = variant.containingFile?.originalFile as? TactFile ?: return@forEach
            val variantContainingDir = variantContainingFile.containingDirectory

            val priority = when {
                variantContainingFile == containingFile                     -> 1000 // local variant has the highest priority
                variantContainingDir == containingDir                       -> 100 // same directory variant has the second-highest priority
                variantContainingFile.virtualFile.path.contains("examples") -> 10
                else                                                        -> 0 // other variants have the lowest priority
            }

            priorityMap[priority] = variant
        }

        // find the highest priority
        val maxPriority = priorityMap.keys.maxOrNull() ?: 0
        return priorityMap[maxPriority]
    }

    override fun ownMethodsList(project: Project): List<TactNamedElement> {
        val anchor = anchor
        if (anchor is TactType) {
            return TactLangUtil.getMethodListNative(project, anchor)
        }
        val resolvedAnchor = anchor(project)
        if (resolvedAnchor is TactType) {
            return TactLangUtil.getMethodListNative(project, resolvedAnchor)
        }

        return TactLangUtil.getMethodList(project, this)
    }

    override fun methodsList(project: Project, visited: MutableSet<TactTypeEx>): List<TactNamedElement> {
        if (this in visited) return emptyList()
        val ownMethods = this.ownMethodsList(project)
        visited.add(this)
        return ownMethods
    }

    override fun findMethod(project: Project, name: String): TactNamedElement? {
        return methodsList(project).find { it.name == name }
    }

    companion object {
        val TactTypeEx?.isAny: Boolean
            get() = when (this) {
                is TactAnyTypeEx     -> true
                is TactUnknownTypeEx -> true
                else                 -> false
            }

        fun TactType?.toEx(visited: MutableMap<TactType, TactTypeEx> = mutableMapOf()): TactTypeEx {
            if (this == null) {
                return TactUnknownTypeEx.INSTANCE
            }

            if (this in visited) {
                return visited[this]!!
            }

            if (visited.isEmpty()) {
                return toExNoVisited()
            }

            val type = toExInner(visited)

            visited[this] = type
            return type
        }

        private fun TactType.toExNoVisited(): TactTypeEx {
            return CachedValuesManager.getCachedValue(this) {
                val value = toExNoVisitedImpl()
                CachedValueProvider.Result.create(value, this)
            }
        }

        private fun TactType.toExNoVisitedImpl(): TactTypeEx {
            return toExInner(mutableMapOf())
        }

        private fun TactType.toExInner(visited: MutableMap<TactType, TactTypeEx>): TactTypeEx {
            val (type, extra) = resolveType()
            if (type is TactStructType && type.parent is TactStructDeclaration) {
                return withExtra(TactStructTypeEx(parentName(type), type), extra)
            }

            return when (type) {
                is TactTraitType     -> withExtra(TactTraitTypeEx(parentName(type), type), extra)
                is TactContractType  -> withExtra(TactContractTypeEx(parentName(type), type), extra)
                is TactMessageType   -> withExtra(TactMessageTypeEx(parentName(type), type), extra)
                is TactPrimitiveType -> {
                    val name = type.identifier.let { it?.text }
                    if (name == null) {
                        return TactUnknownTypeEx.INSTANCE
                    }
                    val primitiveType = TactPrimitiveTypes.find(name) ?: return TactUnknownTypeEx.INSTANCE
                    withExtra(TactPrimitiveTypeEx(primitiveType, type), extra)
                }

                is TactMapType       -> withExtra(TactMapTypeEx(type.keyType.toEx(visited), type.valueType.toEx(visited), type), extra)
                is TactBouncedType   -> TactBouncedTypeEx(type.type.toEx(visited), type)
                is TactTupleType     -> withExtra(
                    TactTupleTypeEx(
                        (type.typeListNoPin?.typeList ?: emptyList()).map { it.toEx(visited) },
                        type
                    ), extra
                )

                else                 -> {
                    val primitive = TactPrimitiveTypes.find(type.text)
                    if (primitive != null) {
                        return withExtra(TactPrimitiveTypeEx(primitive, type), extra)
                    }

                    // only for tests
                    // TODO: remove
                    if (type.text == "String") {
                        return TactPrimitiveTypeEx.STRING
                    }
                    if (type.text == "Int") {
                        return TactPrimitiveTypeEx.INT
                    }
                    if (type.text == "Bool") {
                        return TactPrimitiveTypeEx.BOOL
                    }
                    TactUnknownTypeEx.INSTANCE
                }
            }
        }

        private fun withExtra(type: TactTypeEx, extra: List<TactTypeExtra>): TactTypeEx {
            if (extra.isEmpty()) {
                return type
            }

            var resultType = type

            extra.forEach { extraType ->
                if (extraType.question != null) {
                    val anchor = type.anchor(extraType.project) ?: return@forEach
                    resultType = TactOptionTypeEx(type, anchor)
                }

                if (extraType.tlb != null) {
                    val tlb = extraType.tlb ?: return@forEach
                    val ref = tlb.typeReferenceExpression
                    val typeName = ref?.text
                    if (type is TactPrimitiveTypeEx) {
                        type.tlbType = typeName
                    }
                }
            }

            return resultType
        }

        private fun parentName(type: PsiElement) = (type.parent as TactNamedElement).name!!

        fun stateInitType(project: Project): TactStructTypeEx {
            val errorFile = resolveStdlibPath(project, "std/internal/contract.tact") ?: error("Cannot find contract.tact file in stdlib")
            val errorDecl = errorFile.getStructs().find { it.name == "StateInit" } ?: error("Cannot find StateInit struct in contract.tact")
            return errorDecl.getType(null) as TactStructTypeEx
        }

        private fun resolveStdlibPath(project: Project, path: String): TactFile? {
            val stdlib = project.toolchainSettings.toolchain().stdlibDir()
            val fullPath = "$stdlib/$path"
            return VirtualFileManager.getInstance().findFileByUrl(fullPath)?.findPsiFile(project) as? TactFile
        }
    }
}
