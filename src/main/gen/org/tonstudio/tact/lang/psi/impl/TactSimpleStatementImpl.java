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

public class TactSimpleStatementImpl extends TactStatementImpl implements TactSimpleStatement {

  public TactSimpleStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitSimpleStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public TactAssignmentStatement getAssignmentStatement() {
    return TactPsiTreeUtil.getChildOfType(this, TactAssignmentStatement.class);
  }

  @Override
  @Nullable
  public TactExpression getExpression() {
    return TactPsiTreeUtil.getChildOfType(this, TactExpression.class);
  }

  @Override
  @Nullable
  public TactStatementDestruct getStatementDestruct() {
    return TactPsiTreeUtil.getChildOfType(this, TactStatementDestruct.class);
  }

  @Override
  @Nullable
  public TactVarDeclaration getVarDeclaration() {
    return TactPsiTreeUtil.getChildOfType(this, TactVarDeclaration.class);
  }

  @Override
  @Nullable
  public PsiElement getSemicolon() {
    return findChildByType(SEMICOLON);
  }

}
