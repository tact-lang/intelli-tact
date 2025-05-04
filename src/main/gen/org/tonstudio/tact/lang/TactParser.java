// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static org.tonstudio.tact.lang.TactTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class TactParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, EXTENDS_SETS_);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return File(b, l + 1);
  }

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[] {
    create_token_set_(BOUNCED_TYPE, CONTRACT_TYPE, MAP_TYPE, MESSAGE_TYPE,
      PRIMITIVE_TYPE, SET_TYPE, STRUCT_TYPE, TRAIT_TYPE,
      TUPLE_TYPE, TYPE),
    create_token_set_(ASSIGNMENT_STATEMENT, DESTRUCT_STATEMENT, FOR_EACH_STATEMENT, IF_STATEMENT,
      REPEAT_STATEMENT, RETURN_STATEMENT, SIMPLE_STATEMENT, STATEMENT,
      TRY_STATEMENT, UNTIL_STATEMENT, WHILE_STATEMENT),
    create_token_set_(ADD_EXPR, AND_EXPR, ASSERT_NOT_NULL_EXPR, CALL_EXPR,
      CODE_OF_EXPR, CONDITIONAL_EXPR, DOT_EXPRESSION, EXPRESSION,
      INIT_OF_EXPR, LITERAL, LITERAL_VALUE_EXPRESSION, MAP_LITERAL,
      MUL_EXPR, OR_EXPR, PARENTHESES_EXPR, REFERENCE_EXPRESSION,
      SET_LITERAL, STRING_LITERAL, TERNARY_EXPR, UNARY_EXPR),
  };

  /* ********************************************************** */
  // '+' | '-' | '|' | '^'
  static boolean AddOp(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AddOp")) return false;
    boolean r;
    r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, MINUS);
    if (!r) r = consumeToken(b, BIT_OR);
    if (!r) r = consumeToken(b, BIT_XOR);
    return r;
  }

  /* ********************************************************** */
  // '(' CommaElementList? ','? ')'
  public static boolean ArgumentList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArgumentList")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ARGUMENT_LIST, null);
    r = consumeToken(b, LPAREN);
    p = r; // pin = 1
    r = r && report_error_(b, ArgumentList_1(b, l + 1));
    r = p && report_error_(b, ArgumentList_2(b, l + 1)) && r;
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // CommaElementList?
  private static boolean ArgumentList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArgumentList_1")) return false;
    CommaElementList(b, l + 1);
    return true;
  }

  // ','?
  private static boolean ArgumentList_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArgumentList_2")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // AsmPrimitive*
  public static boolean AsmArguments(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmArguments")) return false;
    Marker m = enter_section_(b, l, _NONE_, ASM_ARGUMENTS, "<asm arguments>");
    while (true) {
      int c = current_position_(b);
      if (!AsmPrimitive(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "AsmArguments", c)) break;
    }
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // BIN_LITERAL
  public static boolean AsmBinBitstring(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmBinBitstring")) return false;
    if (!nextTokenIs(b, BIN_LITERAL)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BIN_LITERAL);
    exit_section_(b, m, ASM_BIN_BITSTRING, r);
    return r;
  }

  /* ********************************************************** */
  // (BOC_LITERAL | EMPTY_BOC_LITERAL) TO_BOC_FIFT?
  public static boolean AsmBocHex(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmBocHex")) return false;
    if (!nextTokenIs(b, "<asm boc hex>", BOC_LITERAL, EMPTY_BOC_LITERAL)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ASM_BOC_HEX, "<asm boc hex>");
    r = AsmBocHex_0(b, l + 1);
    r = r && AsmBocHex_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // BOC_LITERAL | EMPTY_BOC_LITERAL
  private static boolean AsmBocHex_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmBocHex_0")) return false;
    boolean r;
    r = consumeToken(b, BOC_LITERAL);
    if (!r) r = consumeToken(b, EMPTY_BOC_LITERAL);
    return r;
  }

  // TO_BOC_FIFT?
  private static boolean AsmBocHex_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmBocHex_1")) return false;
    consumeToken(b, TO_BOC_FIFT);
    return true;
  }

  /* ********************************************************** */
  // 'c0' | 'c1' | 'c2' | 'c3' | 'c4' | 'c5' | 'c6' | 'c7'
  public static boolean AsmControlRegister(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmControlRegister")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ASM_CONTROL_REGISTER, "<asm control register>");
    r = consumeToken(b, "c0");
    if (!r) r = consumeToken(b, "c1");
    if (!r) r = consumeToken(b, "c2");
    if (!r) r = consumeToken(b, "c3");
    if (!r) r = consumeToken(b, "c4");
    if (!r) r = consumeToken(b, "c5");
    if (!r) r = consumeToken(b, "c6");
    if (!r) r = consumeToken(b, "c7");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '<''b'
  public static boolean AsmCreateBuilder(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmCreateBuilder")) return false;
    if (!nextTokenIs(b, LESS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LESS);
    r = r && consumeToken(b, "b");
    exit_section_(b, m, ASM_CREATE_BUILDER, r);
    return r;
  }

  /* ********************************************************** */
  // AsmArguments AsmInstruction
  public static boolean AsmExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmExpression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ASM_EXPRESSION, "<asm expression>");
    r = AsmArguments(b, l + 1);
    r = r && AsmInstruction(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '{' AsmExpression* '}'
  public static boolean AsmFunctionBody(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmFunctionBody")) return false;
    if (!nextTokenIs(b, LBRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACE);
    r = r && AsmFunctionBody_1(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, m, ASM_FUNCTION_BODY, r);
    return r;
  }

  // AsmExpression*
  private static boolean AsmFunctionBody_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmFunctionBody_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!AsmExpression(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "AsmFunctionBody_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // AsmHeader FunctionAttribute* fun identifier Signature AsmFunctionBody
  public static boolean AsmFunctionDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmFunctionDeclaration")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ASM_FUNCTION_DECLARATION, "<asm function declaration>");
    r = AsmHeader(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, AsmFunctionDeclaration_1(b, l + 1));
    r = p && report_error_(b, consumeTokens(b, -1, FUN, IDENTIFIER)) && r;
    r = p && report_error_(b, Signature(b, l + 1)) && r;
    r = p && AsmFunctionBody(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // FunctionAttribute*
  private static boolean AsmFunctionDeclaration_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmFunctionDeclaration_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!FunctionAttribute(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "AsmFunctionDeclaration_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // 'asm' AsmShuffle?
  public static boolean AsmHeader(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmHeader")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ASM_HEADER, "<asm header>");
    r = consumeToken(b, "asm");
    p = r; // pin = 1
    r = r && AsmHeader_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // AsmShuffle?
  private static boolean AsmHeader_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmHeader_1")) return false;
    AsmShuffle(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // HEX_LITERAL
  public static boolean AsmHexBitstring(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmHexBitstring")) return false;
    if (!nextTokenIs(b, HEX_LITERAL)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, HEX_LITERAL);
    exit_section_(b, m, ASM_HEX_BITSTRING, r);
    return r;
  }

  /* ********************************************************** */
  // '-'? identifier
  public static boolean AsmInstruction(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmInstruction")) return false;
    if (!nextTokenIs(b, "<asm instruction>", IDENTIFIER, MINUS)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ASM_INSTRUCTION, "<asm instruction>");
    r = AsmInstruction_0(b, l + 1);
    r = r && consumeToken(b, IDENTIFIER);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '-'?
  private static boolean AsmInstruction_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmInstruction_0")) return false;
    consumeToken(b, MINUS);
    return true;
  }

  /* ********************************************************** */
  // int
  //     | hex
  //     | oct
  //     | bin
  public static boolean AsmInteger(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmInteger")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ASM_INTEGER, "<asm integer>");
    r = consumeToken(b, INT);
    if (!r) r = consumeToken(b, HEX);
    if (!r) r = consumeToken(b, OCT);
    if (!r) r = consumeToken(b, BIN);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // AsmSequence
  //     | AsmString
  //     | AsmHexBitstring
  //     | AsmBinBitstring
  //     | AsmBocHex
  //     | AsmControlRegister
  //     | AsmStackElement
  //     | AsmInteger
  //     | AsmCreateBuilder
  //     | AsmToCellBuilder
  //     | AsmStoreSlice
  public static boolean AsmPrimitive(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmPrimitive")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ASM_PRIMITIVE, "<asm primitive>");
    r = AsmSequence(b, l + 1);
    if (!r) r = AsmString(b, l + 1);
    if (!r) r = AsmHexBitstring(b, l + 1);
    if (!r) r = AsmBinBitstring(b, l + 1);
    if (!r) r = AsmBocHex(b, l + 1);
    if (!r) r = AsmControlRegister(b, l + 1);
    if (!r) r = AsmStackElement(b, l + 1);
    if (!r) r = AsmInteger(b, l + 1);
    if (!r) r = AsmCreateBuilder(b, l + 1);
    if (!r) r = AsmToCellBuilder(b, l + 1);
    if (!r) r = AsmStoreSlice(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '<''{' AsmExpression* ('}''>' ('c' | 's' | 'CONT')? | )
  public static boolean AsmSequence(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmSequence")) return false;
    if (!nextTokenIs(b, LESS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, LESS, LBRACE);
    r = r && AsmSequence_2(b, l + 1);
    r = r && AsmSequence_3(b, l + 1);
    exit_section_(b, m, ASM_SEQUENCE, r);
    return r;
  }

  // AsmExpression*
  private static boolean AsmSequence_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmSequence_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!AsmExpression(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "AsmSequence_2", c)) break;
    }
    return true;
  }

  // '}''>' ('c' | 's' | 'CONT')? | 
  private static boolean AsmSequence_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmSequence_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = AsmSequence_3_0(b, l + 1);
    if (!r) r = consumeToken(b, ASMSEQUENCE_3_1_0);
    exit_section_(b, m, null, r);
    return r;
  }

  // '}''>' ('c' | 's' | 'CONT')?
  private static boolean AsmSequence_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmSequence_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, RBRACE, GREATER);
    r = r && AsmSequence_3_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ('c' | 's' | 'CONT')?
  private static boolean AsmSequence_3_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmSequence_3_0_2")) return false;
    AsmSequence_3_0_2_0(b, l + 1);
    return true;
  }

  // 'c' | 's' | 'CONT'
  private static boolean AsmSequence_3_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmSequence_3_0_2_0")) return false;
    boolean r;
    r = consumeToken(b, "c");
    if (!r) r = consumeToken(b, "s");
    if (!r) r = consumeToken(b, "CONT");
    return r;
  }

  /* ********************************************************** */
  // '(' ReferenceExpression* ('->' int+)? ')'
  public static boolean AsmShuffle(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmShuffle")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && AsmShuffle_1(b, l + 1);
    r = r && AsmShuffle_2(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, ASM_SHUFFLE, r);
    return r;
  }

  // ReferenceExpression*
  private static boolean AsmShuffle_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmShuffle_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ReferenceExpression(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "AsmShuffle_1", c)) break;
    }
    return true;
  }

  // ('->' int+)?
  private static boolean AsmShuffle_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmShuffle_2")) return false;
    AsmShuffle_2_0(b, l + 1);
    return true;
  }

  // '->' int+
  private static boolean AsmShuffle_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmShuffle_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ARROW);
    r = r && AsmShuffle_2_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // int+
  private static boolean AsmShuffle_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmShuffle_2_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, INT);
    while (r) {
      int c = current_position_(b);
      if (!consumeToken(b, INT)) break;
      if (!empty_element_parsed_guard_(b, "AsmShuffle_2_0_1", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 's0' | 's1' | 's2' | 's3' | 's4' | 's5' | 's6' | 's7' | 's8' | 's9' | 's10' | 's11' | 's12' | 's13' | 's14' | 's15' | 's()' | 's(-1)' | 's(-2)' | 's(-3)' | 's(-4)'
  public static boolean AsmStackElement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmStackElement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ASM_STACK_ELEMENT, "<asm stack element>");
    r = consumeToken(b, "s0");
    if (!r) r = consumeToken(b, "s1");
    if (!r) r = consumeToken(b, "s2");
    if (!r) r = consumeToken(b, "s3");
    if (!r) r = consumeToken(b, "s4");
    if (!r) r = consumeToken(b, "s5");
    if (!r) r = consumeToken(b, "s6");
    if (!r) r = consumeToken(b, "s7");
    if (!r) r = consumeToken(b, "s8");
    if (!r) r = consumeToken(b, "s9");
    if (!r) r = consumeToken(b, "s10");
    if (!r) r = consumeToken(b, "s11");
    if (!r) r = consumeToken(b, "s12");
    if (!r) r = consumeToken(b, "s13");
    if (!r) r = consumeToken(b, "s14");
    if (!r) r = consumeToken(b, "s15");
    if (!r) r = consumeToken(b, "s()");
    if (!r) r = consumeToken(b, "s(-1)");
    if (!r) r = consumeToken(b, "s(-2)");
    if (!r) r = consumeToken(b, "s(-3)");
    if (!r) r = consumeToken(b, "s(-4)");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 's'','
  public static boolean AsmStoreSlice(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmStoreSlice")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ASM_STORE_SLICE, "<asm store slice>");
    r = consumeToken(b, "s");
    r = r && consumeToken(b, COMMA);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ('abort' | '.' | '+')? StringLiteral
  public static boolean AsmString(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmString")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ASM_STRING, "<asm string>");
    r = AsmString_0(b, l + 1);
    r = r && StringLiteral(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ('abort' | '.' | '+')?
  private static boolean AsmString_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmString_0")) return false;
    AsmString_0_0(b, l + 1);
    return true;
  }

  // 'abort' | '.' | '+'
  private static boolean AsmString_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmString_0_0")) return false;
    boolean r;
    r = consumeToken(b, "abort");
    if (!r) r = consumeToken(b, DOT);
    if (!r) r = consumeToken(b, PLUS);
    return r;
  }

  /* ********************************************************** */
  // 'b''>'
  public static boolean AsmToCellBuilder(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmToCellBuilder")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ASM_TO_CELL_BUILDER, "<asm to cell builder>");
    r = consumeToken(b, "b");
    r = r && consumeToken(b, GREATER);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '!!'
  public static boolean AssertNotNullExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AssertNotNullExpr")) return false;
    if (!nextTokenIs(b, ASSERT_OP)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_, ASSERT_NOT_NULL_EXPR, null);
    r = consumeToken(b, ASSERT_OP);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '=' | '+=' | '-=' | '|=' | '^=' | '*=' | '/=' | '%=' | '<<=' | '>>=' | '&=' | '&&=' | '||='
  public static boolean AssignOp(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AssignOp")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ASSIGN_OP, "<assign op>");
    r = consumeToken(b, ASSIGN);
    if (!r) r = consumeToken(b, PLUS_ASSIGN);
    if (!r) r = consumeToken(b, MINUS_ASSIGN);
    if (!r) r = consumeToken(b, BIT_OR_ASSIGN);
    if (!r) r = consumeToken(b, BIT_XOR_ASSIGN);
    if (!r) r = consumeToken(b, MUL_ASSIGN);
    if (!r) r = consumeToken(b, QUOTIENT_ASSIGN);
    if (!r) r = consumeToken(b, REMAINDER_ASSIGN);
    if (!r) r = consumeToken(b, SHIFT_LEFT_ASSIGN);
    if (!r) r = consumeToken(b, SHIFT_RIGHT_ASSIGN);
    if (!r) r = consumeToken(b, BIT_AND_ASSIGN);
    if (!r) r = consumeToken(b, AND_ASSIGN);
    if (!r) r = consumeToken(b, OR_ASSIGN);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // Expression AssignmentStatement semi
  static boolean AssignStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AssignStatement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = Expression(b, l + 1, -1);
    r = r && AssignmentStatement(b, l + 1);
    p = r; // pin = 2
    r = r && semi(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // AssignOp Expression
  public static boolean AssignmentStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AssignmentStatement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _LEFT_, ASSIGNMENT_STATEMENT, "<assignment statement>");
    r = AssignOp(b, l + 1);
    p = r; // pin = 1
    r = r && Expression(b, l + 1, -1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // '@' AttributeExpression
  public static boolean Attribute(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Attribute")) return false;
    if (!nextTokenIs(b, AT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ATTRIBUTE, null);
    r = consumeToken(b, AT);
    p = r; // pin = 1
    r = r && AttributeExpression(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // ArgumentList
  static boolean AttributeArgs(PsiBuilder b, int l) {
    return ArgumentList(b, l + 1);
  }

  /* ********************************************************** */
  // PlainAttribute
  public static boolean AttributeExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AttributeExpression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ATTRIBUTE_EXPRESSION, "<attribute expression>");
    r = PlainAttribute(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // identifier
  public static boolean AttributeIdentifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AttributeIdentifier")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, ATTRIBUTE_IDENTIFIER, r);
    return r;
  }

  /* ********************************************************** */
  // AttributeIdentifier | Literal
  public static boolean AttributeKey(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AttributeKey")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ATTRIBUTE_KEY, "<attribute key>");
    r = AttributeIdentifier(b, l + 1);
    if (!r) r = Literal(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // Attribute Attribute*
  public static boolean Attributes(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Attributes")) return false;
    if (!nextTokenIs(b, AT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ATTRIBUTES, null);
    r = Attribute(b, l + 1);
    p = r; // pin = 1
    r = r && Attributes_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Attribute*
  private static boolean Attributes_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Attributes_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Attribute(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "Attributes_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // '{' Statements '}'
  public static boolean Block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Block")) return false;
    if (!nextTokenIs(b, LBRACE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, BLOCK, null);
    r = consumeToken(b, LBRACE);
    p = r; // pin = 1
    r = r && report_error_(b, Statements(b, l + 1));
    r = p && consumeToken(b, RBRACE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // '{' Statements '}'
  public static boolean BlockNoPin(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BlockNoPin")) return false;
    if (!nextTokenIs(b, LBRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACE);
    r = r && Statements(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, m, BLOCK, r);
    return r;
  }

  /* ********************************************************** */
  // 'bounced' '<' Type '>'
  public static boolean BouncedType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BouncedType")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BOUNCED_TYPE, "<bounced type>");
    r = consumeToken(b, "bounced");
    r = r && consumeToken(b, LESS);
    r = r && Type(b, l + 1);
    r = r && consumeToken(b, GREATER);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ArgumentList
  public static boolean CallExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CallExpr")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_, CALL_EXPR, null);
    r = ArgumentList(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // catch '(' VarDefinition ')' Block
  public static boolean CatchClause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CatchClause")) return false;
    if (!nextTokenIs(b, CATCH)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CATCH_CLAUSE, null);
    r = consumeTokens(b, 1, CATCH, LPAREN);
    p = r; // pin = 1
    r = r && report_error_(b, VarDefinition(b, l + 1));
    r = p && report_error_(b, consumeToken(b, RPAREN)) && r;
    r = p && Block(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // 'codeOf' TypeReferenceExpression
  public static boolean CodeOfExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CodeOfExpr")) return false;
    if (!nextTokenIs(b, CODE_OF)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CODE_OF_EXPR, null);
    r = consumeToken(b, CODE_OF);
    p = r; // pin = 1
    r = r && TypeReferenceExpression(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // Expression (',' Expression?)*
  static boolean CommaElementList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CommaElementList")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = Expression(b, l + 1, -1);
    p = r; // pin = 1
    r = r && CommaElementList_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (',' Expression?)*
  private static boolean CommaElementList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CommaElementList_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!CommaElementList_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "CommaElementList_1", c)) break;
    }
    return true;
  }

  // ',' Expression?
  private static boolean CommaElementList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CommaElementList_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && CommaElementList_1_0_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Expression?
  private static boolean CommaElementList_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CommaElementList_1_0_1")) return false;
    Expression(b, l + 1, -1);
    return true;
  }

  /* ********************************************************** */
  // ConstantModifier* const ConstDefinition semi
  public static boolean ConstDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstDeclaration")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CONST_DECLARATION, "<const declaration>");
    r = ConstDeclaration_0(b, l + 1);
    r = r && consumeToken(b, CONST);
    p = r; // pin = 2
    r = r && report_error_(b, ConstDefinition(b, l + 1));
    r = p && semi(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ConstantModifier*
  private static boolean ConstDeclaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstDeclaration_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ConstantModifier(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ConstDeclaration_0", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // identifier TypeHint ('=' Expression)?
  public static boolean ConstDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstDefinition")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CONST_DEFINITION, null);
    r = consumeToken(b, IDENTIFIER);
    p = r; // pin = 1
    r = r && report_error_(b, TypeHint(b, l + 1));
    r = p && ConstDefinition_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ('=' Expression)?
  private static boolean ConstDefinition_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstDefinition_2")) return false;
    ConstDefinition_2_0(b, l + 1);
    return true;
  }

  // '=' Expression
  private static boolean ConstDefinition_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstDefinition_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ASSIGN);
    r = r && Expression(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // virtual | override | abstract
  public static boolean ConstantModifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstantModifier")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CONSTANT_MODIFIER, "<constant modifier>");
    r = consumeToken(b, VIRTUAL);
    if (!r) r = consumeToken(b, OVERRIDE);
    if (!r) r = consumeToken(b, ABSTRACT);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // Attributes? ContractType
  public static boolean ContractDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ContractDeclaration")) return false;
    if (!nextTokenIs(b, "<contract declaration>", AT, CONTRACT)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CONTRACT_DECLARATION, "<contract declaration>");
    r = ContractDeclaration_0(b, l + 1);
    r = r && ContractType(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // Attributes?
  private static boolean ContractDeclaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ContractDeclaration_0")) return false;
    Attributes(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'init' Parameters Block
  public static boolean ContractInitDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ContractInitDeclaration")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CONTRACT_INIT_DECLARATION, "<contract init declaration>");
    r = consumeToken(b, "init");
    p = r; // pin = 1
    r = r && report_error_(b, Parameters(b, l + 1));
    r = p && Block(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // FieldDeclaration (',' (FieldDeclaration | &')'))* ','?
  static boolean ContractParameterList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ContractParameterList")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = FieldDeclaration(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, ContractParameterList_1(b, l + 1));
    r = p && ContractParameterList_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (',' (FieldDeclaration | &')'))*
  private static boolean ContractParameterList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ContractParameterList_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ContractParameterList_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ContractParameterList_1", c)) break;
    }
    return true;
  }

  // ',' (FieldDeclaration | &')')
  private static boolean ContractParameterList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ContractParameterList_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && ContractParameterList_1_0_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // FieldDeclaration | &')'
  private static boolean ContractParameterList_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ContractParameterList_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = FieldDeclaration(b, l + 1);
    if (!r) r = ContractParameterList_1_0_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // &')'
  private static boolean ContractParameterList_1_0_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ContractParameterList_1_0_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = consumeToken(b, RPAREN);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ','?
  private static boolean ContractParameterList_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ContractParameterList_2")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // '(' ContractParameterList? ','? ')'
  public static boolean ContractParameters(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ContractParameters")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CONTRACT_PARAMETERS, null);
    r = consumeToken(b, LPAREN);
    p = r; // pin = 1
    r = r && report_error_(b, ContractParameters_1(b, l + 1));
    r = p && report_error_(b, ContractParameters_2(b, l + 1)) && r;
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ContractParameterList?
  private static boolean ContractParameters_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ContractParameters_1")) return false;
    ContractParameterList(b, l + 1);
    return true;
  }

  // ','?
  private static boolean ContractParameters_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ContractParameters_2")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // contract identifier ContractParameters? WithClause? '{' MemberItem* '}'
  public static boolean ContractType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ContractType")) return false;
    if (!nextTokenIs(b, CONTRACT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CONTRACT_TYPE, null);
    r = consumeTokens(b, 1, CONTRACT, IDENTIFIER);
    p = r; // pin = 1
    r = r && report_error_(b, ContractType_2(b, l + 1));
    r = p && report_error_(b, ContractType_3(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, LBRACE)) && r;
    r = p && report_error_(b, ContractType_5(b, l + 1)) && r;
    r = p && consumeToken(b, RBRACE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ContractParameters?
  private static boolean ContractType_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ContractType_2")) return false;
    ContractParameters(b, l + 1);
    return true;
  }

  // WithClause?
  private static boolean ContractType_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ContractType_3")) return false;
    WithClause(b, l + 1);
    return true;
  }

  // MemberItem*
  private static boolean ContractType_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ContractType_5")) return false;
    while (true) {
      int c = current_position_(b);
      if (!MemberItem(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ContractType_5", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // '=' Expression
  public static boolean DefaultFieldValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "DefaultFieldValue")) return false;
    if (!nextTokenIs(b, ASSIGN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, DEFAULT_FIELD_VALUE, null);
    r = consumeToken(b, ASSIGN);
    p = r; // pin = 1
    r = r && Expression(b, l + 1, -1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // ReferenceExpression ':' VarDefinition
  //   | VarDefinition
  public static boolean DestructItem(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "DestructItem")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = DestructItem_0(b, l + 1);
    if (!r) r = VarDefinition(b, l + 1);
    exit_section_(b, m, DESTRUCT_ITEM, r);
    return r;
  }

  // ReferenceExpression ':' VarDefinition
  private static boolean DestructItem_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "DestructItem_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ReferenceExpression(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && VarDefinition(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // DestructItem ("," DestructItem)* ("," "..")? ","?
  static boolean DestructList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "DestructList")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = DestructItem(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, DestructList_1(b, l + 1));
    r = p && report_error_(b, DestructList_2(b, l + 1)) && r;
    r = p && DestructList_3(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ("," DestructItem)*
  private static boolean DestructList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "DestructList_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!DestructList_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "DestructList_1", c)) break;
    }
    return true;
  }

  // "," DestructItem
  private static boolean DestructList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "DestructList_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && DestructItem(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ("," "..")?
  private static boolean DestructList_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "DestructList_2")) return false;
    DestructList_2_0(b, l + 1);
    return true;
  }

  // "," ".."
  private static boolean DestructList_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "DestructList_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && consumeToken(b, "..");
    exit_section_(b, m, null, r);
    return r;
  }

  // ","?
  private static boolean DestructList_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "DestructList_3")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // let TypeReferenceExpression "{" DestructList "}" "=" Expression semi
  public static boolean DestructStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "DestructStatement")) return false;
    if (!nextTokenIs(b, LET)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, DESTRUCT_STATEMENT, null);
    r = consumeToken(b, LET);
    r = r && TypeReferenceExpression(b, l + 1);
    r = r && consumeToken(b, LBRACE);
    p = r; // pin = 3
    r = r && report_error_(b, DestructList(b, l + 1));
    r = p && report_error_(b, consumeTokens(b, -1, RBRACE, ASSIGN)) && r;
    r = p && report_error_(b, Expression(b, l + 1, -1)) && r;
    r = p && semi(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // LiteralValueExpression
  //   | MapLiteral
  //   | SetLiteral
  //   | ReferenceExpression
  //   | InitOfExpr
  //   | CodeOfExpr
  //   | ParenthesesExpr
  //   | Literal
  static boolean DotPrimaryExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "DotPrimaryExpr")) return false;
    boolean r;
    r = LiteralValueExpression(b, l + 1);
    if (!r) r = MapLiteral(b, l + 1);
    if (!r) r = SetLiteral(b, l + 1);
    if (!r) r = ReferenceExpression(b, l + 1);
    if (!r) r = InitOfExpr(b, l + 1);
    if (!r) r = CodeOfExpr(b, l + 1);
    if (!r) r = ParenthesesExpr(b, l + 1);
    if (!r) r = Literal(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // 'DummyRightHandRule'
  public static boolean DummyRightHandRule(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "DummyRightHandRule")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_, DUMMY_RIGHT_HAND_RULE, "<dummy right hand rule>");
    r = consumeToken(b, "DummyRightHandRule");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // else Block
  public static boolean ElseBranch(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ElseBranch")) return false;
    if (!nextTokenIs(b, ELSE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ELSE_BRANCH, null);
    r = consumeToken(b, ELSE);
    p = r; // pin = 1
    r = r && Block(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // else IfStatementWithoutElse
  public static boolean ElseIfBranch(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ElseIfBranch")) return false;
    if (!nextTokenIs(b, ELSE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ELSE);
    r = r && IfStatementWithoutElse(b, l + 1);
    exit_section_(b, m, ELSE_IF_BRANCH, r);
    return r;
  }

  /* ********************************************************** */
  // Expression semi
  static boolean ExpressionStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ExpressionStatement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = Expression(b, l + 1, -1);
    p = r; // pin = 1
    r = r && semi(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // FieldDefinition ':' Type DefaultFieldValue? semi?
  public static boolean FieldDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldDeclaration")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FIELD_DECLARATION, null);
    r = FieldDefinition(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, consumeToken(b, COLON));
    r = p && report_error_(b, Type(b, l + 1)) && r;
    r = p && report_error_(b, FieldDeclaration_3(b, l + 1)) && r;
    r = p && FieldDeclaration_4(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // DefaultFieldValue?
  private static boolean FieldDeclaration_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldDeclaration_3")) return false;
    DefaultFieldValue(b, l + 1);
    return true;
  }

  // semi?
  private static boolean FieldDeclaration_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldDeclaration_4")) return false;
    semi(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // identifier
  public static boolean FieldDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldDefinition")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, FIELD_DEFINITION, r);
    return r;
  }

  /* ********************************************************** */
  // ReferenceExpression
  public static boolean FieldName(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldName")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ReferenceExpression(b, l + 1);
    exit_section_(b, m, FIELD_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // ImportList? TopLevelDeclaration*
  static boolean File(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "File")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = File_0(b, l + 1);
    r = r && File_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ImportList?
  private static boolean File_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "File_0")) return false;
    ImportList(b, l + 1);
    return true;
  }

  // TopLevelDeclaration*
  private static boolean File_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "File_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!TopLevelDeclaration(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "File_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // foreach '(' VarDefinition ',' VarDefinition in Expression ')' Block
  public static boolean ForEachStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ForEachStatement")) return false;
    if (!nextTokenIs(b, FOREACH)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FOR_EACH_STATEMENT, null);
    r = consumeTokens(b, 1, FOREACH, LPAREN);
    p = r; // pin = 1
    r = r && report_error_(b, VarDefinition(b, l + 1));
    r = p && report_error_(b, consumeToken(b, COMMA)) && r;
    r = p && report_error_(b, VarDefinition(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, IN)) && r;
    r = p && report_error_(b, Expression(b, l + 1, -1)) && r;
    r = p && report_error_(b, consumeToken(b, RPAREN)) && r;
    r = p && Block(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // GetAttribute
  //   | mutates
  //   | extends
  //   | virtual
  //   | override
  //   | inline
  //   | abstract
  public static boolean FunctionAttribute(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FunctionAttribute")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_ATTRIBUTE, "<function attribute>");
    r = GetAttribute(b, l + 1);
    if (!r) r = consumeToken(b, MUTATES);
    if (!r) r = consumeToken(b, EXTENDS);
    if (!r) r = consumeToken(b, VIRTUAL);
    if (!r) r = consumeToken(b, OVERRIDE);
    if (!r) r = consumeToken(b, INLINE);
    if (!r) r = consumeToken(b, ABSTRACT);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // Attributes? FunctionAttribute* fun identifier Signature (Block | semi)
  public static boolean FunctionDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FunctionDeclaration")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_DECLARATION, "<function declaration>");
    r = FunctionDeclaration_0(b, l + 1);
    r = r && FunctionDeclaration_1(b, l + 1);
    r = r && consumeTokens(b, 0, FUN, IDENTIFIER);
    r = r && Signature(b, l + 1);
    p = r; // pin = 5
    r = r && FunctionDeclaration_5(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Attributes?
  private static boolean FunctionDeclaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FunctionDeclaration_0")) return false;
    Attributes(b, l + 1);
    return true;
  }

  // FunctionAttribute*
  private static boolean FunctionDeclaration_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FunctionDeclaration_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!FunctionAttribute(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "FunctionDeclaration_1", c)) break;
    }
    return true;
  }

  // Block | semi
  private static boolean FunctionDeclaration_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FunctionDeclaration_5")) return false;
    boolean r;
    r = Block(b, l + 1);
    if (!r) r = semi(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // 'get' ('(' Expression ')')?
  public static boolean GetAttribute(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GetAttribute")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, GET_ATTRIBUTE, "<get attribute>");
    r = consumeToken(b, "get");
    r = r && GetAttribute_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ('(' Expression ')')?
  private static boolean GetAttribute_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GetAttribute_1")) return false;
    GetAttribute_1_0(b, l + 1);
    return true;
  }

  // '(' Expression ')'
  private static boolean GetAttribute_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GetAttribute_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && Expression(b, l + 1, -1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // identifier
  public static boolean Ident(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Ident")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, IDENT, r);
    return r;
  }

  /* ********************************************************** */
  // if '(' Expression ')' Block ElseIfBranch* ElseBranch?
  public static boolean IfStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IfStatement")) return false;
    if (!nextTokenIs(b, IF)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, IF_STATEMENT, null);
    r = consumeTokens(b, 1, IF, LPAREN);
    p = r; // pin = 1
    r = r && report_error_(b, Expression(b, l + 1, -1));
    r = p && report_error_(b, consumeToken(b, RPAREN)) && r;
    r = p && report_error_(b, Block(b, l + 1)) && r;
    r = p && report_error_(b, IfStatement_5(b, l + 1)) && r;
    r = p && IfStatement_6(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ElseIfBranch*
  private static boolean IfStatement_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IfStatement_5")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ElseIfBranch(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "IfStatement_5", c)) break;
    }
    return true;
  }

  // ElseBranch?
  private static boolean IfStatement_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IfStatement_6")) return false;
    ElseBranch(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // if Expression Block
  public static boolean IfStatementWithoutElse(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IfStatementWithoutElse")) return false;
    if (!nextTokenIs(b, IF)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, IF_STATEMENT, null);
    r = consumeToken(b, IF);
    p = r; // pin = 1
    r = r && report_error_(b, Expression(b, l + 1, -1));
    r = p && Block(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // import StringLiteral ';'
  public static boolean ImportDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ImportDeclaration")) return false;
    if (!nextTokenIs(b, IMPORT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, IMPORT_DECLARATION, null);
    r = consumeToken(b, IMPORT);
    p = r; // pin = 1
    r = r && report_error_(b, StringLiteral(b, l + 1));
    r = p && consumeToken(b, SEMICOLON) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // ImportDeclaration+
  public static boolean ImportList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ImportList")) return false;
    if (!nextTokenIs(b, IMPORT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ImportDeclaration(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!ImportDeclaration(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ImportList", c)) break;
    }
    exit_section_(b, m, IMPORT_LIST, r);
    return r;
  }

  /* ********************************************************** */
  // 'initOf' TypeReferenceExpression ArgumentList
  public static boolean InitOfExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "InitOfExpr")) return false;
    if (!nextTokenIs(b, INIT_OF)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, INIT_OF_EXPR, null);
    r = consumeToken(b, INIT_OF);
    p = r; // pin = 1
    r = r && report_error_(b, TypeReferenceExpression(b, l + 1));
    r = p && ArgumentList(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // InstanceArgumentFull | InstanceArgumentShort
  public static boolean InstanceArgument(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "InstanceArgument")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = InstanceArgumentFull(b, l + 1);
    if (!r) r = InstanceArgumentShort(b, l + 1);
    exit_section_(b, m, INSTANCE_ARGUMENT, r);
    return r;
  }

  /* ********************************************************** */
  // FieldName ':' Expression?
  public static boolean InstanceArgumentFull(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "InstanceArgumentFull")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, INSTANCE_ARGUMENT_FULL, null);
    r = FieldName(b, l + 1);
    r = r && consumeToken(b, COLON);
    p = r; // pin = 2
    r = r && InstanceArgumentFull_2(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Expression?
  private static boolean InstanceArgumentFull_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "InstanceArgumentFull_2")) return false;
    Expression(b, l + 1, -1);
    return true;
  }

  /* ********************************************************** */
  // ReferenceExpression
  public static boolean InstanceArgumentShort(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "InstanceArgumentShort")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ReferenceExpression(b, l + 1);
    exit_section_(b, m, INSTANCE_ARGUMENT_SHORT, r);
    return r;
  }

  /* ********************************************************** */
  // InstanceArgument (','? InstanceArgument)* ','?
  public static boolean InstanceArguments(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "InstanceArguments")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = InstanceArgument(b, l + 1);
    r = r && InstanceArguments_1(b, l + 1);
    r = r && InstanceArguments_2(b, l + 1);
    exit_section_(b, m, INSTANCE_ARGUMENTS, r);
    return r;
  }

  // (','? InstanceArgument)*
  private static boolean InstanceArguments_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "InstanceArguments_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!InstanceArguments_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "InstanceArguments_1", c)) break;
    }
    return true;
  }

  // ','? InstanceArgument
  private static boolean InstanceArguments_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "InstanceArguments_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = InstanceArguments_1_0_0(b, l + 1);
    r = r && InstanceArgument(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ','?
  private static boolean InstanceArguments_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "InstanceArguments_1_0_0")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  // ','?
  private static boolean InstanceArguments_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "InstanceArguments_2")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // Type '{' InstanceArguments? '}'
  public static boolean LiteralValueExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "LiteralValueExpression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LITERAL_VALUE_EXPRESSION, "<literal value expression>");
    r = Type(b, l + 1);
    r = r && consumeToken(b, LBRACE);
    r = r && LiteralValueExpression_2(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // InstanceArguments?
  private static boolean LiteralValueExpression_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "LiteralValueExpression_2")) return false;
    InstanceArguments(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // MapEntry (',' MapEntry)* ','?
  static boolean MapEntries(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MapEntries")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = MapEntry(b, l + 1);
    r = r && MapEntries_1(b, l + 1);
    r = r && MapEntries_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (',' MapEntry)*
  private static boolean MapEntries_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MapEntries_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!MapEntries_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "MapEntries_1", c)) break;
    }
    return true;
  }

  // ',' MapEntry
  private static boolean MapEntries_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MapEntries_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && MapEntry(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ','?
  private static boolean MapEntries_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MapEntries_2")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // Expression ':' Expression
  public static boolean MapEntry(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MapEntry")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MAP_ENTRY, "<map entry>");
    r = Expression(b, l + 1, -1);
    r = r && consumeToken(b, COLON);
    r = r && Expression(b, l + 1, -1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // MapType '{' MapEntries? '}'
  public static boolean MapLiteral(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MapLiteral")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MAP_LITERAL, "<map literal>");
    r = MapType(b, l + 1);
    r = r && consumeToken(b, LBRACE);
    r = r && MapLiteral_2(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // MapEntries?
  private static boolean MapLiteral_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MapLiteral_2")) return false;
    MapEntries(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'map' '<' Type ',' Type '>'
  public static boolean MapType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MapType")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, MAP_TYPE, "<map type>");
    r = consumeToken(b, "map");
    p = r; // pin = 1
    r = r && report_error_(b, consumeToken(b, LESS));
    r = p && report_error_(b, Type(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, COMMA)) && r;
    r = p && report_error_(b, Type(b, l + 1)) && r;
    r = p && consumeToken(b, GREATER) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // ConstDeclaration
  //   | ContractInitDeclaration
  //   | MessageFunctionDeclaration
  //   | FunctionDeclaration
  //   | FieldDeclaration
  static boolean MemberItem(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MemberItem")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = ConstDeclaration(b, l + 1);
    if (!r) r = ContractInitDeclaration(b, l + 1);
    if (!r) r = MessageFunctionDeclaration(b, l + 1);
    if (!r) r = FunctionDeclaration(b, l + 1);
    if (!r) r = FieldDeclaration(b, l + 1);
    exit_section_(b, l, m, r, false, TactParser::MembersRecover);
    return r;
  }

  /* ********************************************************** */
  // !(
  //       '@' // attribute start
  //     | '}' // contract/trait end
  //     // receivers
  //     | receive
  //     | external
  //     | bounced
  //     // constants
  //     | const
  //     // functions
  //     | fun
  //     | inline
  //     | abstract
  //     | virtual
  //     | 'init'
  //     | mutates
  //     | extends
  //     | override
  //     // fields
  //     | identifier
  // )
  static boolean MembersRecover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MembersRecover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !MembersRecover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '@' // attribute start
  //     | '}' // contract/trait end
  //     // receivers
  //     | receive
  //     | external
  //     | bounced
  //     // constants
  //     | const
  //     // functions
  //     | fun
  //     | inline
  //     | abstract
  //     | virtual
  //     | 'init'
  //     | mutates
  //     | extends
  //     | override
  //     // fields
  //     | identifier
  private static boolean MembersRecover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MembersRecover_0")) return false;
    boolean r;
    r = consumeToken(b, AT);
    if (!r) r = consumeToken(b, RBRACE);
    if (!r) r = consumeToken(b, RECEIVE);
    if (!r) r = consumeToken(b, EXTERNAL);
    if (!r) r = consumeToken(b, BOUNCED);
    if (!r) r = consumeToken(b, CONST);
    if (!r) r = consumeToken(b, FUN);
    if (!r) r = consumeToken(b, INLINE);
    if (!r) r = consumeToken(b, ABSTRACT);
    if (!r) r = consumeToken(b, VIRTUAL);
    if (!r) r = consumeToken(b, "init");
    if (!r) r = consumeToken(b, MUTATES);
    if (!r) r = consumeToken(b, EXTENDS);
    if (!r) r = consumeToken(b, OVERRIDE);
    if (!r) r = consumeToken(b, IDENTIFIER);
    return r;
  }

  /* ********************************************************** */
  // MessageType
  public static boolean MessageDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageDeclaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MESSAGE_DECLARATION, "<message declaration>");
    r = MessageType(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // MessageKind (ReceiveStringId | Parameters) Block
  public static boolean MessageFunctionDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageFunctionDeclaration")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, MESSAGE_FUNCTION_DECLARATION, "<message function declaration>");
    r = MessageKind(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, MessageFunctionDeclaration_1(b, l + 1));
    r = p && Block(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ReceiveStringId | Parameters
  private static boolean MessageFunctionDeclaration_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageFunctionDeclaration_1")) return false;
    boolean r;
    r = ReceiveStringId(b, l + 1);
    if (!r) r = Parameters(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // '(' Expression ')'
  public static boolean MessageId(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageId")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, MESSAGE_ID, null);
    r = consumeToken(b, LPAREN);
    p = r; // pin = 1
    r = r && report_error_(b, Expression(b, l + 1, -1));
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // receive | external | 'bounced'
  public static boolean MessageKind(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageKind")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MESSAGE_KIND, "<message kind>");
    r = consumeToken(b, RECEIVE);
    if (!r) r = consumeToken(b, EXTERNAL);
    if (!r) r = consumeToken(b, "bounced");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'message' MessageId? Ident '{' FieldDeclaration* '}'
  public static boolean MessageType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageType")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, MESSAGE_TYPE, "<message type>");
    r = consumeToken(b, "message");
    p = r; // pin = 1
    r = r && report_error_(b, MessageType_1(b, l + 1));
    r = p && report_error_(b, Ident(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, LBRACE)) && r;
    r = p && report_error_(b, MessageType_4(b, l + 1)) && r;
    r = p && consumeToken(b, RBRACE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // MessageId?
  private static boolean MessageType_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageType_1")) return false;
    MessageId(b, l + 1);
    return true;
  }

  // FieldDeclaration*
  private static boolean MessageType_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MessageType_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!FieldDeclaration(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "MessageType_4", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // '*' | '/' | '%' | '<<' | '>>' | '&'
  static boolean MulOp(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MulOp")) return false;
    boolean r;
    r = consumeToken(b, MUL);
    if (!r) r = consumeToken(b, QUOTIENT);
    if (!r) r = consumeToken(b, REMAINDER);
    if (!r) r = consumeToken(b, SHIFT_LEFT);
    if (!r) r = consumeToken(b, SHIFT_RIGHT);
    if (!r) r = consumeToken(b, BIT_AND);
    return r;
  }

  /* ********************************************************** */
  // Attributes? FunctionAttribute* native identifier Signature ';'
  public static boolean NativeFunctionDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "NativeFunctionDeclaration")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, NATIVE_FUNCTION_DECLARATION, "<native function declaration>");
    r = NativeFunctionDeclaration_0(b, l + 1);
    r = r && NativeFunctionDeclaration_1(b, l + 1);
    r = r && consumeTokens(b, 1, NATIVE, IDENTIFIER);
    p = r; // pin = 3
    r = r && report_error_(b, Signature(b, l + 1));
    r = p && consumeToken(b, SEMICOLON) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Attributes?
  private static boolean NativeFunctionDeclaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "NativeFunctionDeclaration_0")) return false;
    Attributes(b, l + 1);
    return true;
  }

  // FunctionAttribute*
  private static boolean NativeFunctionDeclaration_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "NativeFunctionDeclaration_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!FunctionAttribute(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "NativeFunctionDeclaration_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // identifier ':' Type
  public static boolean ParamDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParamDefinition")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, IDENTIFIER, COLON);
    r = r && Type(b, l + 1);
    exit_section_(b, m, PARAM_DEFINITION, r);
    return r;
  }

  /* ********************************************************** */
  // ParamDefinition (',' (ParamDefinition | &')'))* ','?
  static boolean ParameterList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParameterList")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = ParamDefinition(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, ParameterList_1(b, l + 1));
    r = p && ParameterList_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (',' (ParamDefinition | &')'))*
  private static boolean ParameterList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParameterList_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ParameterList_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ParameterList_1", c)) break;
    }
    return true;
  }

  // ',' (ParamDefinition | &')')
  private static boolean ParameterList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParameterList_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && ParameterList_1_0_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ParamDefinition | &')'
  private static boolean ParameterList_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParameterList_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ParamDefinition(b, l + 1);
    if (!r) r = ParameterList_1_0_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // &')'
  private static boolean ParameterList_1_0_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParameterList_1_0_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = consumeToken(b, RPAREN);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ','?
  private static boolean ParameterList_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParameterList_2")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // '(' ParameterList? ','? ')'
  public static boolean Parameters(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Parameters")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PARAMETERS, null);
    r = consumeToken(b, LPAREN);
    p = r; // pin = 1
    r = r && report_error_(b, Parameters_1(b, l + 1));
    r = p && report_error_(b, Parameters_2(b, l + 1)) && r;
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ParameterList?
  private static boolean Parameters_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Parameters_1")) return false;
    ParameterList(b, l + 1);
    return true;
  }

  // ','?
  private static boolean Parameters_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Parameters_2")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // AttributeKey AttributeArgs?
  public static boolean PlainAttribute(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PlainAttribute")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PLAIN_ATTRIBUTE, "<plain attribute>");
    r = AttributeKey(b, l + 1);
    r = r && PlainAttribute_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // AttributeArgs?
  private static boolean PlainAttribute_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PlainAttribute_1")) return false;
    AttributeArgs(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // PrimitiveType ';'
  public static boolean PrimitiveDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PrimitiveDeclaration")) return false;
    if (!nextTokenIs(b, PRIMITIVE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = PrimitiveType(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, PRIMITIVE_DECLARATION, r);
    return r;
  }

  /* ********************************************************** */
  // primitive identifier
  public static boolean PrimitiveType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PrimitiveType")) return false;
    if (!nextTokenIs(b, PRIMITIVE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PRIMITIVE_TYPE, null);
    r = consumeTokens(b, 1, PRIMITIVE, IDENTIFIER);
    p = r; // pin = 1
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // '.' identifier
  public static boolean QualifiedReferenceExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "QualifiedReferenceExpression")) return false;
    if (!nextTokenIs(b, DOT)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_, REFERENCE_EXPRESSION, null);
    r = consumeTokens(b, 0, DOT, IDENTIFIER);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '.' identifier
  public static boolean QualifiedTypeReferenceExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "QualifiedTypeReferenceExpression")) return false;
    if (!nextTokenIs(b, DOT)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_, TYPE_REFERENCE_EXPRESSION, null);
    r = consumeTokens(b, 0, DOT, IDENTIFIER);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '(' StringLiteral ')'
  public static boolean ReceiveStringId(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ReceiveStringId")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && StringLiteral(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, RECEIVE_STRING_ID, r);
    return r;
  }

  /* ********************************************************** */
  // identifier
  public static boolean ReferenceExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ReferenceExpression")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, REFERENCE_EXPRESSION, r);
    return r;
  }

  /* ********************************************************** */
  // '==' | '!=' | '<' | '<=' | '>' !'>' | '>='
  static boolean RelOp(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RelOp")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EQ);
    if (!r) r = consumeToken(b, NOT_EQ);
    if (!r) r = consumeToken(b, LESS);
    if (!r) r = consumeToken(b, LESS_OR_EQUAL);
    if (!r) r = RelOp_4(b, l + 1);
    if (!r) r = consumeToken(b, GREATER_OR_EQUAL);
    exit_section_(b, m, null, r);
    return r;
  }

  // '>' !'>'
  private static boolean RelOp_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RelOp_4")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, GREATER);
    r = r && RelOp_4_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !'>'
  private static boolean RelOp_4_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RelOp_4_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, GREATER);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // repeat Expression Block
  public static boolean RepeatStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RepeatStatement")) return false;
    if (!nextTokenIs(b, REPEAT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, REPEAT_STATEMENT, null);
    r = consumeToken(b, REPEAT);
    p = r; // pin = 1
    r = r && report_error_(b, Expression(b, l + 1, -1));
    r = p && Block(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // ':'? Type
  public static boolean Result(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Result")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, RESULT, "<result>");
    r = Result_0(b, l + 1);
    r = r && Type(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ':'?
  private static boolean Result_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Result_0")) return false;
    consumeToken(b, COLON);
    return true;
  }

  /* ********************************************************** */
  // return Expression? semi
  public static boolean ReturnStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ReturnStatement")) return false;
    if (!nextTokenIs(b, RETURN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, RETURN_STATEMENT, null);
    r = consumeToken(b, RETURN);
    p = r; // pin = 1
    r = r && report_error_(b, ReturnStatement_1(b, l + 1));
    r = p && semi(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Expression?
  private static boolean ReturnStatement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ReturnStatement_1")) return false;
    Expression(b, l + 1, -1);
    return true;
  }

  /* ********************************************************** */
  // CallExpr
  //   | QualifiedReferenceExpression
  //   | AssertNotNullExpr
  //   | DummyRightHandRule
  static boolean RightHandExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RightHandExpr")) return false;
    boolean r;
    r = CallExpr(b, l + 1);
    if (!r) r = QualifiedReferenceExpression(b, l + 1);
    if (!r) r = AssertNotNullExpr(b, l + 1);
    if (!r) r = DummyRightHandRule(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // RightHandExpr*
  static boolean RightHandExprs(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RightHandExprs")) return false;
    while (true) {
      int c = current_position_(b);
      if (!RightHandExpr(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "RightHandExprs", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // SetEntry (',' SetEntry)* ','?
  static boolean SetEntries(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SetEntries")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = SetEntry(b, l + 1);
    r = r && SetEntries_1(b, l + 1);
    r = r && SetEntries_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (',' SetEntry)*
  private static boolean SetEntries_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SetEntries_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!SetEntries_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "SetEntries_1", c)) break;
    }
    return true;
  }

  // ',' SetEntry
  private static boolean SetEntries_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SetEntries_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && SetEntry(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ','?
  private static boolean SetEntries_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SetEntries_2")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // Expression
  public static boolean SetEntry(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SetEntry")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SET_ENTRY, "<set entry>");
    r = Expression(b, l + 1, -1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // SetType '{' SetEntries? '}'
  public static boolean SetLiteral(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SetLiteral")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SET_LITERAL, "<set literal>");
    r = SetType(b, l + 1);
    r = r && consumeToken(b, LBRACE);
    r = r && SetLiteral_2(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // SetEntries?
  private static boolean SetLiteral_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SetLiteral_2")) return false;
    SetEntries(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'set' '<' Type '>'
  public static boolean SetType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SetType")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SET_TYPE, "<set type>");
    r = consumeToken(b, "set");
    p = r; // pin = 1
    r = r && report_error_(b, consumeToken(b, LESS));
    r = p && report_error_(b, Type(b, l + 1)) && r;
    r = p && consumeToken(b, GREATER) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // Parameters Result?
  public static boolean Signature(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Signature")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SIGNATURE, null);
    r = Parameters(b, l + 1);
    p = r; // pin = 1
    r = r && Signature_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Result?
  private static boolean Signature_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Signature_1")) return false;
    Result(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // VarDeclaration
  //   | DestructStatement
  //   | AssignStatement
  //   | ExpressionStatement
  public static boolean SimpleStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SimpleStatement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, SIMPLE_STATEMENT, "<simple statement>");
    r = VarDeclaration(b, l + 1);
    if (!r) r = DestructStatement(b, l + 1);
    if (!r) r = AssignStatement(b, l + 1);
    if (!r) r = ExpressionStatement(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // BlockNoPin
  //   | IfStatement
  //   | WhileStatement
  //   | RepeatStatement
  //   | UntilStatement
  //   | ForEachStatement
  //   | TryStatement
  //   | ReturnStatement
  //   | SimpleStatement
  public static boolean Statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, STATEMENT, "<statement>");
    r = BlockNoPin(b, l + 1);
    if (!r) r = IfStatement(b, l + 1);
    if (!r) r = WhileStatement(b, l + 1);
    if (!r) r = RepeatStatement(b, l + 1);
    if (!r) r = UntilStatement(b, l + 1);
    if (!r) r = ForEachStatement(b, l + 1);
    if (!r) r = TryStatement(b, l + 1);
    if (!r) r = ReturnStatement(b, l + 1);
    if (!r) r = SimpleStatement(b, l + 1);
    exit_section_(b, l, m, r, false, TactParser::StatementRecover);
    return r;
  }

  /* ********************************************************** */
  // !(
  //       '!'
  //     | '@'
  //     | '?'
  //     | '&'
  //     | '['
  //     | '('
  //     | '*'
  //     | '+'
  //     | '-'
  //     | ';'
  //     | '^'
  //     | '{'
  //     | '|'
  //     | '|='
  //     | '||'
  //     | '&&'
  //     | '}'
  //     | OPEN_QUOTE
  //     | let
  //     | try
  //     | while
  //     | until
  //     | do
  //     | repeat
  //     | foreach
  //     | const
  //     | else
  //     | for
  //     | fun
  //     | hex
  //     | identifier
  //     | if
  //     | int
  //     | oct
  //     | return
  //     | struct
  //     | true
  //     | false
  //     | null
  //     | mutates
  //     | extends
  //     | virtual
  //     | override
  //     | inline
  //     | abstract
  //     | 'initOf'
  //     | 'codeOf'
  // )
  static boolean StatementRecover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StatementRecover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !StatementRecover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '!'
  //     | '@'
  //     | '?'
  //     | '&'
  //     | '['
  //     | '('
  //     | '*'
  //     | '+'
  //     | '-'
  //     | ';'
  //     | '^'
  //     | '{'
  //     | '|'
  //     | '|='
  //     | '||'
  //     | '&&'
  //     | '}'
  //     | OPEN_QUOTE
  //     | let
  //     | try
  //     | while
  //     | until
  //     | do
  //     | repeat
  //     | foreach
  //     | const
  //     | else
  //     | for
  //     | fun
  //     | hex
  //     | identifier
  //     | if
  //     | int
  //     | oct
  //     | return
  //     | struct
  //     | true
  //     | false
  //     | null
  //     | mutates
  //     | extends
  //     | virtual
  //     | override
  //     | inline
  //     | abstract
  //     | 'initOf'
  //     | 'codeOf'
  private static boolean StatementRecover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StatementRecover_0")) return false;
    boolean r;
    r = consumeToken(b, NOT);
    if (!r) r = consumeToken(b, AT);
    if (!r) r = consumeToken(b, QUESTION);
    if (!r) r = consumeToken(b, BIT_AND);
    if (!r) r = consumeToken(b, LBRACK);
    if (!r) r = consumeToken(b, LPAREN);
    if (!r) r = consumeToken(b, MUL);
    if (!r) r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, MINUS);
    if (!r) r = consumeToken(b, SEMICOLON);
    if (!r) r = consumeToken(b, BIT_XOR);
    if (!r) r = consumeToken(b, LBRACE);
    if (!r) r = consumeToken(b, BIT_OR);
    if (!r) r = consumeToken(b, BIT_OR_ASSIGN);
    if (!r) r = consumeToken(b, COND_OR);
    if (!r) r = consumeToken(b, COND_AND);
    if (!r) r = consumeToken(b, RBRACE);
    if (!r) r = consumeToken(b, OPEN_QUOTE);
    if (!r) r = consumeToken(b, LET);
    if (!r) r = consumeToken(b, TRY);
    if (!r) r = consumeToken(b, WHILE);
    if (!r) r = consumeToken(b, UNTIL);
    if (!r) r = consumeToken(b, DO);
    if (!r) r = consumeToken(b, REPEAT);
    if (!r) r = consumeToken(b, FOREACH);
    if (!r) r = consumeToken(b, CONST);
    if (!r) r = consumeToken(b, ELSE);
    if (!r) r = consumeToken(b, FOR);
    if (!r) r = consumeToken(b, FUN);
    if (!r) r = consumeToken(b, HEX);
    if (!r) r = consumeToken(b, IDENTIFIER);
    if (!r) r = consumeToken(b, IF);
    if (!r) r = consumeToken(b, INT);
    if (!r) r = consumeToken(b, OCT);
    if (!r) r = consumeToken(b, RETURN);
    if (!r) r = consumeToken(b, STRUCT);
    if (!r) r = consumeToken(b, TRUE);
    if (!r) r = consumeToken(b, FALSE);
    if (!r) r = consumeToken(b, NULL);
    if (!r) r = consumeToken(b, MUTATES);
    if (!r) r = consumeToken(b, EXTENDS);
    if (!r) r = consumeToken(b, VIRTUAL);
    if (!r) r = consumeToken(b, OVERRIDE);
    if (!r) r = consumeToken(b, INLINE);
    if (!r) r = consumeToken(b, ABSTRACT);
    if (!r) r = consumeToken(b, INIT_OF);
    if (!r) r = consumeToken(b, CODE_OF);
    return r;
  }

  /* ********************************************************** */
  // Statement*
  static boolean Statements(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Statements")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Statement(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "Statements", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // OPEN_QUOTE (STRING_ENTRY | STRING_ESCAPE_ENTRY)* CLOSING_QUOTE
  public static boolean StringLiteral(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StringLiteral")) return false;
    if (!nextTokenIs(b, OPEN_QUOTE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OPEN_QUOTE);
    r = r && StringLiteral_1(b, l + 1);
    r = r && consumeToken(b, CLOSING_QUOTE);
    exit_section_(b, m, STRING_LITERAL, r);
    return r;
  }

  // (STRING_ENTRY | STRING_ESCAPE_ENTRY)*
  private static boolean StringLiteral_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StringLiteral_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!StringLiteral_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "StringLiteral_1", c)) break;
    }
    return true;
  }

  // STRING_ENTRY | STRING_ESCAPE_ENTRY
  private static boolean StringLiteral_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StringLiteral_1_0")) return false;
    boolean r;
    r = consumeToken(b, STRING_ENTRY);
    if (!r) r = consumeToken(b, STRING_ESCAPE_ENTRY);
    return r;
  }

  /* ********************************************************** */
  // Attributes? StructType
  public static boolean StructDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StructDeclaration")) return false;
    if (!nextTokenIs(b, "<struct declaration>", AT, STRUCT)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STRUCT_DECLARATION, "<struct declaration>");
    r = StructDeclaration_0(b, l + 1);
    r = r && StructType(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // Attributes?
  private static boolean StructDeclaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StructDeclaration_0")) return false;
    Attributes(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // struct identifier '{' FieldDeclaration* '}'
  public static boolean StructType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StructType")) return false;
    if (!nextTokenIs(b, STRUCT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, STRUCT_TYPE, null);
    r = consumeTokens(b, 1, STRUCT, IDENTIFIER, LBRACE);
    p = r; // pin = 1
    r = r && report_error_(b, StructType_3(b, l + 1));
    r = p && consumeToken(b, RBRACE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // FieldDeclaration*
  private static boolean StructType_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StructType_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!FieldDeclaration(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "StructType_3", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // as TypeReferenceExpression
  public static boolean Tlb(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Tlb")) return false;
    if (!nextTokenIs(b, AS)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, TLB, null);
    r = consumeToken(b, AS);
    p = r; // pin = 1
    r = r && TypeReferenceExpression(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // FunctionDeclaration
  //   | NativeFunctionDeclaration
  //   | AsmFunctionDeclaration
  //   | PrimitiveDeclaration
  //   | StructDeclaration
  //   | MessageDeclaration
  //   | TraitDeclaration
  //   | ContractDeclaration
  //   | ConstDeclaration
  //   | Statement
  static boolean TopDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TopDeclaration")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = FunctionDeclaration(b, l + 1);
    if (!r) r = NativeFunctionDeclaration(b, l + 1);
    if (!r) r = AsmFunctionDeclaration(b, l + 1);
    if (!r) r = PrimitiveDeclaration(b, l + 1);
    if (!r) r = StructDeclaration(b, l + 1);
    if (!r) r = MessageDeclaration(b, l + 1);
    if (!r) r = TraitDeclaration(b, l + 1);
    if (!r) r = ContractDeclaration(b, l + 1);
    if (!r) r = ConstDeclaration(b, l + 1);
    if (!r) r = Statement(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !<<eof>> TopDeclaration
  static boolean TopLevelDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TopLevelDeclaration")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = TopLevelDeclaration_0(b, l + 1);
    p = r; // pin = 1
    r = r && TopDeclaration(b, l + 1);
    exit_section_(b, l, m, r, p, TactParser::TopLevelDeclarationRecover);
    return r || p;
  }

  // !<<eof>>
  private static boolean TopLevelDeclaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TopLevelDeclaration_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !eof(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // !(
  //     inline |
  //     extends |
  //     mutates |
  //     message |
  //     primitive |
  //     contract |
  //     trait |
  //     import |
  //     try |
  //     '@' |
  //     '!' |
  //     '?' |
  //     '&' |
  //     '(' |
  //     '*' |
  //     '+' |
  //     '-' |
  //     ';' |
  //     '^' |
  //     '{' |
  //     '|' |
  //     '|=' |
  //     '||' |
  //     '&&' |
  //     '}' |
  //     const |
  //     else |
  //     fun |
  //     hex |
  //     identifier |
  //     if |
  //     int |
  //     oct |
  //     return |
  //     struct |
  //     asm |
  //     true |
  //     false |
  //     null
  // )
  static boolean TopLevelDeclarationRecover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TopLevelDeclarationRecover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !TopLevelDeclarationRecover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // inline |
  //     extends |
  //     mutates |
  //     message |
  //     primitive |
  //     contract |
  //     trait |
  //     import |
  //     try |
  //     '@' |
  //     '!' |
  //     '?' |
  //     '&' |
  //     '(' |
  //     '*' |
  //     '+' |
  //     '-' |
  //     ';' |
  //     '^' |
  //     '{' |
  //     '|' |
  //     '|=' |
  //     '||' |
  //     '&&' |
  //     '}' |
  //     const |
  //     else |
  //     fun |
  //     hex |
  //     identifier |
  //     if |
  //     int |
  //     oct |
  //     return |
  //     struct |
  //     asm |
  //     true |
  //     false |
  //     null
  private static boolean TopLevelDeclarationRecover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TopLevelDeclarationRecover_0")) return false;
    boolean r;
    r = consumeToken(b, INLINE);
    if (!r) r = consumeToken(b, EXTENDS);
    if (!r) r = consumeToken(b, MUTATES);
    if (!r) r = consumeToken(b, MESSAGE);
    if (!r) r = consumeToken(b, PRIMITIVE);
    if (!r) r = consumeToken(b, CONTRACT);
    if (!r) r = consumeToken(b, TRAIT);
    if (!r) r = consumeToken(b, IMPORT);
    if (!r) r = consumeToken(b, TRY);
    if (!r) r = consumeToken(b, AT);
    if (!r) r = consumeToken(b, NOT);
    if (!r) r = consumeToken(b, QUESTION);
    if (!r) r = consumeToken(b, BIT_AND);
    if (!r) r = consumeToken(b, LPAREN);
    if (!r) r = consumeToken(b, MUL);
    if (!r) r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, MINUS);
    if (!r) r = consumeToken(b, SEMICOLON);
    if (!r) r = consumeToken(b, BIT_XOR);
    if (!r) r = consumeToken(b, LBRACE);
    if (!r) r = consumeToken(b, BIT_OR);
    if (!r) r = consumeToken(b, BIT_OR_ASSIGN);
    if (!r) r = consumeToken(b, COND_OR);
    if (!r) r = consumeToken(b, COND_AND);
    if (!r) r = consumeToken(b, RBRACE);
    if (!r) r = consumeToken(b, CONST);
    if (!r) r = consumeToken(b, ELSE);
    if (!r) r = consumeToken(b, FUN);
    if (!r) r = consumeToken(b, HEX);
    if (!r) r = consumeToken(b, IDENTIFIER);
    if (!r) r = consumeToken(b, IF);
    if (!r) r = consumeToken(b, INT);
    if (!r) r = consumeToken(b, OCT);
    if (!r) r = consumeToken(b, RETURN);
    if (!r) r = consumeToken(b, STRUCT);
    if (!r) r = consumeToken(b, ASM);
    if (!r) r = consumeToken(b, TRUE);
    if (!r) r = consumeToken(b, FALSE);
    if (!r) r = consumeToken(b, NULL);
    return r;
  }

  /* ********************************************************** */
  // Attributes? TraitType
  public static boolean TraitDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TraitDeclaration")) return false;
    if (!nextTokenIs(b, "<trait declaration>", AT, TRAIT)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TRAIT_DECLARATION, "<trait declaration>");
    r = TraitDeclaration_0(b, l + 1);
    r = r && TraitType(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // Attributes?
  private static boolean TraitDeclaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TraitDeclaration_0")) return false;
    Attributes(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // trait identifier WithClause? '{' MemberItem* '}'
  public static boolean TraitType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TraitType")) return false;
    if (!nextTokenIs(b, TRAIT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, TRAIT_TYPE, null);
    r = consumeTokens(b, 1, TRAIT, IDENTIFIER);
    p = r; // pin = 1
    r = r && report_error_(b, TraitType_2(b, l + 1));
    r = p && report_error_(b, consumeToken(b, LBRACE)) && r;
    r = p && report_error_(b, TraitType_4(b, l + 1)) && r;
    r = p && consumeToken(b, RBRACE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // WithClause?
  private static boolean TraitType_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TraitType_2")) return false;
    WithClause(b, l + 1);
    return true;
  }

  // MemberItem*
  private static boolean TraitType_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TraitType_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!MemberItem(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "TraitType_4", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // try Block CatchClause?
  public static boolean TryStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TryStatement")) return false;
    if (!nextTokenIs(b, TRY)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, TRY_STATEMENT, null);
    r = consumeToken(b, TRY);
    p = r; // pin = 1
    r = r && report_error_(b, Block(b, l + 1));
    r = p && TryStatement_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // CatchClause?
  private static boolean TryStatement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TryStatement_2")) return false;
    CatchClause(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // '(' TypeListNoPin? ')'
  public static boolean TupleType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TupleType")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && TupleType_1(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, TUPLE_TYPE, r);
    return r;
  }

  // TypeListNoPin?
  private static boolean TupleType_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TupleType_1")) return false;
    TypeListNoPin(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // (TypeLit | TypeName) TypeExtra*
  public static boolean Type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, TYPE, "<type>");
    r = Type_0(b, l + 1);
    r = r && Type_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // TypeLit | TypeName
  private static boolean Type_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Type_0")) return false;
    boolean r;
    r = TypeLit(b, l + 1);
    if (!r) r = TypeName(b, l + 1);
    return r;
  }

  // TypeExtra*
  private static boolean Type_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Type_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!TypeExtra(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "Type_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // '?' | Tlb
  public static boolean TypeExtra(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeExtra")) return false;
    if (!nextTokenIs(b, "<type extra>", AS, QUESTION)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TYPE_EXTRA, "<type extra>");
    r = consumeToken(b, QUESTION);
    if (!r) r = Tlb(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ':' Type
  public static boolean TypeHint(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeHint")) return false;
    if (!nextTokenIs(b, COLON)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && Type(b, l + 1);
    exit_section_(b, m, TYPE_HINT, r);
    return r;
  }

  /* ********************************************************** */
  // Type (',' Type)* ','?
  public static boolean TypeListNoPin(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeListNoPin")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TYPE_LIST_NO_PIN, "<type list no pin>");
    r = Type(b, l + 1);
    r = r && TypeListNoPin_1(b, l + 1);
    r = r && TypeListNoPin_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (',' Type)*
  private static boolean TypeListNoPin_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeListNoPin_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!TypeListNoPin_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "TypeListNoPin_1", c)) break;
    }
    return true;
  }

  // ',' Type
  private static boolean TypeListNoPin_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeListNoPin_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && Type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ','?
  private static boolean TypeListNoPin_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeListNoPin_2")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // MapType
  //   | TupleType
  //   | BouncedType
  static boolean TypeLit(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeLit")) return false;
    boolean r;
    r = MapType(b, l + 1);
    if (!r) r = TupleType(b, l + 1);
    if (!r) r = BouncedType(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // TypeReferenceExpression QualifiedTypeReferenceExpression*
  static boolean TypeName(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeName")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = TypeReferenceExpression(b, l + 1);
    r = r && TypeName_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // QualifiedTypeReferenceExpression*
  private static boolean TypeName_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeName_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!QualifiedTypeReferenceExpression(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "TypeName_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // identifier
  public static boolean TypeReferenceExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeReferenceExpression")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, TYPE_REFERENCE_EXPRESSION, r);
    return r;
  }

  /* ********************************************************** */
  // '+' | '-' | '!' | '^' | '~' | '*'
  static boolean UnaryOp(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "UnaryOp")) return false;
    boolean r;
    r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, MINUS);
    if (!r) r = consumeToken(b, NOT);
    if (!r) r = consumeToken(b, BIT_XOR);
    if (!r) r = consumeToken(b, BIT_NOT);
    if (!r) r = consumeToken(b, MUL);
    return r;
  }

  /* ********************************************************** */
  // do Block until Expression semi
  public static boolean UntilStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "UntilStatement")) return false;
    if (!nextTokenIs(b, DO)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, UNTIL_STATEMENT, null);
    r = consumeToken(b, DO);
    p = r; // pin = 1
    r = r && report_error_(b, Block(b, l + 1));
    r = p && report_error_(b, consumeToken(b, UNTIL)) && r;
    r = p && report_error_(b, Expression(b, l + 1, -1)) && r;
    r = p && semi(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // let VarDefinition TypeHint? '=' Expression semi
  public static boolean VarDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VarDeclaration")) return false;
    if (!nextTokenIs(b, LET)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, VAR_DECLARATION, null);
    r = consumeToken(b, LET);
    r = r && VarDefinition(b, l + 1);
    r = r && VarDeclaration_2(b, l + 1);
    r = r && consumeToken(b, ASSIGN);
    p = r; // pin = 4
    r = r && report_error_(b, Expression(b, l + 1, -1));
    r = p && semi(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // TypeHint?
  private static boolean VarDeclaration_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VarDeclaration_2")) return false;
    TypeHint(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // identifier
  public static boolean VarDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VarDefinition")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, VAR_DEFINITION, r);
    return r;
  }

  /* ********************************************************** */
  // while Expression Block
  public static boolean WhileStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "WhileStatement")) return false;
    if (!nextTokenIs(b, WHILE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, WHILE_STATEMENT, null);
    r = consumeToken(b, WHILE);
    p = r; // pin = 1
    r = r && report_error_(b, Expression(b, l + 1, -1));
    r = p && Block(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // with Type (',' Type)* ','?
  public static boolean WithClause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "WithClause")) return false;
    if (!nextTokenIs(b, WITH)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, WITH_CLAUSE, null);
    r = consumeToken(b, WITH);
    p = r; // pin = 1
    r = r && report_error_(b, Type(b, l + 1));
    r = p && report_error_(b, WithClause_2(b, l + 1)) && r;
    r = p && WithClause_3(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (',' Type)*
  private static boolean WithClause_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "WithClause_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!WithClause_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "WithClause_2", c)) break;
    }
    return true;
  }

  // ',' Type
  private static boolean WithClause_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "WithClause_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && Type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ','?
  private static boolean WithClause_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "WithClause_3")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // ';' | &'}'
  public static boolean semi(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "semi")) return false;
    if (!nextTokenIs(b, "<semi>", RBRACE, SEMICOLON)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SEMI, "<semi>");
    r = consumeToken(b, SEMICOLON);
    if (!r) r = semi_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // &'}'
  private static boolean semi_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "semi_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = consumeToken(b, RBRACE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // Expression root: Expression
  // Operator priority table:
  // 0: BINARY(OrExpr)
  // 1: BINARY(TernaryExpr)
  // 2: BINARY(AndExpr)
  // 3: BINARY(ConditionalExpr)
  // 4: BINARY(AddExpr)
  // 5: BINARY(MulExpr)
  // 6: PREFIX(UnaryExpr)
  // 7: ATOM(DotExpression) ATOM(Literal)
  // 8: PREFIX(ParenthesesExpr)
  public static boolean Expression(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "Expression")) return false;
    addVariant(b, "<expression>");
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<expression>");
    r = UnaryExpr(b, l + 1);
    if (!r) r = DotExpression(b, l + 1);
    if (!r) r = Literal(b, l + 1);
    if (!r) r = ParenthesesExpr(b, l + 1);
    p = r;
    r = r && Expression_0(b, l + 1, g);
    exit_section_(b, l, m, null, r, p, null);
    return r || p;
  }

  public static boolean Expression_0(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "Expression_0")) return false;
    boolean r = true;
    while (true) {
      Marker m = enter_section_(b, l, _LEFT_, null);
      if (g < 0 && consumeTokenSmart(b, COND_OR)) {
        r = Expression(b, l, 0);
        exit_section_(b, l, m, OR_EXPR, r, true, null);
      }
      else if (g < 1 && consumeTokenSmart(b, QUESTION)) {
        r = report_error_(b, Expression(b, l, 1));
        r = TernaryExpr_1(b, l + 1) && r;
        exit_section_(b, l, m, TERNARY_EXPR, r, true, null);
      }
      else if (g < 2 && consumeTokenSmart(b, COND_AND)) {
        r = Expression(b, l, 2);
        exit_section_(b, l, m, AND_EXPR, r, true, null);
      }
      else if (g < 3 && RelOp(b, l + 1)) {
        r = Expression(b, l, 3);
        exit_section_(b, l, m, CONDITIONAL_EXPR, r, true, null);
      }
      else if (g < 4 && AddOp(b, l + 1)) {
        r = Expression(b, l, 4);
        exit_section_(b, l, m, ADD_EXPR, r, true, null);
      }
      else if (g < 5 && MulOp(b, l + 1)) {
        r = Expression(b, l, 5);
        exit_section_(b, l, m, MUL_EXPR, r, true, null);
      }
      else {
        exit_section_(b, l, m, null, false, false, null);
        break;
      }
    }
    return r;
  }

  // ':' Expression
  private static boolean TernaryExpr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TernaryExpr_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && Expression(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  public static boolean UnaryExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "UnaryExpr")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = UnaryOp(b, l + 1);
    p = r;
    r = p && Expression(b, l, 6);
    exit_section_(b, l, m, UNARY_EXPR, r, p, null);
    return r || p;
  }

  // DotPrimaryExpr RightHandExprs
  public static boolean DotExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "DotExpression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, DOT_EXPRESSION, "<dot expression>");
    r = DotPrimaryExpr(b, l + 1);
    r = r && RightHandExprs(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // int
  //   | hex
  //   | oct
  //   | bin
  //   | true
  //   | false
  //   | StringLiteral
  //   | null
  public static boolean Literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Literal")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, LITERAL, "<literal>");
    r = consumeTokenSmart(b, INT);
    if (!r) r = consumeTokenSmart(b, HEX);
    if (!r) r = consumeTokenSmart(b, OCT);
    if (!r) r = consumeTokenSmart(b, BIN);
    if (!r) r = consumeTokenSmart(b, TRUE);
    if (!r) r = consumeTokenSmart(b, FALSE);
    if (!r) r = StringLiteral(b, l + 1);
    if (!r) r = consumeTokenSmart(b, NULL);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  public static boolean ParenthesesExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParenthesesExpr")) return false;
    if (!nextTokenIsSmart(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, LPAREN);
    p = r;
    r = p && Expression(b, l, -1);
    r = p && report_error_(b, consumeToken(b, RPAREN)) && r;
    exit_section_(b, l, m, PARENTHESES_EXPR, r, p, null);
    return r || p;
  }

}
