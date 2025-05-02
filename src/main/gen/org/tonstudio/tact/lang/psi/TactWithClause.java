// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.tonstudio.tact.lang.stubs.TactWithClauseStub;

public interface TactWithClause extends TactCompositeElement, StubBasedPsiElement<TactWithClauseStub> {

  @NotNull
  List<TactType> getTypeList();

  @NotNull
  PsiElement getWith();

}
