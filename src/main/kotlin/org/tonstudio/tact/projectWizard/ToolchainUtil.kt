package org.tonstudio.tact.projectWizard

import java.nio.file.Path
import kotlin.io.path.isExecutable

fun Path.isJavaScriptExecutable(toolName: String): Boolean =
    resolve(toolName).toAbsolutePath().isExecutable()
