package org.tonstudio.tact.ide.build.project

import com.intellij.execution.configuration.EnvironmentVariablesComponent
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.*
import javax.swing.JComponent
import javax.swing.JPanel

open class TactBuildConfigurationEditor(private val project: Project) : SettingsEditor<TactBuildConfiguration>() {
    data class Model(
        var fileName: String = "",
        var projectName: String = "",
    )

    private val environmentVariables = EnvironmentVariablesComponent()
    private lateinit var mainPanel: DialogPanel
    private val model = Model()

    init {
        environmentVariables.label.text = "Environment:"
    }

    override fun resetEditorFrom(demoRunConfiguration: TactBuildConfiguration) {
        with(model) {
            fileName = demoRunConfiguration.fileName
            projectName = demoRunConfiguration.projectName
        }

        mainPanel.reset()
    }

    override fun applyEditorTo(demoRunConfiguration: TactBuildConfiguration) {
        mainPanel.apply()

        with(demoRunConfiguration) {
            fileName = model.fileName
            projectName = model.projectName
        }
    }

    override fun createEditor(): JComponent = component()

    private fun component(): JPanel {
        mainPanel = panel {
            row("Config File:") {
                textFieldWithBrowseButton(
                    FileChooserDescriptorFactory.createSingleFileDescriptor("tact.config.json").withTitle("Select Tact Config File"),
                    project,
                )
                    .align(AlignX.FILL)
                    .bindText(model::fileName)
            }.bottomGap(BottomGap.NONE)

            row("Project name:") {
                textField()
                    .align(AlignX.FILL)
                    .bindText(model::projectName)
                    .comment("Project name to build")
            }.bottomGap(BottomGap.NONE)
        }
        return mainPanel
    }
}
