package org.tonstudio.tact.ide.navigation

import org.tonstudio.tact.lang.psi.TactNamedElement
import org.tonstudio.tact.lang.stubs.index.TactClassLikeIndex

open class TactGotoClassLikeContributor :
    TactGotoContributorBase<TactNamedElement>(TactNamedElement::class.java, TactClassLikeIndex.KEY)
