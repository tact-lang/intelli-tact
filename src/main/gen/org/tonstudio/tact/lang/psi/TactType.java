// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.tonstudio.tact.lang.stubs.TactTypeStub;
import kotlin.Pair;

public interface TactType extends TactCompositeElement, StubBasedPsiElement<TactTypeStub> {

  @Nullable
  TactType getType();

  @NotNull
  List<TactTypeExtra> getTypeExtraList();

  @Nullable
  TactTypeReferenceExpression getTypeReferenceExpression();

  @Nullable
  PsiElement getIdentifier();

  @NotNull
  Pair<TactType, List<TactTypeExtra>> resolveType();

}
