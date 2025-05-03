// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.tonstudio.tact.lang.stubs.TactAsmFunctionDeclarationStub;
import com.intellij.psi.ResolveState;
import org.tonstudio.tact.lang.psi.types.TactTypeEx;

public interface TactAsmFunctionDeclaration extends TactNamedElement, TactSignatureOwner, StubBasedPsiElement<TactAsmFunctionDeclarationStub> {

  @Nullable
  TactAsmFunctionBody getAsmFunctionBody();

  @NotNull
  TactAsmHeader getAsmHeader();

  @NotNull
  List<TactFunctionAttribute> getFunctionAttributeList();

  @Nullable
  TactSignature getSignature();

  @Nullable
  PsiElement getFun();

  @Nullable
  PsiElement getIdentifier();

  @Nullable
  TactTypeEx getTypeInner(@Nullable ResolveState context);

}
