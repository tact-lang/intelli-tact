// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TactAsmBLock extends TactCompositeElement {

  @NotNull
  List<TactAsmInstruction> getAsmInstructionList();

  @NotNull
  PsiElement getGreater();

  @NotNull
  PsiElement getLbrace();

  @NotNull
  PsiElement getLess();

  @NotNull
  PsiElement getRbrace();

}
