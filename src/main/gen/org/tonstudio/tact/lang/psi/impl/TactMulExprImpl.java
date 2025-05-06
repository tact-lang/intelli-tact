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

public class TactMulExprImpl extends TactBinaryExprImpl implements TactMulExpr {

  public TactMulExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitMulExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiElement getBitAnd() {
    return findChildByType(BIT_AND);
  }

  @Override
  @Nullable
  public PsiElement getMul() {
    return findChildByType(MUL);
  }

  @Override
  @Nullable
  public PsiElement getQuotient() {
    return findChildByType(QUOTIENT);
  }

  @Override
  @Nullable
  public PsiElement getRemainder() {
    return findChildByType(REMAINDER);
  }

  @Override
  @Nullable
  public PsiElement getShiftLeft() {
    return findChildByType(SHIFT_LEFT);
  }

  @Override
  @Nullable
  public PsiElement getShiftRight() {
    return findChildByType(SHIFT_RIGHT);
  }

}
