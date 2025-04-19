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
  public TactAssertNotNullExpression getAssertNotNullExpression() {
    return TactPsiTreeUtil.getChildOfType(this, TactAssertNotNullExpression.class);
  }

  @Override
  @NotNull
  public TactExpression getExpression() {
    return notNullChild(TactPsiTreeUtil.getChildOfType(this, TactExpression.class));
  }

}
