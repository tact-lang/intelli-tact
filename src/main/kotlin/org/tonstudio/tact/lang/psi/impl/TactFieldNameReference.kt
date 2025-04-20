package org.tonstudio.tact.lang.psi.impl

import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import org.tonstudio.tact.lang.psi.*
import org.tonstudio.tact.lang.psi.impl.TactReferenceBase.Companion.LOCAL_RESOLVE
import org.tonstudio.tact.lang.psi.types.TactMessageTypeEx
import org.tonstudio.tact.lang.psi.types.TactStructTypeEx

class TactFieldNameReference(element: TactReferenceExpressionBase) :
    TactCachedReference<TactReferenceExpressionBase>(element) {

    override fun processResolveVariants(processor: TactScopeProcessor): Boolean {
        val fieldProcessor =
            if (processor is TactFieldProcessor)
                processor
            else
                object : TactFieldProcessor(myElement) {
                    override fun execute(e: PsiElement, state: ResolveState): Boolean {
                        return super.execute(e, state) && processor.execute(e, state)
                    }
                }

        val key = myElement.parentOfType<TactKey>()
        val value = myElement.parentOfType<TactValue>()
        if (key == null && (value == null || PsiTreeUtil.getPrevSiblingOfType(value, TactKey::class.java) != null)) {
            return true
        }

        val type = myElement.parentOfType<TactLiteralValueExpression>()?.getType(null) ?: return true

        val typeFile = type.anchor(project)?.containingFile as? TactFile
        val originFile = element.containingFile as TactFile
        val localResolve = typeFile == null || TactReference.isLocalResolve(typeFile, originFile)

        return when (type) {
            is TactStructTypeEx  -> processStructType(fieldProcessor, type, localResolve)
            is TactMessageTypeEx -> processMessageType(fieldProcessor, type, localResolve)
            else                 -> true
        }
    }

    private fun processStructType(fieldProcessor: TactScopeProcessor, type: TactStructTypeEx?, localResolve: Boolean): Boolean {
        val state = if (localResolve) ResolveState.initial().put(LOCAL_RESOLVE, true) else ResolveState.initial()
        val declaration = type?.resolve(project) ?: return true
        val structType = declaration.structType

        val fields = structType.fieldList
        for (field in fields) {
            if (!fieldProcessor.execute(field, state)) return false
        }

        return true
    }

    private fun processMessageType(fieldProcessor: TactScopeProcessor, type: TactMessageTypeEx?, localResolve: Boolean): Boolean {
        val state = if (localResolve) ResolveState.initial().put(LOCAL_RESOLVE, true) else ResolveState.initial()
        val declaration = type?.resolve(project) ?: return true
        val structType = declaration.messageType

        val fields = structType.fieldList
        for (field in fields) {
            if (!fieldProcessor.execute(field, state)) return false
        }

        return true
    }

    override fun resolveInner(): PsiElement? {
        val p = TactFieldProcessor(myElement)
        processResolveVariants(p)
        return p.getResult()
    }

    private open class TactFieldProcessor(element: PsiElement) : TactScopeProcessorBase(element) {
        private val myModule: Module?

        init {
            val containingFile = origin.containingFile
            myModule = ModuleUtilCore.findModuleForPsiElement(containingFile.originalFile)
        }

        override fun crossOff(e: PsiElement): Boolean {
            if (e !is TactFieldDefinition) return true
            return !e.isValid
        }
    }
}
