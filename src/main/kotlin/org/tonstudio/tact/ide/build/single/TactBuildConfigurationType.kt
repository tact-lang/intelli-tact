package org.tonstudio.tact.ide.build.single

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.openapi.project.Project
import org.tonstudio.tact.ide.ui.Icons

class TactBuildConfigurationType : ConfigurationTypeBase(
    ID, "Tact Build single contract",
    "Build single Tact contract",
    Icons.Tact
) {
    companion object {
        const val ID = "TactBuild"
    }

    init {
        addFactory(object : ConfigurationFactory(this) {
            override fun getId() = ID

            override fun createTemplateConfiguration(project: Project) =
                TactBuildConfiguration(project, this, "Build Tact contract")

            override fun getOptionsClass() = TactBuildConfigurationOptions::class.java
        })
    }
}
