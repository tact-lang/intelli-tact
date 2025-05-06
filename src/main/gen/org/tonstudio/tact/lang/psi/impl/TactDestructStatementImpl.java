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

public class TactDestructStatementImpl extends TactStatementImpl implements TactDestructStatement {

  public TactDestructStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitDestructStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<TactDestructItem> getDestructItemList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TactDestructItem.class);
  }

  @Override
  @Nullable
  public TactExpression getExpression() {
    return PsiTreeUtil.getChildOfType(this, TactExpression.class);
  }

  @Override
  @NotNull
  public TactTypeReferenceExpression getTypeReferenceExpression() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, TactTypeReferenceExpression.class));
  }

  @Override
  @Nullable
  public TactSemi getSemi() {
    return PsiTreeUtil.getChildOfType(this, TactSemi.class);
  }

  @Override
  @Nullable
  public PsiElement getAssign() {
    return findChildByType(ASSIGN);
  }

  @Override
  @NotNull
  public PsiElement getLbrace() {
    return notNullChild(findChildByType(LBRACE));
  }

  @Override
  @Nullable
  public PsiElement getRbrace() {
    return findChildByType(RBRACE);
  }

  @Override
  @NotNull
  public PsiElement getLet() {
    return notNullChild(findChildByType(LET));
  }

}
