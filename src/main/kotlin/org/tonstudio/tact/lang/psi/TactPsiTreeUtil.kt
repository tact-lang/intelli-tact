@file:Suppress("UNCHECKED_CAST")

package org.tonstudio.tact.lang.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.StubBasedPsiElement
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.SmartList

object TactPsiTreeUtil {
    @JvmStatic
    fun <T : PsiElement?> getStubChildOfType(element: PsiElement?, aClass: Class<T>): T? {
        if (element == null) return null
        val stub = if (element is StubBasedPsiElement<*>) element.stub else null
        if (stub == null) {
            return PsiTreeUtil.getChildOfType(element, aClass)
        }
        for (childStub in stub.childrenStubs) {
            val child = childStub.psi
            if (aClass.isInstance(child)) {
                return child as T
            }
        }
        return null
    }

    @JvmStatic
    inline fun <reified T : StubElement<*>> StubElement<*>.parentStubOfType(): T? {
        var stub: StubElement<*>? = this
        while (stub != null) {
            if (stub is T) {
                return stub
            }
            stub = stub.parentStub
        }
        return null
    }

    @JvmStatic
    fun <T : PsiElement?> getStubChildrenOfTypeAsList(element: PsiElement?, aClass: Class<T>): List<T> {
        if (element == null) return emptyList()
        val stub = (if (element is StubBasedPsiElement<*>) element.stub else null)
            ?: return PsiTreeUtil.getChildrenOfTypeAsList(element, aClass)
        val result: MutableList<T> = SmartList()
        for (childStub in stub.childrenStubs) {
            val child = childStub.psi
            if (aClass.isInstance(child)) {
                result.add(child as T)
            }
        }
        return result
    }

    @JvmStatic
    fun <T : PsiElement?> getChildrenOfTypeAsList(element: PsiElement?, aClass: Class<out T>): List<T> {
        return PsiTreeUtil.getChildrenOfTypeAsList(element, aClass)
    }

    @JvmStatic
    fun <T : PsiElement?> getChildOfType(element: PsiElement?, aClass: Class<T>): T? {
        return PsiTreeUtil.getChildOfType(element, aClass)
    }
}