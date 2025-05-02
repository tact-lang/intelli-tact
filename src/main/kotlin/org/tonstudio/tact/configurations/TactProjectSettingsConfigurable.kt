package org.tonstudio.tact.configurations

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.util.Disposer
import org.tonstudio.tact.toolchain.TactToolchain
import org.tonstudio.tact.toolchain.TactToolchainService.Companion.toolchainSettings

class TactProjectSettingsConfigurable(private val project: Project) : Configurable {
    private val mainPanel: DialogPanel
    private val model = TactProjectSettingsForm.Model(
        toolchainLocation = "",
    )
    private val settingsForm = TactProjectSettingsForm(project, model)

    init {
        mainPanel = settingsForm.createComponent()

        val disposable = Disposer.newDisposable()
        mainPanel.registerValidators(disposable)
    }

    override fun getDisplayName() = "Tact"
    override fun getPreferredFocusedComponent() = mainPanel
    override fun createComponent() = mainPanel

    override fun isModified(): Boolean {
        mainPanel.apply()

        val settings = project.toolchainSettings
        return model.toolchainLocation != settings.toolchainLocation
    }

    override fun apply() {
        mainPanel.apply()

        validateSettings()

        val settings = project.toolchainSettings
        settings.setToolchain(project, TactToolchain.fromPath(model.toolchainLocation))
    }

    private fun validateSettings() {
        val issues = mainPanel.validateAll()
        if (issues.isNotEmpty()) {
            throw ConfigurationException(issues.first().message)
        }
    }

    override fun reset() {
        val settings = project.toolchainSettings

        with(model) {
            toolchainLocation = settings.toolchainLocation
        }

        settingsForm.reset()
        mainPanel.reset()
    }

    companion object {
        fun show(project: Project) {
            ShowSettingsUtil.getInstance().showSettingsDialog(project, TactProjectSettingsConfigurable::class.java)
        }
    }
}
