package org.tonstudio.tact.lang

import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.vfs.VirtualFile
import org.tonstudio.tact.ide.ui.Icons

object TactFileType : LanguageFileType(TactLanguage) {
    override fun getName() = "Tact"
    override fun getDescription() = "Tact file"
    override fun getDefaultExtension() = "tact"
    override fun getIcon() = Icons.Tact
    override fun getCharset(file: VirtualFile, content: ByteArray): String = "UTF-8"
}
