package org.tonstudio.tact.lang.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.util.parentOfType
import org.tonstudio.tact.lang.psi.*
import org.tonstudio.tact.lang.psi.types.TactMessageTypeEx
import org.tonstudio.tact.lang.psi.types.TactStructTypeEx

class TactFieldNameReference(element: TactReferenceExpressionBase) : TactCachedReference<TactReferenceExpressionBase>(element) {
    override fun processResolveVariants(processor: TactScopeProcessor): Boolean {
        val fieldProcessor = createProcessor(processor)

        val instanceExpression = myElement.parentOfType<TactLiteralValueExpression>() ?: return false
        val type = instanceExpression.getType(null) ?: return true

        return when (type) {
            is TactStructTypeEx  -> processStructType(fieldProcessor, type)
            is TactMessageTypeEx -> processMessageType(fieldProcessor, type)
            else                 -> true
        }
    }

    private fun processStructType(fieldProcessor: TactScopeProcessor, type: TactStructTypeEx?): Boolean {
        val state = ResolveState.initial()
        val declaration = type?.resolve(project) ?: return true
        return processFields(fieldProcessor, state, declaration.structType.fieldList)
    }

    private fun processMessageType(fieldProcessor: TactScopeProcessor, type: TactMessageTypeEx?): Boolean {
        val state = ResolveState.initial()
        val declaration = type?.resolve(project) ?: return true
        return processFields(fieldProcessor, state, declaration.messageType.fieldList)
    }

    private fun processFields(
        fieldProcessor: TactScopeProcessor,
        state: ResolveState,
        fields: List<TactFieldDefinition>,
    ): Boolean {
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

    private fun createProcessor(processor: TactScopeProcessor) =
        if (processor is TactFieldProcessor) processor
        else object : TactFieldProcessor(myElement) {
            override fun execute(e: PsiElement, state: ResolveState): Boolean {
                return super.execute(e, state) && processor.execute(e, state)
            }
        }

    private open class TactFieldProcessor(element: PsiElement) : TactScopeProcessorBase(element) {
        override fun crossOff(e: PsiElement): Boolean {
            if (e !is TactFieldDefinition) return true
            return !e.isValid
        }
    }
}
