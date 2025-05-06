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

public class TactAssignmentStatementImpl extends TactStatementImpl implements TactAssignmentStatement {

  public TactAssignmentStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitAssignmentStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public TactAssignOp getAssignOp() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, TactAssignOp.class));
  }

  @Override
  @NotNull
  public List<TactExpression> getExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TactExpression.class);
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

}
