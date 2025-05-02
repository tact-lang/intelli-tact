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

public class TactReturnStatementImpl extends TactStatementImpl implements TactReturnStatement {

  public TactReturnStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitReturnStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public TactExpression getExpression() {
    return TactPsiTreeUtil.getChildOfType(this, TactExpression.class);
  }

  @Override
  @Nullable
  public TactSemi getSemi() {
    return TactPsiTreeUtil.getChildOfType(this, TactSemi.class);
  }

  @Override
  @NotNull
  public PsiElement getReturn() {
    return notNullChild(findChildByType(RETURN));
  }

}
