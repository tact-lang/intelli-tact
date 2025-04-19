// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.tonstudio.tact.lang.stubs.TactVarDefinitionStub;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveState;
import org.tonstudio.tact.lang.psi.types.TactTypeEx;

public interface TactVarDefinition extends TactNamedElement, StubBasedPsiElement<TactVarDefinitionStub> {

  @NotNull
  PsiElement getIdentifier();

  @Nullable
  TactTypeEx getTypeInner(@Nullable ResolveState context);

  @NotNull
  String getName();

  @NotNull
  PsiReference getReference();

}
