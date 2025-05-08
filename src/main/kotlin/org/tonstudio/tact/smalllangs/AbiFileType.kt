package org.tonstudio.tact.smalllangs

import com.intellij.json.JsonFileType
import com.intellij.json.JsonLanguage
import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object AbiFileType : LanguageFileType(JsonLanguage.INSTANCE) {
    override fun getName(): String = "Tact Contract ABI"
    override fun getDescription(): String = "Tact Contract ABI file"
    override fun getDefaultExtension(): String = "abi"
    override fun getIcon(): Icon = JsonFileType.INSTANCE.icon
}
