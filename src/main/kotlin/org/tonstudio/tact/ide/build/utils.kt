package org.tonstudio.tact.ide.build

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import org.tonstudio.tact.utils.pathAsPath
import kotlin.io.path.Path
import kotlin.io.path.pathString

fun tryRelativizePath(project: Project, path: String): Pair<String, String?> {
    val projectDir = project.guessProjectDir() ?: return path to null
    return projectDir.pathAsPath.relativize(Path(path)).pathString to projectDir.path
}
