// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.tonstudio.tact.lang.stubs.TactResultStub;
import com.intellij.psi.ResolveState;
import org.tonstudio.tact.lang.psi.types.TactTypeEx;

public interface TactResult extends TactTypeOwner, StubBasedPsiElement<TactResultStub> {

  @NotNull
  TactType getType();

  @Nullable
  PsiElement getColon();

  @NotNull TactTypeEx getType(@Nullable ResolveState context);

}
