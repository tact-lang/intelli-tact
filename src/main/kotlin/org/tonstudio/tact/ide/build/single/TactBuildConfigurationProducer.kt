package org.tonstudio.tact.ide.build.single

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.openapi.roots.TestSourcesFilter
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import org.tonstudio.tact.lang.psi.TactFile

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

        if (containingFile !is TactFile) {
            return false
        }

        return configuration.fileName == containingFile.virtualFile.path
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

        if (containingFile !is TactFile) {
            return false
        }

        configuration.name = "Build single contract ${containingFile.name}"
        configuration.fileName = containingFile.virtualFile.path
        return true
    }
}
