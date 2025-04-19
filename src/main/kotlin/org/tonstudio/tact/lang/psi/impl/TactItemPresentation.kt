package org.tonstudio.tact.lang.psi.impl

import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.util.Iconable
import com.intellij.psi.PsiElement
import org.tonstudio.tact.lang.psi.TactFile
import javax.swing.Icon

abstract class TactItemPresentation<T : PsiElement>(protected val element: T) : ItemPresentation {
    private var locationString: String? = null

    override fun getLocationString(): String {
        return if (locationString != null) locationString!! else getLocationStringInner().also { locationString = it }
    }

    private fun getLocationStringInner(): String {
        val file = element.containingFile
        val fileName = file.name
        val importPath = if (file is TactFile) file.getModuleQualifiedName().replace(".", "/") else null
        return "in " + if (importPath != null) "$importPath/$fileName" else fileName
    }

    override fun getIcon(b: Boolean): Icon =
        element.getIcon(Iconable.ICON_FLAG_VISIBILITY)
}
