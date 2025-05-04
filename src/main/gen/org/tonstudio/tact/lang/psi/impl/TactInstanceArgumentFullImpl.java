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

public class TactInstanceArgumentFullImpl extends TactCompositeElementImpl implements TactInstanceArgumentFull {

  public TactInstanceArgumentFullImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitInstanceArgumentFull(this);
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
  @NotNull
  public TactFieldName getFieldName() {
    return notNullChild(TactPsiTreeUtil.getChildOfType(this, TactFieldName.class));
  }

  @Override
  @NotNull
  public PsiElement getColon() {
    return notNullChild(findChildByType(COLON));
  }

}
