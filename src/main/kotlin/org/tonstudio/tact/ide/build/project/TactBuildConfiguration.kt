package org.tonstudio.tact.ide.build.project

import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.LocatableConfiguration
import com.intellij.execution.configurations.RunConfigurationBase
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.project.Project
import org.jdom.Element

open class TactBuildConfiguration(project: Project, factory: ConfigurationFactory?, name: String?) :
    RunConfigurationBase<TactBuildConfigurationOptions>(project, factory, name),
    LocatableConfiguration {

    override fun getOptions() = super.getOptions() as TactBuildConfigurationOptions

    var fileName: String
        get() = options.fileName
        set(value) {
            options.fileName = value
        }

    var projectName: String
        get() = options.projectName
        set(value) {
            options.projectName = value
        }

    override fun writeExternal(element: Element) {
        super<RunConfigurationBase>.writeExternal(element)

        with(element) {
            writeString("fileName", fileName)
            writeString("projectName", projectName)
        }
    }

    override fun isGeneratedName(): Boolean {
        return false
    }

    override fun readExternal(element: Element) {
        super<RunConfigurationBase>.readExternal(element)

        with(element) {
            readString("fileName")?.let { fileName = it }
            readString("projectName")?.let { projectName = it }
        }
    }

    override fun getConfigurationEditor() = TactBuildConfigurationEditor(project)

    override fun checkConfiguration() {}

    override fun getState(executor: Executor, executionEnvironment: ExecutionEnvironment): RunProfileState? {
        return TactBuildConfigurationRunState(executionEnvironment, this)
    }

    private fun Element.writeString(name: String, value: String) {
        val opt = Element("option")
        opt.setAttribute("name", name)
        opt.setAttribute("value", value)
        addContent(opt)
    }

    private fun Element.readString(name: String): String? =
        children
            .find { it.name == "option" && it.getAttributeValue("name") == name }
            ?.getAttributeValue("value")
}
