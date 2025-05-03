package org.tonstudio.tact.compiler

import java.nio.ByteBuffer
import java.security.MessageDigest

enum class ExitCodeFormat { DECIMAL, HEX }

fun requireFunctionExitCode(
    content: String,
    exitCodeFormat: ExitCodeFormat = ExitCodeFormat.DECIMAL,
): String? {
    if (content.isBlank()) return null

    val hash = MessageDigest.getInstance("SHA-256")
        .digest(content.toByteArray())

    val firstFour = ByteBuffer.wrap(hash).int
    val unsigned = firstFour.toLong() and 0xFFFF_FFFFL
    val code = ((unsigned % 63_000) + 1_000).toInt()

    return when (exitCodeFormat) {
        ExitCodeFormat.HEX     -> "0x" + code.toString(16).uppercase()
        ExitCodeFormat.DECIMAL -> code.toString()
    }
}
