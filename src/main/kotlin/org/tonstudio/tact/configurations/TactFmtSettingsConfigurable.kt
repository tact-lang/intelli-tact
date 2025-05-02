package org.tonstudio.tact.configurations

import com.intellij.execution.configuration.EnvironmentVariablesComponent
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.*
import org.tonstudio.tact.configurations.TactFmtSettingsState.Companion.tactfmtSettings

class TactFmtSettingsConfigurable(private val project: Project) : Configurable {
    data class Model(
        var additionalArguments: String,
        var envs: Map<String, String>,
        var runTactfmtOnSave: Boolean,
    )

    private val mainPanel: DialogPanel
    private val model = Model(
        additionalArguments = "",
        envs = emptyMap(),
        runTactfmtOnSave = false,
    )

    private val environmentVariables = EnvironmentVariablesComponent()

    init {
        mainPanel = panel {
            row("Additional arguments:") {
                expandableTextField()
                    .align(AlignX.FILL)
                    .bindText(model::additionalArguments)
                    .comment("Additional arguments to pass to <b>tact-fmt</b> command")
            }
            row(environmentVariables.label) {
                cell(environmentVariables)
                    .align(AlignX.FILL)
                    .bind(
                        componentGet = { it.envs },
                        componentSet = { component, value -> component.envs = value },
                        prop = model::envs.toMutableProperty()
                    )
            }
            row {
                checkBox("Run tactfmt on Save")
                    .bindSelected(model::runTactfmtOnSave)
            }
        }
    }

    override fun getDisplayName() = "Tactfmt"
    override fun getPreferredFocusedComponent() = mainPanel
    override fun createComponent() = mainPanel

    override fun isModified(): Boolean {
        mainPanel.apply()

        val settings = project.tactfmtSettings
        return model.additionalArguments != settings.additionalArguments ||
                model.envs != settings.envs ||
                model.runTactfmtOnSave != settings.runTactfmtOnSave
    }

    override fun apply() {
        mainPanel.apply()

        val settings = project.tactfmtSettings
        with(settings) {
            additionalArguments = model.additionalArguments
            envs = model.envs
            runTactfmtOnSave = model.runTactfmtOnSave
        }
    }

    override fun reset() {
        val settings = project.tactfmtSettings

        with(model) {
            additionalArguments = settings.additionalArguments
            envs = settings.envs
            runTactfmtOnSave = settings.runTactfmtOnSave
        }

        mainPanel.reset()
    }
}
