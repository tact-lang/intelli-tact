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
import com.intellij.psi.stubs.IStubElementType;
import org.tonstudio.tact.lang.stubs.TactTypeStub;

public class TactPrimitiveTypeImpl extends TactTypeImpl implements TactPrimitiveType {

  public TactPrimitiveTypeImpl(@NotNull TactTypeStub<?> stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public TactPrimitiveTypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitPrimitiveType(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiElement getIdentifier() {
    return findChildByType(IDENTIFIER);
  }

  @Override
  @NotNull
  public PsiElement getPrimitive() {
    return notNullChild(findChildByType(PRIMITIVE));
  }

}
