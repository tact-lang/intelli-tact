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

public class TactVarDeclarationImpl extends TactCompositeElementImpl implements TactVarDeclaration {

  public TactVarDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitVarDeclaration(this);
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
  public TactTypeHint getTypeHint() {
    return PsiTreeUtil.getChildOfType(this, TactTypeHint.class);
  }

  @Override
  @NotNull
  public TactVarDefinition getVarDefinition() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, TactVarDefinition.class));
  }

  @Override
  @Nullable
  public TactSemi getSemi() {
    return PsiTreeUtil.getChildOfType(this, TactSemi.class);
  }

  @Override
  @NotNull
  public PsiElement getAssign() {
    return notNullChild(findChildByType(ASSIGN));
  }

  @Override
  @NotNull
  public PsiElement getLet() {
    return notNullChild(findChildByType(LET));
  }

}
