package org.tonstudio.tact.smalllangs

import com.intellij.json.JsonFileType
import com.intellij.json.JsonLanguage
import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object PkgFileType : LanguageFileType(JsonLanguage.INSTANCE) {
    override fun getName(): String = "Tact Package Config"
    override fun getDescription(): String = "Tact Package Config file"
    override fun getDefaultExtension(): String = "pkg"
    override fun getIcon(): Icon = JsonFileType.INSTANCE.icon
}
