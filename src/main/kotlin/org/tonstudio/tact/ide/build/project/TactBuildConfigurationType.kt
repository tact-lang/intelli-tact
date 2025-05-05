package org.tonstudio.tact.ide.build.project

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.openapi.project.Project
import org.tonstudio.tact.ide.ui.Icons

class TactBuildConfigurationType : ConfigurationTypeBase(
    ID, "Tact Build project",
    "Build Tact project",
    Icons.Tact
) {
    companion object {
        const val ID = "TactBuildProject"
    }

    init {
        addFactory(object : ConfigurationFactory(this) {
            override fun getId() = ID

            override fun createTemplateConfiguration(project: Project) =
                TactBuildConfiguration(project, this, "Build Tact project")

            override fun getOptionsClass() = TactBuildConfigurationOptions::class.java
        })
    }
}
