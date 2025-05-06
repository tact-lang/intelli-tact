package org.tonstudio.tact.lang.structure

import com.intellij.ide.structureView.*
import com.intellij.ide.structureView.StructureViewModel.ElementInfoProvider
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase
import com.intellij.ide.util.treeView.smartTree.Filter
import com.intellij.lang.PsiStructureViewFactory
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.IndexNotReadyException
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.tonstudio.tact.lang.psi.*
import org.tonstudio.tact.lang.psi.types.TactBaseTypeEx.Companion.toEx

class TactStructureViewFactory : PsiStructureViewFactory {
    override fun getStructureViewBuilder(psiFile: PsiFile): StructureViewBuilder {
        return object : TreeBasedStructureViewBuilder() {
            override fun createStructureViewModel(editor: Editor?): StructureViewModel = Model(psiFile, editor)
            override fun isRootNodeShown() = false
        }
    }

    class Model(file: PsiFile, editor: Editor?) : StructureViewModelBase(file, editor, Element(file)), ElementInfoProvider {
        init {
            withSuitableClasses(TactFile::class.java, TactNamedElement::class.java)
        }

        override fun getFilters() = arrayOf<Filter>()

        override fun isAlwaysShowsPlus(element: StructureViewTreeElement) = false

        override fun isAlwaysLeaf(element: StructureViewTreeElement) = false
    }

    class Element(element: PsiElement) : PsiTreeElementBase<PsiElement>(element) {
        override fun getChildrenBase(): MutableCollection<StructureViewTreeElement> {
            val result = mutableListOf<StructureViewTreeElement>()
            val element = element

            if (element is TactFile) {
                element.getPrimitives().forEach { result.add(Element(it)) }
                element.getFunctions().forEach { result.add(Element(it)) }
                element.getAsmFunctions().forEach { result.add(Element(it)) }
                element.getNativeFunctions().forEach { result.add(Element(it)) }
                element.getMessages().forEach { result.add(Element(it)) }
                element.getStructs().forEach { result.add(Element(it)) }
                element.getTraits().forEach { result.add(Element(it)) }
                element.getContracts().forEach { result.add(Element(it)) }
                element.getConstants().forEach { result.add(Element(it)) }
            }

            val type = when (element) {
                is TactPrimitiveDeclaration -> element.primitiveType
                is TactStructDeclaration    -> element.structType
                is TactMessageDeclaration   -> element.messageType
                is TactContractDeclaration  -> element.contractType
                is TactTraitDeclaration     -> element.traitType
                else                        -> null
            }?.toEx()

            if (type != null) {
                type.methodsList(element!!.project).forEach { result.add(Element(it)) }

                if (type is TactFieldListOwner) {
                    type.fieldList.forEach { result.add(Element(it)) }
                }
            }

            return result
        }

        override fun getPresentableText(): String? {
            val element = element

            if (element is TactFile) {
                return element.name
            }

            if (element is TactNamedElement && element is TactSignatureOwner) {
                val name = element.name
                val signature = element.getSignature() ?: return null
                return name + signature.text
            }

            if (element is TactConstDeclaration) {
                val type = try {
                    element.getType(null)
                } catch (ignored: IndexNotReadyException) {
                    null
                }
                if (type == null) return element.name
                return "${element.name}: ${type.readableName(element)}"
            }

            if (element is TactType) {
                return element.toEx().readableName(element)
            }

            if (element is TactNamedElement) {
                return element.name
            }

            return null
        }
    }
}
