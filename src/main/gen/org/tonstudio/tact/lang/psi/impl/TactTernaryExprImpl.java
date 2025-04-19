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

public class TactTernaryExprImpl extends TactExpressionImpl implements TactTernaryExpr {

  public TactTernaryExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitTernaryExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<TactExpression> getExpressionList() {
    return TactPsiTreeUtil.getChildrenOfTypeAsList(this, TactExpression.class);
  }

  @Override
  @Nullable
  public PsiElement getColon() {
    return findChildByType(COLON);
  }

  @Override
  @NotNull
  public PsiElement getQuestion() {
    return notNullChild(findChildByType(QUESTION));
  }

  @Override
  @NotNull
  public TactExpression getCondition() {
    List<TactExpression> p1 = getExpressionList();
    return p1.get(0);
  }

  @Override
  @Nullable
  public TactExpression getThenBranch() {
    List<TactExpression> p1 = getExpressionList();
    return p1.size() < 2 ? null : p1.get(1);
  }

  @Override
  @Nullable
  public TactExpression getElseBranch() {
    List<TactExpression> p1 = getExpressionList();
    return p1.size() < 3 ? null : p1.get(2);
  }

}
