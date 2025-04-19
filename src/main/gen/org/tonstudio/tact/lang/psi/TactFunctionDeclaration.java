// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.tonstudio.tact.lang.stubs.TactFunctionDeclarationStub;
import com.intellij.psi.ResolveState;
import org.tonstudio.tact.lang.psi.types.TactTypeEx;

public interface TactFunctionDeclaration extends TactSignatureOwner, TactFunctionOrMethodDeclaration, TactAttributeOwner, StubBasedPsiElement<TactFunctionDeclarationStub> {

  @Nullable
  TactAttributes getAttributes();

  @Nullable
  TactBlock getBlock();

  @NotNull
  List<TactFunctionAttribute> getFunctionAttributeList();

  @NotNull
  TactSignature getSignature();

  @Nullable
  PsiElement getSemicolon();

  @NotNull
  PsiElement getFun();

  @NotNull
  PsiElement getIdentifier();

  @NotNull
  String getName();

  @Nullable
  TactTypeEx getTypeInner(@Nullable ResolveState context);

}
