package org.tonstudio.tact.lang.psi.impl

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileInfoManager
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet
import org.tonstudio.tact.lang.psi.TactFile

class TactFileReference(set: FileReferenceSet, range: TextRange, index: Int, text: String) : FileReference(set, range, index, text) {
    override fun createLookupItem(element: PsiElement): Any {
        val file = element as? TactFile ?: return FileInfoManager.getFileLookupItem(element)
        if (!file.isPhysical) return FileInfoManager.getFileLookupItem(element)
        val withoutExtensions = file.name.removeSuffix(".tact")
        val extension = if (withoutExtensions == file.name) "" else ".tact"
        val root = rangeInElement.startOffset == 1
        val finalName = if (root) "./$withoutExtensions" else withoutExtensions

        return FileInfoManager.getFileLookupItem(file, finalName, file.getIcon(0)).withTailText(extension)
    }
}
