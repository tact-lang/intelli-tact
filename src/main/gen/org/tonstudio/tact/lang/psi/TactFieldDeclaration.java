// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.tonstudio.tact.lang.stubs.TactFieldDeclarationStub;

public interface TactFieldDeclaration extends TactCompositeElement, StubBasedPsiElement<TactFieldDeclarationStub> {

  @Nullable
  TactDefaultFieldValue getDefaultFieldValue();

  @NotNull
  TactFieldDefinition getFieldDefinition();

  @NotNull
  TactType getType();

  @NotNull
  PsiElement getColon();

  @Nullable
  PsiElement getSemicolon();

}
