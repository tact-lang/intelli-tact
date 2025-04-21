// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TactContractType extends TactType, TactStorageMembersOwner {

  @NotNull
  List<TactConstDeclaration> getConstDeclarationList();

  @NotNull
  List<TactContractInitDeclaration> getContractInitDeclarationList();

  @Nullable
  TactContractParameters getContractParameters();

  @NotNull
  List<TactFieldDeclaration> getFieldDeclarationList();

  @NotNull
  List<TactFunctionDeclaration> getFunctionDeclarationList();

  @NotNull
  List<TactMessageFunctionDeclaration> getMessageFunctionDeclarationList();

  @Nullable
  TactWithClause getWithClause();

  @Nullable
  PsiElement getLbrace();

  @Nullable
  PsiElement getRbrace();

  @NotNull
  PsiElement getContract();

  @Nullable
  PsiElement getIdentifier();

  @NotNull
  List<TactFieldDefinition> getFieldList();

  @NotNull
  List<TactFunctionDeclaration> getMethodsList();

  @NotNull
  List<TactConstDefinition> getConstantsList();

}
