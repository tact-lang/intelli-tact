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

  @NotNull
  TactAsmHeader getAsmHeader();

  @NotNull
  List<TactAsmInstruction> getAsmInstructionList();

  @NotNull
  List<TactFunctionAttribute> getFunctionAttributeList();

  @NotNull
  TactSignature getSignature();

  @NotNull
  PsiElement getLbrace();

  @NotNull
  PsiElement getRbrace();

  @NotNull
  PsiElement getFun();

  @NotNull
  PsiElement getIdentifier();

  @Nullable
  TactTypeEx getTypeInner(@Nullable ResolveState context);

}
