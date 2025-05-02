// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.tonstudio.tact.lang.psi.TactPsiTreeUtil;
import static org.tonstudio.tact.lang.TactTypes.*;
import org.tonstudio.tact.lang.stubs.TactWithClauseStub;
import org.tonstudio.tact.lang.psi.*;
import com.intellij.psi.stubs.IStubElementType;

public class TactWithClauseImpl extends TactStubbedElementImpl<TactWithClauseStub> implements TactWithClause {

  public TactWithClauseImpl(@NotNull TactWithClauseStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public TactWithClauseImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitWithClause(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<TactType> getTypeList() {
    return TactPsiTreeUtil.getStubChildrenOfTypeAsList(this, TactType.class);
  }

  @Override
  @NotNull
  public PsiElement getWith() {
    return notNullChild(findChildByType(WITH));
  }

}
