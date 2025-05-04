// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.tonstudio.tact.lang.stubs.TactMessageFunctionDeclarationStub;

public interface TactMessageFunctionDeclaration extends TactNamedElement, StubBasedPsiElement<TactMessageFunctionDeclarationStub> {

  @Nullable
  TactBlock getBlock();

  @NotNull
  TactMessageKind getMessageKind();

  @Nullable
  TactParameters getParameters();

  @Nullable
  TactReceiveStringId getReceiveStringId();

  @Nullable PsiElement getIdentifier();

  @NotNull String getName();

  @NotNull String nameLike();

}
