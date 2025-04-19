// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.tonstudio.tact.lang.stubs.TactPlainAttributeStub;

public interface TactPlainAttribute extends TactCompositeElement, StubBasedPsiElement<TactPlainAttributeStub> {

  @Nullable
  TactArgumentList getArgumentList();

  @NotNull
  TactAttributeKey getAttributeKey();

}
