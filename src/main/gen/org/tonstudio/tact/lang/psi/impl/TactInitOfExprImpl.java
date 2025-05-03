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

public class TactInitOfExprImpl extends TactExpressionImpl implements TactInitOfExpr {

  public TactInitOfExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitInitOfExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public TactArgumentList getArgumentList() {
    return TactPsiTreeUtil.getChildOfType(this, TactArgumentList.class);
  }

  @Override
  @Nullable
  public TactTypeReferenceExpression getTypeReferenceExpression() {
    return TactPsiTreeUtil.getChildOfType(this, TactTypeReferenceExpression.class);
  }

  @Override
  @NotNull
  public PsiElement getInitOf() {
    return notNullChild(findChildByType(INIT_OF));
  }

}
