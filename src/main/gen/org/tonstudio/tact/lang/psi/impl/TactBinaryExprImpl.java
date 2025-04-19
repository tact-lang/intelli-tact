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

public class TactBinaryExprImpl extends TactExpressionImpl implements TactBinaryExpr {

  public TactBinaryExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitBinaryExpr(this);
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
  @NotNull
  public TactExpression getLeft() {
    List<TactExpression> p1 = getExpressionList();
    return p1.get(0);
  }

  @Override
  @Nullable
  public TactExpression getRight() {
    List<TactExpression> p1 = getExpressionList();
    return p1.size() < 2 ? null : p1.get(1);
  }

  @Override
  @Nullable
  public PsiElement getOperator() {
    return TactPsiImplUtil.getOperator(this);
  }

}
