// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static org.tonstudio.tact.lang.TactTypes.*;
import org.tonstudio.tact.lang.stubs.TactParametersStub;
import org.tonstudio.tact.lang.psi.*;
import com.intellij.psi.stubs.IStubElementType;

public class TactParametersImpl extends TactStubbedElementImpl<TactParametersStub> implements TactParameters {

  public TactParametersImpl(@NotNull TactParametersStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public TactParametersImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitParameters(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<TactParamDefinition> getParamDefinitionList() {
    return PsiTreeUtil.getStubChildrenOfTypeAsList(this, TactParamDefinition.class);
  }

  @Override
  @NotNull
  public PsiElement getLparen() {
    return notNullChild(findChildByType(LPAREN));
  }

  @Override
  @Nullable
  public PsiElement getRparen() {
    return findChildByType(RPAREN);
  }

}
