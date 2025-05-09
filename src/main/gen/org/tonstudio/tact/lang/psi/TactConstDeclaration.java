// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.tonstudio.tact.lang.stubs.TactConstDeclarationStub;
import com.intellij.psi.ResolveState;
import org.tonstudio.tact.lang.psi.types.TactTypeEx;

public interface TactConstDeclaration extends TactNamedElement, StubBasedPsiElement<TactConstDeclarationStub> {

  @NotNull
  List<TactConstantModifier> getConstantModifierList();

  @Nullable
  TactExpression getExpression();

  @Nullable
  TactTypeHint getTypeHint();

  @Nullable
  TactSemi getSemi();

  @Nullable
  PsiElement getAssign();

  @NotNull
  PsiElement getConst();

  @Nullable
  PsiElement getIdentifier();

  @Nullable TactTypeEx getTypeInner(@Nullable ResolveState context);

  @NotNull String getName();

  @NotNull String getExpressionText();

  @NotNull String getExpressionType();

}
