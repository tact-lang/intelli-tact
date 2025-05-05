package org.tonstudio.tact.ide.build.single

import com.intellij.execution.configurations.RunConfigurationOptions

class TactBuildConfigurationOptions : RunConfigurationOptions() {
    private var _fileName = string("").provideDelegate(this, "fileNameRunConfiguration")

    var fileName: String
        get() = _fileName.getValue(this) ?: ""
        set(value) {
            _fileName.setValue(this, value)
        }
}
