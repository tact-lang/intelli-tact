// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.tonstudio.tact.lang.stubs.TactNativeFunctionDeclarationStub;
import com.intellij.psi.ResolveState;
import org.tonstudio.tact.lang.psi.types.TactTypeEx;

public interface TactNativeFunctionDeclaration extends TactNamedElement, TactSignatureOwner, StubBasedPsiElement<TactNativeFunctionDeclarationStub> {

  @Nullable
  TactAttributes getAttributes();

  @NotNull
  List<TactFunctionAttribute> getFunctionAttributeList();

  @Nullable
  TactSignature getSignature();

  @Nullable
  PsiElement getSemicolon();

  @Nullable
  PsiElement getIdentifier();

  @NotNull
  PsiElement getNative();

  @Nullable TactTypeEx getTypeInner(@Nullable ResolveState context);

}
