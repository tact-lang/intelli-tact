package org.tonstudio.tact.lang.completion.sort

import com.intellij.codeInsight.lookup.LookupElement

/**
 * A weigher for Tact completion variants.
 *
 * @see TACT_COMPLETION_WEIGHERS
 */
interface TactCompletionWeigher {
    /**
     * Returned values are sorted in ascending order, i.e.
     * - `0`, `1`, `2`,
     * - `false`, `true`,
     * - upper enum variants before bottom ones.
     */
    fun weigh(element: LookupElement): Comparable<*>

    /**
     * The [id] turns into [com.intellij.codeInsight.lookup.LookupElementWeigher.myId], which then turns into
     * [com.intellij.codeInsight.lookup.ClassifierFactory.getId] which is used for comparison of
     * [com.intellij.codeInsight.completion.CompletionSorter]s.
     */
    val id: String
}
