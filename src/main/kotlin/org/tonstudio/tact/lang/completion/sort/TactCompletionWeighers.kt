package org.tonstudio.tact.lang.completion.sort

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionSorter
import com.intellij.codeInsight.completion.impl.CompletionSorterImpl
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementWeigher
import org.tonstudio.tact.lang.completion.TactLookupElement
import org.tonstudio.tact.lang.completion.TactLookupElementProperties

val SPAWN_COMPLETION_WEIGHERS: List<Any> = listOf(
    /**
     * Based on the value passed via [com.intellij.codeInsight.completion.PrioritizedLookupElement.withPriority].
     * Unused in our case.
     * @see com.intellij.codeInsight.completion.PriorityWeigher
     */
    "priority",

    preferTrue(TactLookupElementProperties::isContextElement, id = "tact-prefer-context-element"),
    preferTrue(TactLookupElementProperties::isContextMember, id = "tact-prefer-context-member"),
    preferTrue(TactLookupElementProperties::isLocal, id = "tact-prefer-locals"),
    preferTrue(TactLookupElementProperties::isReceiverTypeCompatible, id = "tact-prefer-compatible-self-type"),
    preferUpperVariant(TactLookupElementProperties::elementKind, id = "tact-prefer-by-kind"),

    /**
     * Checks prefix matching.
     * @see com.intellij.codeInsight.completion.impl.RealPrefixMatchingWeigher
     */
    "prefix",

    /**
     * Bubbles up the most frequently used items.
     * @see com.intellij.codeInsight.completion.StatisticsWeigher.LookupStatisticsWeigher
     */
    "stats",

    /**
     * Puts closer elements above more distant ones (relative to the location where completion is invoked).
     * For example, elements from workspace packages considered closer than elements from external packages.
     * Specific rules are defined by [com.intellij.psi.util.proximity.ProximityWeigher] implementations
     * registered using `<com.intellij.weigher key="proximity">` extension point.
     * @see com.intellij.codeInsight.completion.LookupElementProximityWeigher
     */
    "proximity",
)

val SPAWN_COMPLETION_WEIGHERS_GROUPED: List<AnchoredWeigherGroup> = splitIntoGroups(SPAWN_COMPLETION_WEIGHERS)

fun withTactSorter(parameters: CompletionParameters, result: CompletionResultSet): CompletionResultSet {
    var sorter = (CompletionSorter.defaultSorter(parameters, result.prefixMatcher) as CompletionSorterImpl)
        .withoutClassifiers { it.id == "liftShorter" }
    for (weigherGroups in SPAWN_COMPLETION_WEIGHERS_GROUPED) {
        sorter = sorter.weighAfter(weigherGroups.anchor, *weigherGroups.weighers)
    }
    return result.withRelevanceSorter(sorter)
}

private fun splitIntoGroups(weighersWithAnchors: List<Any>): List<AnchoredWeigherGroup> {
    val firstEntry = weighersWithAnchors.firstOrNull() ?: return emptyList()
    check(firstEntry is String) {
        "The first element in the weigher list must be a string placeholder like \"priority\"; " +
                "actually it is `${firstEntry}`"
    }
    val result = mutableListOf<AnchoredWeigherGroup>()
    val weigherIds = hashSetOf<String>()
    var currentAnchor: String = firstEntry
    var currentWeighers = mutableListOf<LookupElementWeigher>()
    // Add "dummy weigher" in order to execute `is String ->` arm in the last iteration
    for (weigherOrAnchor in weighersWithAnchors.asSequence().drop(1).plus(sequenceOf("dummy weigher"))) {
        when (weigherOrAnchor) {
            is String                -> {
                if (currentWeighers.isNotEmpty()) {
                    result += AnchoredWeigherGroup(currentAnchor, currentWeighers.toTypedArray())
                    currentWeighers = mutableListOf()
                }
                currentAnchor = weigherOrAnchor
            }
            is TactCompletionWeigher -> {
                if (!weigherIds.add(weigherOrAnchor.id)) {
                    error("Found a ${TactCompletionWeigher::class.simpleName}.id duplicate: " +
                            "`${weigherOrAnchor.id}`")
                }
                currentWeighers += TactCompletionWeigherAsLookupElementWeigher(weigherOrAnchor)
            }
            else                     -> error("The weigher list must consists of String placeholders and instances of " +
                    "${TactCompletionWeigher::class.simpleName}, got ${weigherOrAnchor.javaClass}")
        }
    }
    return result
}

class AnchoredWeigherGroup(val anchor: String, val weighers: Array<LookupElementWeigher>)

private class TactCompletionWeigherAsLookupElementWeigher(
    private val weigher: TactCompletionWeigher
) : LookupElementWeigher(weigher.id, /* negated = */ false, /* dependsOnPrefix = */ false) {
    override fun weigh(element: LookupElement): Comparable<*> {
        val lookupElement = element.`as`(TactLookupElement::class.java)
        return weigher.weigh(lookupElement ?: element)
    }
}

private fun preferTrue(
    property: (TactLookupElementProperties) -> Boolean,
    id: String
): TactCompletionWeigher = object : TactCompletionWeigher {
    override fun weigh(element: LookupElement): Boolean =
        if (element is TactLookupElement) !property(element.props) else true

    override val id: String get() = id
}

private fun preferUpperVariant(
    property: (TactLookupElementProperties) -> Enum<*>,
    id: String
): TactCompletionWeigher = object : TactCompletionWeigher {
    override fun weigh(element: LookupElement): Int =
        if (element is TactLookupElement) property(element.props).ordinal else Int.MAX_VALUE

    override val id: String get() = id
}
