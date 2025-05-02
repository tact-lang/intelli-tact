package org.tonstudio.tact.configurations

import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.openapi.util.Condition
import com.intellij.openapi.util.Disposer
import com.intellij.ui.JBColor
import com.intellij.ui.dsl.builder.*
import com.intellij.ui.layout.ValidationInfoBuilder
import com.intellij.util.ui.JBDimension
import org.tonstudio.tact.projectWizard.TactToolchainFlavor
import org.tonstudio.tact.toolchain.TactKnownToolchainsState
import org.tonstudio.tact.toolchain.TactToolchain
import org.tonstudio.tact.utils.toPath
import java.io.File
import java.nio.file.Path
import javax.swing.JComponent
import javax.swing.JLabel
import kotlin.io.path.exists
import kotlin.io.path.isDirectory

class TactNewToolchainDialog(private val toolchainFilter: Condition<Path>, project: Project) : DialogWrapper(project) {
    data class Model(
        var toolchainLocation: String,
        var toolchainVersion: String,
        var stdlibLocation: String,
        var modulesLocation: String,
    )

    private val model: Model = Model("", "N/A", "", "")
    private val mainPanel: DialogPanel
    private val toolchainVersion = JLabel()
    private val toolchainIconLabel = JLabel()
    private val pathToToolchainComboBox = TactToolchainPathChoosingComboBox { onToolchainLocationChanged() }

    init {
        title = "New Tact Toolchain"
        setOKButtonText("Add")

        mainPanel = panel {
            row("Location:") {
                cell(pathToToolchainComboBox)
                    .align(AlignX.FILL)
                    .validationOnApply { validateToolchainPath() }
            }
            row("Version:") {
                cell(toolchainVersion)
                    .bind(JLabel::getText, JLabel::setText, model::toolchainVersion.toMutableProperty())
                    .gap(RightGap.SMALL)
                    .apply {
                        component.foreground = JBColor.RED
                    }
                cell(toolchainIconLabel)
            }
            row("Standard library:") {
                textField()
                    .align(AlignX.FILL)
                    .bindText(model::stdlibLocation)
                    .enabled(false)
            }
        }

        pathToToolchainComboBox.addToolchainsAsync {
            TactToolchainFlavor.getApplicableFlavors().flatMap { it.suggestHomePaths(project) }.distinct().
                    filter { toolchainFilter.value(it) }
        }

        val disposable = Disposer.newDisposable()
        mainPanel.registerValidators(disposable)

        init()
    }

    override fun getPreferredFocusedComponent(): JComponent = pathToToolchainComboBox

    override fun createCenterPanel(): JComponent {
        return mainPanel.apply {
            preferredSize = JBDimension(450, height)
        }
    }

    override fun doOKAction() {
        if (TactKnownToolchainsState.getInstance().isKnown(model.toolchainLocation)) {
            setErrorText("This toolchain is already added")
            return
        }

        TactKnownToolchainsState.getInstance().add(TactToolchain.fromPath(model.toolchainLocation))

        super.doOKAction()
    }

    fun addedToolchain(): String? {
        return if (exitCode == OK_EXIT_CODE) model.toolchainLocation else null
    }

    private fun onToolchainLocationChanged() {
        model.toolchainLocation = pathToToolchainComboBox.selectedPath ?: ""

        model.toolchainVersion = TactConfigurationUtil.guessToolchainVersion(model.toolchainLocation)

        if (model.toolchainVersion != TactConfigurationUtil.UNDEFINED_VERSION) {
            model.stdlibLocation = TactConfigurationUtil.getStdlibLocation(model.toolchainLocation) ?: ""
        } else {
            model.stdlibLocation = ""
        }

        mainPanel.reset()

        if (model.toolchainVersion == TactConfigurationUtil.UNDEFINED_VERSION) {
            toolchainVersion.foreground = JBColor.RED
            toolchainIconLabel.icon = null
        } else {
            toolchainVersion.foreground = JBColor.foreground()
            toolchainIconLabel.icon = AllIcons.General.InspectionsOK
        }
    }

    private fun ValidationInfoBuilder.validateToolchainPath(): ValidationInfo? {
        if (model.toolchainLocation.isEmpty()) {
            return error("Toolchain location is required")
        }

        val toolchainPath = model.toolchainLocation.toPath()
        if (!toolchainPath.exists()) {
            return error("Toolchain location is invalid, $toolchainPath not exist")
        }

        if (!toolchainPath.isDirectory()) {
            return error("Toolchain location must be a directory")
        }

        val version = TactConfigurationUtil.guessToolchainVersion(model.toolchainLocation)
        if (version == TactConfigurationUtil.UNDEFINED_VERSION) {
            return error("Toolchain location is invalid, can't get version. Please check that Tact compiler contains in ${model.toolchainLocation} folder")
        }

        val file = File(model.stdlibLocation)
        if (!file.exists() || !file.isDirectory) {
            return error("Standard library location is invalid, $file not exist or not a directory")
        }

        return null
    }
}
