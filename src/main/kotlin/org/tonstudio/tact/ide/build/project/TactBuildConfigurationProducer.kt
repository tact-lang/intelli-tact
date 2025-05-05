package org.tonstudio.tact.ide.build.project

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonProperty
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.openapi.roots.TestSourcesFilter
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement

class TactBuildConfigurationProducer : LazyRunConfigurationProducer<TactBuildConfiguration>() {
    override fun getConfigurationFactory(): ConfigurationFactory =
        ConfigurationTypeUtil.findConfigurationType(TactBuildConfigurationType.ID)!!
            .configurationFactories[0]

    override fun isConfigurationFromContext(
        configuration: TactBuildConfiguration,
        context: ConfigurationContext,
    ): Boolean {
        val element = context.location?.psiElement ?: return false
        val containingFile = element.containingFile ?: return false
        if (TestSourcesFilter.isTestSources(containingFile.virtualFile, element.project)) {
            return false
        }

        if (containingFile !is JsonFile || containingFile.name != "tact.config.json") {
            return false
        }

        val projectName = getProjectName(element) ?: return false

        return configuration.fileName == containingFile.virtualFile.path && configuration.projectName == projectName
    }

    override fun setupConfigurationFromContext(
        configuration: TactBuildConfiguration,
        context: ConfigurationContext,
        sourceElement: Ref<PsiElement>,
    ): Boolean {
        val element = sourceElement.get()
        val containingFile = element.containingFile ?: return false
        if (TestSourcesFilter.isTestSources(containingFile.virtualFile, element.project)) {
            return false
        }

        if (containingFile !is JsonFile || containingFile.name != "tact.config.json") {
            return false
        }

        val configPath = containingFile.virtualFile.path
        val projectName = getProjectName(element) ?: return false

        configuration.name = "Build project \"$projectName\""
        configuration.fileName = configPath
        configuration.projectName = projectName
        return true
    }

    private fun getProjectName(element: PsiElement?): String?{
        val jsonProperty = element?.parent?.parent as? JsonProperty
        val projectName = jsonProperty?.value as? JsonStringLiteral ?: return null
        return projectName.value
    }
}
