// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.tonstudio.tact.lang.psi.TactPsiTreeUtil;
import static org.tonstudio.tact.lang.TactTypes.*;
import org.tonstudio.tact.lang.psi.*;

public class TactAssignOpImpl extends TactCompositeElementImpl implements TactAssignOp {

  public TactAssignOpImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitAssignOp(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiElement getAssign() {
    return findChildByType(ASSIGN);
  }

  @Override
  @Nullable
  public PsiElement getBitAndAssign() {
    return findChildByType(BIT_AND_ASSIGN);
  }

  @Override
  @Nullable
  public PsiElement getBitOrAssign() {
    return findChildByType(BIT_OR_ASSIGN);
  }

  @Override
  @Nullable
  public PsiElement getBitXorAssign() {
    return findChildByType(BIT_XOR_ASSIGN);
  }

  @Override
  @Nullable
  public PsiElement getMinusAssign() {
    return findChildByType(MINUS_ASSIGN);
  }

  @Override
  @Nullable
  public PsiElement getMulAssign() {
    return findChildByType(MUL_ASSIGN);
  }

  @Override
  @Nullable
  public PsiElement getPlusAssign() {
    return findChildByType(PLUS_ASSIGN);
  }

  @Override
  @Nullable
  public PsiElement getQuotientAssign() {
    return findChildByType(QUOTIENT_ASSIGN);
  }

  @Override
  @Nullable
  public PsiElement getRemainderAssign() {
    return findChildByType(REMAINDER_ASSIGN);
  }

  @Override
  @Nullable
  public PsiElement getShiftLeftAssign() {
    return findChildByType(SHIFT_LEFT_ASSIGN);
  }

  @Override
  @Nullable
  public PsiElement getShiftRightAssign() {
    return findChildByType(SHIFT_RIGHT_ASSIGN);
  }

}
