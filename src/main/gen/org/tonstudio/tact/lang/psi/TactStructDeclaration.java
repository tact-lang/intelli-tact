// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.tonstudio.tact.lang.stubs.TactStructDeclarationStub;
import com.intellij.psi.ResolveState;
import org.tonstudio.tact.lang.psi.types.TactTypeEx;

public interface TactStructDeclaration extends TactNamedElement, StubBasedPsiElement<TactStructDeclarationStub> {

  @Nullable
  TactAttributes getAttributes();

  @NotNull
  TactStructType getStructType();

  @Nullable PsiElement getIdentifier();

  @NotNull String getName();

  @NotNull TactTypeEx getTypeInner(@Nullable ResolveState context);

}
