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

public class TactDotExpressionImpl extends TactExpressionImpl implements TactDotExpression {

  public TactDotExpressionImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitDotExpression(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public TactDummyRightHandRule getDummyRightHandRule() {
    return PsiTreeUtil.getChildOfType(this, TactDummyRightHandRule.class);
  }

  @Override
  @NotNull
  public TactExpression getExpression() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, TactExpression.class));
  }

}
