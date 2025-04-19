// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.tonstudio.tact.lang.stubs.TactConstDefinitionStub;
import com.intellij.psi.ResolveState;
import org.tonstudio.tact.lang.psi.types.TactTypeEx;

public interface TactConstDefinition extends TactNamedElement, StubBasedPsiElement<TactConstDefinitionStub> {

  @Nullable
  TactExpression getExpression();

  @Nullable
  TactTypeHint getTypeHint();

  @Nullable
  PsiElement getAssign();

  @NotNull
  PsiElement getIdentifier();

  @Nullable
  TactTypeEx getTypeInner(@Nullable ResolveState context);

  @NotNull
  String getName();

  @NotNull
  String getExpressionText();

  @NotNull
  String getExpressionType();

}
