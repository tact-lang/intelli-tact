package org.tonstudio.tact.ide.navigation

import org.tonstudio.tact.lang.psi.TactNamedElement
import org.tonstudio.tact.lang.stubs.index.TactNamesIndex

open class TactGotoSymbolContributor :
    TactGotoContributorBase<TactNamedElement>(TactNamedElement::class.java, TactNamesIndex.KEY)
