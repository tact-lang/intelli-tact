package org.tonstudio.tact.lang.psi.impl

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFileSystemItem
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileTargetContext
import org.tonstudio.tact.lang.TactFileType
import org.tonstudio.tact.lang.psi.TactStringLiteral
import org.tonstudio.tact.toolchain.TactToolchainService.Companion.toolchainSettings

class TactLiteralFileReferenceSet(
    str: String,
    private val fromStdlib: Boolean,
    element: TactStringLiteral,
    startOffset: Int,
    isCaseSensitive: Boolean,
) : FileReferenceSet(str, element, startOffset, null, isCaseSensitive, false, arrayOf(TactFileType)) {
    override fun getTargetContexts(): MutableCollection<FileTargetContext> {
        val contexts = super.getTargetContexts()
        return contexts
    }

    override fun computeDefaultContexts(): MutableCollection<PsiFileSystemItem> {
        val contexts = super.computeDefaultContexts()

        if (fromStdlib) {
            val toolchain = element.project.toolchainSettings.toolchain()
            val stdlibDir = toolchain.stdlibDir() ?: return contexts
            val libsDir = stdlibDir.findChild("libs") ?: return contexts
            val psiFile = PsiManager.getInstance(element.project).findDirectory(libsDir) ?: return contexts
            return mutableListOf(psiFile as PsiFileSystemItem)
        }

        return contexts
    }

    override fun createFileReference(range: TextRange, index: Int, text: String): FileReference {
        if (text == "@stdlib") {
            return FileReference(this, range, index, ".") // we use second context with an absolute path to libs
        }

        // automatically add ".tact" for the last non-directory part
        // foo/bar -> foo/bar.tact
        // foo/bar/ -> foo/bar/
        if (!text.endsWith(".tact") && range.endOffset - 1 == this.pathString.length) {
            return TactFileReference(this, range, index, "$text.tact")
        }

        return TactFileReference(this, range, index, text)
    }
}
