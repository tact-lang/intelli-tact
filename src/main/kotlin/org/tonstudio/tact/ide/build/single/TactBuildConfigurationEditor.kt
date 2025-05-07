package org.tonstudio.tact.ide.build.single

import com.intellij.execution.configuration.EnvironmentVariablesComponent
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.*
import org.tonstudio.tact.lang.TactFileType
import javax.swing.JComponent
import javax.swing.JPanel

@Suppress("UnstableApiUsage")
open class TactBuildConfigurationEditor(private val project: Project) : SettingsEditor<TactBuildConfiguration>() {
    data class Model(
        var fileName: String = "",
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
        }

        mainPanel.reset()
    }

    override fun applyEditorTo(demoRunConfiguration: TactBuildConfiguration) {
        mainPanel.apply()

        with(demoRunConfiguration) {
            fileName = model.fileName
        }
    }

    override fun createEditor(): JComponent = component()

    private fun component(): JPanel {
        mainPanel = panel {
            row("File:") {
                textFieldWithBrowseButton(
                    FileChooserDescriptorFactory.createSingleFileDescriptor(TactFileType).withTitle("Select Tact File"),
                    project,
                )
                    .align(AlignX.FILL)
                    .bindText(model::fileName)
            }.bottomGap(BottomGap.NONE)
        }
        return mainPanel
    }
}
