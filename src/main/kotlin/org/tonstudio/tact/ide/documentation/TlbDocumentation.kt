package org.tonstudio.tact.ide.documentation

data class TypeDoc(
    val range: String,
    val size: String,
    val description: String? = null,
    val tlb: String? = null,
)

val TYPE_DOCS: Map<String, TypeDoc> = mapOf(
    "uint8" to TypeDoc(range = "0 to 255 (2^8 - 1)", size = "8 bits = 1 byte"),
    "uint16" to TypeDoc(range = "0 to 65,535 (2^16 - 1)", size = "16 bits = 2 bytes"),
    "uint32" to TypeDoc(range = "0 to 4,294,967,295 (2^32 - 1)", size = "32 bits = 4 bytes"),
    "uint64" to TypeDoc(range = "0 to 2^64 - 1", size = "64 bits = 8 bytes"),
    "uint128" to TypeDoc(range = "0 to 2^128 - 1", size = "128 bits = 16 bytes"),
    "uint256" to TypeDoc(range = "0 to 2^256 - 1", size = "256 bits = 32 bytes"),

    "int8" to TypeDoc(range = "-128 to 127 (-2^7 to 2^7 - 1)", size = "8 bits = 1 byte"),
    "int16" to TypeDoc(range = "-32,768 to 32,767 (-2^15 to 2^15 - 1)", size = "16 bits = 2 bytes"),
    "int32" to TypeDoc(range = "-2^31 to 2^31 - 1", size = "32 bits = 4 bytes"),
    "int64" to TypeDoc(range = "-2^63 to 2^63 - 1", size = "64 bits = 8 bytes"),
    "int128" to TypeDoc(range = "-2^127 to 2^127 - 1", size = "128 bits = 16 bytes"),
    "int256" to TypeDoc(range = "-2^255 to 2^255 - 1", size = "256 bits = 32 bytes"),
    "int257" to TypeDoc(range = "-2^256 to 2^256 - 1", size = "257 bits = 32 bytes + 1 bit"),

    "coins" to TypeDoc(
        range = "0 to 2^120 - 1",
        size = "4 to 124 bits",
        description = "An alias to `VarUInteger16`, commonly used for storing `nanoToncoin` amounts. Takes variable bit length depending on the optimal number of bytes needed.",
        tlb = "varuint16"
    ),
    "varuint16" to TypeDoc(range = "0 to 2^120 - 1", size = "4 to 124 bits"),
    "varint16" to TypeDoc(range = "-2^119 to 2^119 - 1", size = "4 to 124 bits"),
    "varuint32" to TypeDoc(range = "0 to 2^248 - 1", size = "5 to 253 bits"),
    "varint32" to TypeDoc(range = "-2^247 to 2^247 - 1", size = "5 to 253 bits")
)

fun generateArbitraryIntDoc(type: String): TypeDoc? {
    val match = Regex("""^(u?int)(\d+)$""").matchEntire(type) ?: return null
    val (prefix, bitsStr) = match.destructured
    val bitWidth = bitsStr.toInt()

    if (prefix == "uint" && (bitWidth < 1 || bitWidth > 256)) return null
    if (prefix == "int" && (bitWidth < 1 || bitWidth > 257)) return null

    return if (prefix == "uint") {
        TypeDoc(
            range = "0 to 2^$bitWidth - 1",
            size = "$bitWidth bits",
            description = "Arbitrary bit-width unsigned integer type (available since Tact 1.5)"
        )
    } else {
        TypeDoc(
            range = "-2^${bitWidth - 1} to 2^${bitWidth - 1} - 1",
            size = "$bitWidth bits",
            description = "Arbitrary bit-width signed integer type (available since Tact 1.5)"
        )
    }
}

fun generateTlBTypeDoc(word: String): String? {
    if (word == "remaining") {
        return """
        **remaining** — direct serialization modifier

        - **Applies to**: Cell, Builder, and Slice types
        - **Effect**: Stores/loads data directly in the current cell instead of as a reference
        - **Usage**: `value: Type as remaining`

        Affects how values are serialized into cells. Instead of using references (default), stores data directly in the current cell.

        Learn more in documentation: https://docs.tact-lang.org/book/cells/#serialization-remaining
        """.trimIndent()
    }

    val typeInfo = TYPE_DOCS[word] ?: generateArbitraryIntDoc(word) ?: return null
    val isVariable = word.startsWith("var") || word == "coins"
    val isUnsigned = word.startsWith("uint") || word == "coins"
    val bitSizePrefix = if (isVariable) "variable-length" else "${typeInfo.size.split(' ')[0]}-bit"

    return buildString {
        appendLine("**$word** — $bitSizePrefix ${if (isUnsigned) "unsigned" else "signed"} integer")
        appendLine()
        appendLine("- **Range**: ${typeInfo.range}")
        appendLine("- **Size**: ${typeInfo.size}")
        append("- **TL-B**: ${typeInfo.tlb ?: word}")
        typeInfo.description?.let { appendLine("\n\n$it") }
    }
}
