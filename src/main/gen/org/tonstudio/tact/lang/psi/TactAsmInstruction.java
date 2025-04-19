// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TactAsmInstruction extends TactCompositeElement {

  @Nullable
  TactAsmBLock getAsmBLock();

  @Nullable
  TactAsmCreateBuilder getAsmCreateBuilder();

  @Nullable
  TactAsmStoreSlice getAsmStoreSlice();

  @Nullable
  TactAsmToCellBuilder getAsmToCellBuilder();

  @Nullable
  TactExpression getExpression();

  @Nullable
  PsiElement getIdentifier();

}
