// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TactTraitType extends TactType, TactStorageMembersOwner {

  @NotNull
  List<TactConstDeclaration> getConstDeclarationList();

  @NotNull
  List<TactContractInitDeclaration> getContractInitDeclarationList();

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

  @Nullable
  PsiElement getIdentifier();

  @NotNull
  PsiElement getTrait();

  @NotNull List<@NotNull TactFieldDefinition> getFieldList();

  @NotNull List<@NotNull TactFunctionDeclaration> getMethodsList();

  @NotNull List<@NotNull TactConstDefinition> getConstantsList();

  @NotNull List<@NotNull TactTraitDeclaration> getInheritedTraits();

}
