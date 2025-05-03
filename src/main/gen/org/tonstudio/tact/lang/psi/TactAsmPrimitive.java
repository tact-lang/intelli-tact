// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TactAsmPrimitive extends TactCompositeElement {

  @Nullable
  TactAsmBinBitstring getAsmBinBitstring();

  @Nullable
  TactAsmBocHex getAsmBocHex();

  @Nullable
  TactAsmControlRegister getAsmControlRegister();

  @Nullable
  TactAsmCreateBuilder getAsmCreateBuilder();

  @Nullable
  TactAsmHexBitstring getAsmHexBitstring();

  @Nullable
  TactAsmInteger getAsmInteger();

  @Nullable
  TactAsmSequence getAsmSequence();

  @Nullable
  TactAsmStackElement getAsmStackElement();

  @Nullable
  TactAsmStoreSlice getAsmStoreSlice();

  @Nullable
  TactAsmString getAsmString();

  @Nullable
  TactAsmToCellBuilder getAsmToCellBuilder();

}
