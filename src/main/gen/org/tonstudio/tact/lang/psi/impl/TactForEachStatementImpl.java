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

public class TactForEachStatementImpl extends TactStatementImpl implements TactForEachStatement {

  public TactForEachStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitForEachStatement(this);
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
  public List<TactVarDefinition> getVarDefinitionList() {
    return TactPsiTreeUtil.getChildrenOfTypeAsList(this, TactVarDefinition.class);
  }

  @Override
  @Nullable
  public PsiElement getComma() {
    return findChildByType(COMMA);
  }

  @Override
  @Nullable
  public PsiElement getLparen() {
    return findChildByType(LPAREN);
  }

  @Override
  @Nullable
  public PsiElement getRparen() {
    return findChildByType(RPAREN);
  }

  @Override
  @NotNull
  public PsiElement getForeach() {
    return notNullChild(findChildByType(FOREACH));
  }

  @Override
  @Nullable
  public PsiElement getIn() {
    return findChildByType(IN);
  }

  @Override
  @Nullable
  public TactVarDefinition getKey() {
    List<TactVarDefinition> p1 = getVarDefinitionList();
    return p1.size() < 1 ? null : p1.get(0);
  }

  @Override
  @Nullable
  public TactVarDefinition getValue() {
    List<TactVarDefinition> p1 = getVarDefinitionList();
    return p1.size() < 2 ? null : p1.get(1);
  }

}
