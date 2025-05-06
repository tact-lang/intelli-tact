package org.tonstudio.tact.asm

import org.tonstudio.tact.lang.psi.TactAsmExpression
import org.tonstudio.tact.lang.psi.TactAsmSequence
import org.tonstudio.tact.utils.childOfType

data class GasSettings(val loopGasCoefficient: Int)

data class GasConsumption(val value: Int, val unknown: Boolean, val exact: Boolean)

fun computeSeqGasConsumption(seq: TactAsmSequence, gasSettings: GasSettings): GasConsumption =
    computeGasConsumption(seq.asmExpressionList, gasSettings)

fun computeGasConsumption(
    expressions: List<TactAsmExpression>,
    gasSettings: GasSettings,
): GasConsumption {
    var exact = true
    var total = 0

    for (expr in expressions) {
        val name = expr.asmInstruction.identifier.text
        val info = findInstruction(name, expr.asmArguments.asmPrimitiveList)
        if (info == null || info.doc.gas.isEmpty()) {
            exact = false
            continue
        }

        val continuations = expr.asmArguments.asmPrimitiveList.mapNotNull { primitive ->
            primitive.childOfType<TactAsmSequence>()
        }

        val continuationsGas = continuations.map { computeSeqGasConsumption(it, gasSettings) }

        if (continuationsGas.any { !it.exact }) {
            exact = false
        }

        total += if (
            (info.mnemonic.startsWith("IFELSE") || info.mnemonic.startsWith("IFREFELSEREF")) &&
            continuationsGas.size == 2
        ) {
            // select max branch consumption
            maxOf(continuationsGas[0].value, continuationsGas[1].value)
        } else {
            val sumBranches = continuationsGas.sumOf { it.value }
            if (info.mnemonic.startsWith("REPEAT") ||
                info.mnemonic.startsWith("UNTIL") ||
                info.mnemonic.startsWith("WHILE")
            ) gasSettings.loopGasCoefficient * sumBranches
            else sumBranches
        }

        if (info.doc.gas.contains("|") || info.doc.gas.contains("+")) {
            exact = false
        }

        when (info.mnemonic) {
            "WHILE", "REPEAT",
            "UNTIL", "IFNOT",
            "IFREF", "IFNOTREF",
            "IFJMPREF", "IFNOTJMPREF",
            "IFREFELSEREF", "IFELSE", "IF",
                -> {
                exact = false
            }
        }

        total += info.doc.gas.toIntOrNull() ?: 0
    }

    return GasConsumption(value = total, unknown = false, exact = exact)
}

fun instructionPresentation(gas: String?, stack: String?, format: String): String {
    if (gas.isNullOrEmpty()) {
        return ": no data"
    }
    return format.replace("{gas}", gas).replace("{stack}", getStackPresentation(stack))
}
