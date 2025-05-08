package org.tonstudio.tact.boc

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon
import org.tonstudio.tact.ide.ui.Icons

object TonBocFileType : LanguageFileType(TonBocLanguage) {
    override fun getName(): String = "TON BoC File"
    override fun getDescription(): String = "TON Blockchain Bag of Cells file"
    override fun getDefaultExtension(): String = "boc"
    override fun getIcon(): Icon = Icons.Boc
}
