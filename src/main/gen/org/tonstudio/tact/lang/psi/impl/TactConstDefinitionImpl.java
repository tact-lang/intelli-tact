// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.tonstudio.tact.lang.psi.TactPsiTreeUtil;
import static org.tonstudio.tact.lang.TactTypes.*;
import org.tonstudio.tact.lang.stubs.TactConstDefinitionStub;
import org.tonstudio.tact.lang.psi.*;
import com.intellij.psi.ResolveState;
import org.tonstudio.tact.lang.psi.types.TactTypeEx;
import com.intellij.psi.stubs.IStubElementType;

public class TactConstDefinitionImpl extends TactNamedElementImpl<TactConstDefinitionStub> implements TactConstDefinition {

  public TactConstDefinitionImpl(@NotNull TactConstDefinitionStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public TactConstDefinitionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitConstDefinition(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<TactConstantModifier> getConstantModifierList() {
    return TactPsiTreeUtil.getChildrenOfTypeAsList(this, TactConstantModifier.class);
  }

  @Override
  @Nullable
  public TactExpression getExpression() {
    return TactPsiTreeUtil.getChildOfType(this, TactExpression.class);
  }

  @Override
  @Nullable
  public TactTypeHint getTypeHint() {
    return TactPsiTreeUtil.getChildOfType(this, TactTypeHint.class);
  }

  @Override
  @Nullable
  public TactSemi getSemi() {
    return TactPsiTreeUtil.getChildOfType(this, TactSemi.class);
  }

  @Override
  @Nullable
  public PsiElement getAssign() {
    return findChildByType(ASSIGN);
  }

  @Override
  @NotNull
  public PsiElement getConst() {
    return notNullChild(findChildByType(CONST));
  }

  @Override
  @Nullable
  public PsiElement getIdentifier() {
    return findChildByType(IDENTIFIER);
  }

  @Override
  public @Nullable TactTypeEx getTypeInner(@Nullable ResolveState context) {
    return TactPsiImplUtil.getTypeInner(this, context);
  }

  @Override
  public @NotNull String getName() {
    return TactPsiImplUtil.getName(this);
  }

  @Override
  public @NotNull String getExpressionText() {
    return TactPsiImplUtil.getExpressionText(this);
  }

  @Override
  public @NotNull String getExpressionType() {
    return TactPsiImplUtil.getExpressionType(this);
  }

}
