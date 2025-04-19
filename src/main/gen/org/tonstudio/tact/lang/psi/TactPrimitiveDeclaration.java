// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.tonstudio.tact.lang.stubs.TactPrimitiveDeclarationStub;
import com.intellij.psi.ResolveState;
import org.tonstudio.tact.lang.psi.types.TactTypeEx;

public interface TactPrimitiveDeclaration extends TactNamedElement, StubBasedPsiElement<TactPrimitiveDeclarationStub> {

  @NotNull
  TactPrimitiveType getPrimitiveType();

  @NotNull
  PsiElement getSemicolon();

  @Nullable
  PsiElement getIdentifier();

  @NotNull
  String getName();

  @NotNull
  TactTypeEx getTypeInner(@Nullable ResolveState context);

}
