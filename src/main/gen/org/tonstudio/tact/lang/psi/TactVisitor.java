// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiLanguageInjectionHost;

public class TactVisitor extends PsiElementVisitor {

  public void visitAddExpr(@NotNull TactAddExpr o) {
    visitBinaryExpr(o);
  }

  public void visitAndExpr(@NotNull TactAndExpr o) {
    visitBinaryExpr(o);
  }

  public void visitArgumentList(@NotNull TactArgumentList o) {
    visitCompositeElement(o);
  }

  public void visitAsmArguments(@NotNull TactAsmArguments o) {
    visitCompositeElement(o);
  }

  public void visitAsmBinBitstring(@NotNull TactAsmBinBitstring o) {
    visitCompositeElement(o);
  }

  public void visitAsmBocHex(@NotNull TactAsmBocHex o) {
    visitCompositeElement(o);
  }

  public void visitAsmControlRegister(@NotNull TactAsmControlRegister o) {
    visitCompositeElement(o);
  }

  public void visitAsmCreateBuilder(@NotNull TactAsmCreateBuilder o) {
    visitCompositeElement(o);
  }

  public void visitAsmExpression(@NotNull TactAsmExpression o) {
    visitCompositeElement(o);
  }

  public void visitAsmFunctionBody(@NotNull TactAsmFunctionBody o) {
    visitCompositeElement(o);
  }

  public void visitAsmFunctionDeclaration(@NotNull TactAsmFunctionDeclaration o) {
    visitNamedElement(o);
    // visitSignatureOwner(o);
  }

  public void visitAsmHeader(@NotNull TactAsmHeader o) {
    visitCompositeElement(o);
  }

  public void visitAsmHexBitstring(@NotNull TactAsmHexBitstring o) {
    visitCompositeElement(o);
  }

  public void visitAsmInstruction(@NotNull TactAsmInstruction o) {
    visitCompositeElement(o);
  }

  public void visitAsmInteger(@NotNull TactAsmInteger o) {
    visitCompositeElement(o);
  }

  public void visitAsmPrimitive(@NotNull TactAsmPrimitive o) {
    visitCompositeElement(o);
  }

  public void visitAsmSequence(@NotNull TactAsmSequence o) {
    visitCompositeElement(o);
  }

  public void visitAsmShuffle(@NotNull TactAsmShuffle o) {
    visitCompositeElement(o);
  }

  public void visitAsmStackElement(@NotNull TactAsmStackElement o) {
    visitCompositeElement(o);
  }

  public void visitAsmStoreSlice(@NotNull TactAsmStoreSlice o) {
    visitCompositeElement(o);
  }

  public void visitAsmString(@NotNull TactAsmString o) {
    visitCompositeElement(o);
  }

  public void visitAsmToCellBuilder(@NotNull TactAsmToCellBuilder o) {
    visitCompositeElement(o);
  }

  public void visitAssertNotNullExpression(@NotNull TactAssertNotNullExpression o) {
    visitCompositeElement(o);
  }

  public void visitAssignOp(@NotNull TactAssignOp o) {
    visitCompositeElement(o);
  }

  public void visitAssignmentStatement(@NotNull TactAssignmentStatement o) {
    visitStatement(o);
  }

  public void visitAttribute(@NotNull TactAttribute o) {
    visitCompositeElement(o);
  }

  public void visitAttributeExpression(@NotNull TactAttributeExpression o) {
    visitCompositeElement(o);
  }

  public void visitAttributeIdentifier(@NotNull TactAttributeIdentifier o) {
    visitCompositeElement(o);
  }

  public void visitAttributeKey(@NotNull TactAttributeKey o) {
    visitCompositeElement(o);
  }

  public void visitAttributes(@NotNull TactAttributes o) {
    visitCompositeElement(o);
  }

  public void visitBinaryExpr(@NotNull TactBinaryExpr o) {
    visitExpression(o);
  }

  public void visitBlock(@NotNull TactBlock o) {
    visitCompositeElement(o);
  }

  public void visitBouncedType(@NotNull TactBouncedType o) {
    visitType(o);
  }

  public void visitCallExpr(@NotNull TactCallExpr o) {
    visitExpression(o);
  }

  public void visitCatchClause(@NotNull TactCatchClause o) {
    visitCompositeElement(o);
  }

  public void visitCodeOfExpr(@NotNull TactCodeOfExpr o) {
    visitExpression(o);
  }

  public void visitConditionalExpr(@NotNull TactConditionalExpr o) {
    visitBinaryExpr(o);
  }

  public void visitConstDeclaration(@NotNull TactConstDeclaration o) {
    visitCompositeElement(o);
  }

  public void visitConstDefinition(@NotNull TactConstDefinition o) {
    visitNamedElement(o);
  }

  public void visitConstantModifier(@NotNull TactConstantModifier o) {
    visitCompositeElement(o);
  }

  public void visitContractDeclaration(@NotNull TactContractDeclaration o) {
    visitNamedElement(o);
  }

  public void visitContractInitDeclaration(@NotNull TactContractInitDeclaration o) {
    visitNamedElement(o);
  }

  public void visitContractParameters(@NotNull TactContractParameters o) {
    visitCompositeElement(o);
  }

  public void visitContractType(@NotNull TactContractType o) {
    visitType(o);
    // visitStorageMembersOwner(o);
  }

  public void visitDefaultFieldValue(@NotNull TactDefaultFieldValue o) {
    visitCompositeElement(o);
  }

  public void visitDestructItem(@NotNull TactDestructItem o) {
    visitCompositeElement(o);
  }

  public void visitDotExpression(@NotNull TactDotExpression o) {
    visitExpression(o);
  }

  public void visitElement(@NotNull TactElement o) {
    visitCompositeElement(o);
  }

  public void visitElseBranch(@NotNull TactElseBranch o) {
    visitCompositeElement(o);
  }

  public void visitElseIfBranch(@NotNull TactElseIfBranch o) {
    visitCompositeElement(o);
  }

  public void visitExpression(@NotNull TactExpression o) {
    visitTypeOwner(o);
  }

  public void visitFieldDeclaration(@NotNull TactFieldDeclaration o) {
    visitCompositeElement(o);
  }

  public void visitFieldDefinition(@NotNull TactFieldDefinition o) {
    visitNamedElement(o);
  }

  public void visitFieldName(@NotNull TactFieldName o) {
    visitReferenceExpressionBase(o);
  }

  public void visitForEachStatement(@NotNull TactForEachStatement o) {
    visitStatement(o);
  }

  public void visitFunctionAttribute(@NotNull TactFunctionAttribute o) {
    visitCompositeElement(o);
  }

  public void visitFunctionDeclaration(@NotNull TactFunctionDeclaration o) {
    visitSignatureOwner(o);
    // visitFunctionOrMethodDeclaration(o);
    // visitAttributeOwner(o);
  }

  public void visitGetAttribute(@NotNull TactGetAttribute o) {
    visitCompositeElement(o);
  }

  public void visitIdent(@NotNull TactIdent o) {
    visitCompositeElement(o);
  }

  public void visitIfStatement(@NotNull TactIfStatement o) {
    visitStatement(o);
  }

  public void visitImportDeclaration(@NotNull TactImportDeclaration o) {
    visitCompositeElement(o);
  }

  public void visitImportList(@NotNull TactImportList o) {
    visitCompositeElement(o);
  }

  public void visitInitOfExpr(@NotNull TactInitOfExpr o) {
    visitExpression(o);
  }

  public void visitKey(@NotNull TactKey o) {
    visitCompositeElement(o);
  }

  public void visitLiteral(@NotNull TactLiteral o) {
    visitExpression(o);
  }

  public void visitLiteralValueExpression(@NotNull TactLiteralValueExpression o) {
    visitExpression(o);
  }

  public void visitMapType(@NotNull TactMapType o) {
    visitType(o);
  }

  public void visitMessageDeclaration(@NotNull TactMessageDeclaration o) {
    visitNamedElement(o);
  }

  public void visitMessageFunctionDeclaration(@NotNull TactMessageFunctionDeclaration o) {
    visitNamedElement(o);
  }

  public void visitMessageId(@NotNull TactMessageId o) {
    visitCompositeElement(o);
  }

  public void visitMessageKind(@NotNull TactMessageKind o) {
    visitCompositeElement(o);
  }

  public void visitMessageType(@NotNull TactMessageType o) {
    visitType(o);
    // visitFieldListOwner(o);
  }

  public void visitMulExpr(@NotNull TactMulExpr o) {
    visitBinaryExpr(o);
  }

  public void visitNativeFunctionDeclaration(@NotNull TactNativeFunctionDeclaration o) {
    visitNamedElement(o);
    // visitSignatureOwner(o);
  }

  public void visitOrExpr(@NotNull TactOrExpr o) {
    visitBinaryExpr(o);
  }

  public void visitParamDefinition(@NotNull TactParamDefinition o) {
    visitNamedElement(o);
  }

  public void visitParameters(@NotNull TactParameters o) {
    visitCompositeElement(o);
  }

  public void visitParenthesesExpr(@NotNull TactParenthesesExpr o) {
    visitExpression(o);
  }

  public void visitPlainAttribute(@NotNull TactPlainAttribute o) {
    visitCompositeElement(o);
  }

  public void visitPrimitiveDeclaration(@NotNull TactPrimitiveDeclaration o) {
    visitNamedElement(o);
  }

  public void visitPrimitiveType(@NotNull TactPrimitiveType o) {
    visitType(o);
  }

  public void visitReceiveStringId(@NotNull TactReceiveStringId o) {
    visitCompositeElement(o);
  }

  public void visitReferenceExpression(@NotNull TactReferenceExpression o) {
    visitExpression(o);
    // visitReferenceExpressionBase(o);
  }

  public void visitRepeatStatement(@NotNull TactRepeatStatement o) {
    visitStatement(o);
  }

  public void visitResult(@NotNull TactResult o) {
    visitTypeOwner(o);
  }

  public void visitReturnStatement(@NotNull TactReturnStatement o) {
    visitStatement(o);
  }

  public void visitSignature(@NotNull TactSignature o) {
    visitCompositeElement(o);
  }

  public void visitSimpleStatement(@NotNull TactSimpleStatement o) {
    visitStatement(o);
  }

  public void visitStatement(@NotNull TactStatement o) {
    visitCompositeElement(o);
  }

  public void visitStatementDestruct(@NotNull TactStatementDestruct o) {
    visitCompositeElement(o);
  }

  public void visitStringLiteral(@NotNull TactStringLiteral o) {
    visitExpression(o);
    // visitPsiLanguageInjectionHost(o);
  }

  public void visitStructDeclaration(@NotNull TactStructDeclaration o) {
    visitNamedElement(o);
    // visitAttributeOwner(o);
  }

  public void visitStructType(@NotNull TactStructType o) {
    visitType(o);
    // visitFieldListOwner(o);
  }

  public void visitTernaryExpr(@NotNull TactTernaryExpr o) {
    visitExpression(o);
  }

  public void visitTlb(@NotNull TactTlb o) {
    visitCompositeElement(o);
  }

  public void visitTraitDeclaration(@NotNull TactTraitDeclaration o) {
    visitNamedElement(o);
  }

  public void visitTraitType(@NotNull TactTraitType o) {
    visitType(o);
    // visitStorageMembersOwner(o);
  }

  public void visitTryStatement(@NotNull TactTryStatement o) {
    visitStatement(o);
  }

  public void visitTupleType(@NotNull TactTupleType o) {
    visitType(o);
  }

  public void visitType(@NotNull TactType o) {
    visitCompositeElement(o);
  }

  public void visitTypeExtra(@NotNull TactTypeExtra o) {
    visitCompositeElement(o);
  }

  public void visitTypeHint(@NotNull TactTypeHint o) {
    visitCompositeElement(o);
  }

  public void visitTypeListNoPin(@NotNull TactTypeListNoPin o) {
    visitCompositeElement(o);
  }

  public void visitTypeReferenceExpression(@NotNull TactTypeReferenceExpression o) {
    visitReferenceExpressionBase(o);
  }

  public void visitUnaryExpr(@NotNull TactUnaryExpr o) {
    visitExpression(o);
  }

  public void visitUntilStatement(@NotNull TactUntilStatement o) {
    visitStatement(o);
  }

  public void visitValue(@NotNull TactValue o) {
    visitCompositeElement(o);
  }

  public void visitVarDeclaration(@NotNull TactVarDeclaration o) {
    visitCompositeElement(o);
  }

  public void visitVarDefinition(@NotNull TactVarDefinition o) {
    visitNamedElement(o);
  }

  public void visitWhileStatement(@NotNull TactWhileStatement o) {
    visitStatement(o);
  }

  public void visitWithClause(@NotNull TactWithClause o) {
    visitCompositeElement(o);
  }

  public void visitSemi(@NotNull TactSemi o) {
    visitCompositeElement(o);
  }

  public void visitNamedElement(@NotNull TactNamedElement o) {
    visitCompositeElement(o);
  }

  public void visitReferenceExpressionBase(@NotNull TactReferenceExpressionBase o) {
    visitCompositeElement(o);
  }

  public void visitSignatureOwner(@NotNull TactSignatureOwner o) {
    visitCompositeElement(o);
  }

  public void visitTypeOwner(@NotNull TactTypeOwner o) {
    visitCompositeElement(o);
  }

  public void visitCompositeElement(@NotNull TactCompositeElement o) {
    visitElement(o);
  }

}
