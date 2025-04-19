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

public class TactConditionalExprImpl extends TactBinaryExprImpl implements TactConditionalExpr {

  public TactConditionalExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitConditionalExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiElement getEq() {
    return findChildByType(EQ);
  }

  @Override
  @Nullable
  public PsiElement getGreater() {
    return findChildByType(GREATER);
  }

  @Override
  @Nullable
  public PsiElement getGreaterOrEqual() {
    return findChildByType(GREATER_OR_EQUAL);
  }

  @Override
  @Nullable
  public PsiElement getLess() {
    return findChildByType(LESS);
  }

  @Override
  @Nullable
  public PsiElement getLessOrEqual() {
    return findChildByType(LESS_OR_EQUAL);
  }

  @Override
  @Nullable
  public PsiElement getNotEq() {
    return findChildByType(NOT_EQ);
  }

}
