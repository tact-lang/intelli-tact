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

public class TactElseIfBranchImpl extends TactCompositeElementImpl implements TactElseIfBranch {

  public TactElseIfBranchImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitElseIfBranch(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public TactIfStatement getIfStatement() {
    return notNullChild(TactPsiTreeUtil.getChildOfType(this, TactIfStatement.class));
  }

  @Override
  @NotNull
  public PsiElement getElse() {
    return notNullChild(findChildByType(ELSE));
  }

}
