// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.tonstudio.tact.lang.stubs.TactFunctionDeclarationStub;
import com.intellij.psi.ResolveState;
import kotlin.Pair;
import org.tonstudio.tact.lang.psi.types.TactTypeEx;

public interface TactFunctionDeclaration extends TactNamedElement, TactSignatureOwner, StubBasedPsiElement<TactFunctionDeclarationStub> {

  @Nullable
  TactAttributes getAttributes();

  @Nullable
  TactBlock getBlock();

  @NotNull
  List<TactFunctionAttribute> getFunctionAttributeList();

  @NotNull
  TactSignature getSignature();

  @Nullable
  TactSemi getSemi();

  @NotNull
  PsiElement getFun();

  @NotNull
  PsiElement getIdentifier();

  @NotNull String getName();

  @Nullable TactTypeEx getTypeInner(@Nullable ResolveState context);

  boolean isGet();

  boolean isAbstract();

  boolean isVirtual();

  @NotNull Pair<@NotNull String, @NotNull Boolean> computeMethodId();

}
