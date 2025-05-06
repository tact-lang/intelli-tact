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

public class TactUntilStatementImpl extends TactStatementImpl implements TactUntilStatement {

  public TactUntilStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitUntilStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public TactExpression getExpression() {
    return PsiTreeUtil.getChildOfType(this, TactExpression.class);
  }

  @Override
  @Nullable
  public TactSemi getSemi() {
    return PsiTreeUtil.getChildOfType(this, TactSemi.class);
  }

  @Override
  @NotNull
  public PsiElement getDo() {
    return notNullChild(findChildByType(DO));
  }

  @Override
  @Nullable
  public PsiElement getUntil() {
    return findChildByType(UNTIL);
  }

}
