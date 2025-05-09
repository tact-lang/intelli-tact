package org.tonstudio.tact.createtact

import com.intellij.execution.RunManager
import com.intellij.execution.filters.Filter
import com.intellij.ide.util.projectWizard.SettingsStep
import com.intellij.javascript.nodejs.npm.InstallNodeLocalDependenciesAction
import com.intellij.javascript.nodejs.packages.NodePackageUtil
import com.intellij.lang.javascript.boilerplate.NpmPackageProjectGenerator
import com.intellij.lang.javascript.boilerplate.NpxPackageDescriptor
import com.intellij.lang.javascript.buildTools.npm.PackageJsonUtil
import com.intellij.lang.javascript.buildTools.npm.rc.NpmRunConfigurationBuilder
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ContentEntry
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.ProjectGeneratorPeer
import com.intellij.ui.components.JBCheckBox
import com.intellij.util.PathUtil
import org.tonstudio.tact.ide.ui.Icons
import javax.swing.Icon
import javax.swing.JPanel

class CreateTactProjectGenerator() : NpmPackageProjectGenerator() {
    override fun getDescription(): String =
        "Create a new <a href='https://github.com/tact-lang/create-tact'>create-tact</a> project using CLI"

    override fun getId(): String = "create-tact"
    override fun getName(): String = "Tact"
    override fun getIcon(): Icon? = Icons.Tact
    override fun presentablePackageName(): String = "Tact"
    override fun filters(project: Project, file: VirtualFile): Array<Filter> = emptyArray()
    override fun packageName(): String = CREATE_TACT_PACKAGE_NAME
    override fun customizeModule(file: VirtualFile, entry: ContentEntry?) {}

    override fun getNpxCommands(): List<NpxPackageDescriptor.NpxCommand> = listOf(
        NpxPackageDescriptor.NpxCommand(CREATE_TACT_PACKAGE_NAME, CREATE_TACT_PACKAGE_NAME),
    )

    override fun validateProjectPath(path: String): String? {
        val error = NodePackageUtil.validateNpmPackageName(PathUtil.getFileName(path))
        return error ?: super.validateProjectPath(path)
    }

    override fun generatorArgs(project: Project, dir: VirtualFile, settings: Settings): Array<String> {
        val workingDir = if (generateInTemp()) dir.name else "."
        val packageName = settings.myPackage.name
        val initGit = settings.getUserData(INIT_GIT_CODE) ?: false
        val args = arrayOf(
            "--contractName",
            "Main",
            "--skipDepsInstallation",
            "true", // we install deps later
            "--initializeGitRepository",
            initGit.toString(),
            workingDir
        )
        if (packageName.contains(CREATE_TACT_PACKAGE_NAME)) {
            return args
        }
        return arrayOf(CREATE_COMMAND, *args)
    }

    override fun configureProject(project: Project, baseDir: VirtualFile) {
        val packageJson = PackageJsonUtil.findChildPackageJsonFile(baseDir) ?: return

        createRunConfigurations(project, baseDir, packageJson)
        InstallNodeLocalDependenciesAction.runAndShowConsole(project, packageJson)
    }

    private fun createRunConfigurations(project: Project, baseDir: VirtualFile, packageJson: VirtualFile) {
        val build = createRunConfiguration(project, baseDir, packageJson, "Build", "build")
        createRunConfiguration(project, baseDir, packageJson, "Test", "test")
        createRunConfiguration(project, baseDir, packageJson, "Deploy to testnet", "verifier:testnet")
        createRunConfiguration(project, baseDir, packageJson, "Deploy to mainnet", "verifier:mainnet")
        createRunConfiguration(project, baseDir, packageJson, "Format", "fmt")

        RunManager.getInstance(project).selectedConfiguration = build
    }

    private fun createRunConfiguration(
        project: Project,
        baseDir: VirtualFile,
        packageJson: VirtualFile,
        name: String,
        script: String,
    ) = NpmRunConfigurationBuilder(project)
        .createRunConfiguration(name, baseDir, packageJson.path, mapOf("run-script" to script))

    override fun createPeer(): ProjectGeneratorPeer<Settings> {
        return object : NpmPackageGeneratorPeer() {
            private lateinit var initGitCheckbox: JBCheckBox

            override fun createPanel(): JPanel {
                return super.createPanel().apply {
                    initGitCheckbox = JBCheckBox("Initialize Git repository", true).also { add(it) }
                }
            }

            override fun buildUI(settingsStep: SettingsStep) {
                super.buildUI(settingsStep)
                settingsStep.addSettingsComponent(initGitCheckbox)
            }

            override fun getSettings(): Settings {
                return super.getSettings().apply {
                    putUserData(INIT_GIT_CODE, initGitCheckbox.isSelected)
                }
            }
        }
    }

    override fun generateInTemp(): Boolean = false

    companion object {
        const val CREATE_TACT_PACKAGE_NAME = "create-tact"
        const val CREATE_COMMAND = "create"

        private val INIT_GIT_CODE = Key.create<Boolean>("tact.create-tact.init.git.repository")
    }
}
