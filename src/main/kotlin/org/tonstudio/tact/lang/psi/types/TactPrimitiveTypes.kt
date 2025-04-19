package org.tonstudio.tact.lang.psi.types

enum class TactPrimitiveTypes(val value: String, val size: Int) {
    BOOL("Bool", 1),
    INT("Int", 257),
    BUILDER("Builder", 0),
    CELL("Cell", 0),
    ADDRESS("Address", 0),
    SLICE("Slice", 0),
    STRING("String", 0);

    companion object {
        fun find(name: String): TactPrimitiveTypes? = entries.find { it.value == name }

        fun isPrimitiveType(name: String): Boolean {
            return entries.any { it.value == name }
        }
    }
}
