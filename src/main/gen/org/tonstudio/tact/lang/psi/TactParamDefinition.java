// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.tonstudio.tact.lang.stubs.TactParamDefinitionStub;

public interface TactParamDefinition extends TactNamedElement, StubBasedPsiElement<TactParamDefinitionStub> {

  @NotNull
  TactType getType();

  @NotNull
  PsiElement getColon();

  @NotNull
  PsiElement getIdentifier();

}
