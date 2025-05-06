// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static org.tonstudio.tact.lang.TactTypes.*;
import org.tonstudio.tact.lang.psi.*;

public class TactAsmPrimitiveImpl extends TactCompositeElementImpl implements TactAsmPrimitive {

  public TactAsmPrimitiveImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitAsmPrimitive(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public TactAsmBinBitstring getAsmBinBitstring() {
    return PsiTreeUtil.getChildOfType(this, TactAsmBinBitstring.class);
  }

  @Override
  @Nullable
  public TactAsmBocHex getAsmBocHex() {
    return PsiTreeUtil.getChildOfType(this, TactAsmBocHex.class);
  }

  @Override
  @Nullable
  public TactAsmControlRegister getAsmControlRegister() {
    return PsiTreeUtil.getChildOfType(this, TactAsmControlRegister.class);
  }

  @Override
  @Nullable
  public TactAsmCreateBuilder getAsmCreateBuilder() {
    return PsiTreeUtil.getChildOfType(this, TactAsmCreateBuilder.class);
  }

  @Override
  @Nullable
  public TactAsmHexBitstring getAsmHexBitstring() {
    return PsiTreeUtil.getChildOfType(this, TactAsmHexBitstring.class);
  }

  @Override
  @Nullable
  public TactAsmInteger getAsmInteger() {
    return PsiTreeUtil.getChildOfType(this, TactAsmInteger.class);
  }

  @Override
  @Nullable
  public TactAsmSequence getAsmSequence() {
    return PsiTreeUtil.getChildOfType(this, TactAsmSequence.class);
  }

  @Override
  @Nullable
  public TactAsmStackElement getAsmStackElement() {
    return PsiTreeUtil.getChildOfType(this, TactAsmStackElement.class);
  }

  @Override
  @Nullable
  public TactAsmStoreSlice getAsmStoreSlice() {
    return PsiTreeUtil.getChildOfType(this, TactAsmStoreSlice.class);
  }

  @Override
  @Nullable
  public TactAsmString getAsmString() {
    return PsiTreeUtil.getChildOfType(this, TactAsmString.class);
  }

  @Override
  @Nullable
  public TactAsmToCellBuilder getAsmToCellBuilder() {
    return PsiTreeUtil.getChildOfType(this, TactAsmToCellBuilder.class);
  }

}
