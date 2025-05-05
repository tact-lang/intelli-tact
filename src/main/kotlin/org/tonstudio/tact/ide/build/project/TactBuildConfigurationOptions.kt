package org.tonstudio.tact.ide.build.project

import com.intellij.execution.configurations.RunConfigurationOptions

class TactBuildConfigurationOptions : RunConfigurationOptions() {
    private var _fileName = string("").provideDelegate(this, "fileNameRunConfiguration")
    private var _projectName = string("").provideDelegate(this, "projectNameRunConfiguration")

    var fileName: String
        get() = _fileName.getValue(this) ?: ""
        set(value) {
            _fileName.setValue(this, value)
        }

    var projectName: String
        get() = _projectName.getValue(this) ?: ""
        set(value) {
            _projectName.setValue(this, value)
        }
}
