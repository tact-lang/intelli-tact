package org.tonstudio.tact.configurations

import com.intellij.openapi.diagnostic.logger
import java.nio.file.Path

object TactConfigurationUtil {
    private val LOG = logger<TactConfigurationUtil>()

    const val TOOLCHAIN_NOT_SETUP = "Tact executable not found, toolchain not setup correctly?"
    const val UNDEFINED_VERSION = "N/A"
    const val STANDARD_LIB_PATH = "std"
    val STANDARD_TACT_COMPILER = "./.bin/tact"

    fun getStdlibLocation(path: String): String? {
        if (path.isBlank()) {
            return null
        }
        return Path.of(path, STANDARD_LIB_PATH).toString()
    }

    fun guessToolchainVersion(path: String): String {
        return "" // TODO: implement
    }
}
