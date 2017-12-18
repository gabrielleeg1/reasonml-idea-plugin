package com.reason.lang;

import com.intellij.psi.tree.IElementType;
import com.reason.lang.core.stub.type.ModuleStubElementType;
import com.reason.lang.core.stub.type.PsiLetStubElementType;
import com.reason.lang.core.stub.type.PsiTypeElementType;

public interface RmlTypes {

    IElementType ANNOTATION_EXPRESSION = new RmlElementType("ANNOTATION_EXPRESSION");
    IElementType EXTERNAL_EXPRESSION = new RmlElementType("EXTERNAL_EXPRESSION");
    IElementType EXCEPTION_EXPRESSION = new RmlElementType("EXCEPTION_EXPRESSION");
    IElementType EXCEPTION_NAME = new RmlElementType("EXCEPTION_NAME");
    IElementType FILE_MODULE = new RmlTokenType("FILE_MODULE");
    IElementType INCLUDE_EXPRESSION = new RmlElementType("INCLUDE_EXPRESSION");
    IElementType LET_EXPRESSION = new PsiLetStubElementType("LET_EXPRESSION");
    IElementType MACRO_EXPRESSION = new RmlElementType("MACRO_EXPRESSION");
    IElementType MACRO_NAME = new RmlElementType("MACRO_NAME");
    IElementType MODULE_EXPRESSION = new ModuleStubElementType("MODULE_EXPRESSION");
    IElementType MODULE_NAME = new RmlElementType("MODULE_NAME");
    IElementType MODULE_PATH = new RmlElementType("MODULE_PATH");
    IElementType OPEN_EXPRESSION = new RmlElementType("OPEN_EXPRESSION");
    IElementType TYPE_EXPRESSION = new PsiTypeElementType("TYPE_EXPRESSION");
    IElementType VAL_EXPRESSION = new PsiLetStubElementType("VAL_EXPRESSION");

    IElementType LET_FUN_PARAMS = new RmlElementType("LET_FUN_PARAMS");
    IElementType LET_BINDING = new RmlElementType("LET_BINDING");
    IElementType TYPE_CONSTR_NAME = new RmlElementType("TYPE_CONSTR_NAME");
    IElementType PATTERN_MATCH_EXPR = new RmlElementType("PATTERN_MATCH_EXPR");
    IElementType OBJECT_EXPR = new RmlElementType("OBJECT_EXPR");
    IElementType SCOPED_EXPR = new RmlElementType("SCOPED_EXPR");
    IElementType VALUE_NAME = new RmlElementType("VALUE_NAME");
    IElementType PROPERTY_NAME = new RmlElementType("PROPERTY_NAME");
    IElementType FUNCTOR_PARAMS = new RmlElementType("FUNCTOR_PARAMS");

    IElementType AND = new RmlTokenType("AND");
    IElementType ANDAND = new RmlTokenType("ANDAND");
    IElementType ARROBASE = new RmlTokenType("ARROBASE");
    IElementType ARROW = new RmlTokenType("ARROW");
    IElementType ASSERT = new RmlTokenType("ASSERT");
    IElementType AS = new RmlTokenType("AS");
    IElementType BACKTICK = new RmlTokenType("BACKTICK");
    IElementType CARRET = new RmlTokenType("CARRET");
    IElementType TAG_AUTO_CLOSE = new RmlTokenType("TAG_AUTO_CLOSE");
    IElementType TAG_START = new RmlTokenType("TAG_START");
    IElementType TAG_CLOSE = new RmlTokenType("TAG_CLOSE");
    IElementType TAG_NAME = new RmlTokenType("TAG_NAME");
    IElementType TAG_PROPERTY = new RmlTokenType("TAG_PROPERTY");
    IElementType TAG_LT = new RmlTokenType("TAG_LT");
    IElementType TAG_GT = new RmlTokenType("TAG_GT");
    IElementType COLON = new RmlTokenType("COLON");
    IElementType COMMA = new RmlTokenType("COMMA");
    IElementType COMMENT = new RmlTokenType("COMMENT");
    IElementType DIFF = new RmlTokenType("DIFF");
    IElementType DOLLAR = new RmlTokenType("DOLLAR");
    IElementType DOT = new RmlTokenType("DOT");
    IElementType DOTDOTDOT = new RmlTokenType("DOTDOTDOT");
    IElementType DO = new RmlTokenType("DO");
    IElementType DONE = new RmlTokenType("DONE");
    IElementType ELSE = new RmlTokenType("ELSE");
    IElementType END = new RmlTokenType("END");
    IElementType NOT_EQ = new RmlTokenType("EQ");
    IElementType NOT_EQEQ = new RmlTokenType("EQEQ");
    IElementType EQ = new RmlTokenType("EQ");
    IElementType EQEQ = new RmlTokenType("EQEQ");
    IElementType EQEQEQ = new RmlTokenType("EQEQEQ");
    IElementType EXCEPTION = new RmlTokenType("EXCEPTION");
    IElementType EXCLAMATION_MARK = new RmlTokenType("EXCLAMATION_MARK");
    IElementType EXTERNAL = new RmlTokenType("EXTERNAL");
    IElementType FALSE = new RmlTokenType("FALSE");
    IElementType FLOAT = new RmlTokenType("FLOAT");
    IElementType CHAR = new RmlElementType("CHAR");
    IElementType FOR = new RmlElementType("FOR");
    IElementType FUN = new RmlTokenType("FUN");
    IElementType FUN_PARAMS = new RmlTokenType("FUN_PARAMS");
    IElementType FUN_BODY = new RmlTokenType("FUN_BODY");
    IElementType FUNCTION = new RmlTokenType("FUNCTION");
    IElementType TYPE_ARGUMENT = new RmlTokenType("TYPE_ARGUMENT");
    IElementType GT = new RmlTokenType("GT");
    IElementType IF = new RmlTokenType("IF");
    IElementType IN = new RmlElementType("IN");
    IElementType LAZY = new RmlElementType("LAZY");
    IElementType INCLUDE = new RmlTokenType("INCLUDE");
    IElementType INT = new RmlTokenType("INT");
    IElementType LARRAY = new RmlTokenType("LARRAY");
    IElementType LBRACE = new RmlTokenType("LBRACE");
    IElementType LBRACKET = new RmlTokenType("LBRACKET");
    IElementType LET = new RmlTokenType("LET");
    IElementType LIDENT = new RmlTokenType("LIDENT");
    IElementType LIST = new RmlTokenType("LIST");
    IElementType LPAREN = new RmlTokenType("LPAREN");
    IElementType LT = new RmlTokenType("LT");
    IElementType MATCH = new RmlTokenType("MATCH");
    IElementType MINUS = new RmlTokenType("MINUS");
    IElementType MINUSDOT = new RmlTokenType("MINUSDOT");
    IElementType MODULE = new RmlTokenType("MODULE");
    IElementType MUTABLE = new RmlTokenType("MUTABLE");
    IElementType NONE = new RmlTokenType("NONE");
    IElementType OBJECT = new RmlElementType("OBJECT");
    IElementType OF = new RmlElementType("OF");
    IElementType OPEN = new RmlTokenType("OPEN");
    IElementType OPTION = new RmlTokenType("OPTION");
    IElementType POLY_VARIANT = new RmlTokenType("POLY_VARIANT");
    IElementType PIPE = new RmlTokenType("PIPE");
    IElementType PIPE_FORWARD = new RmlTokenType("PIPE_FORWARD");
    IElementType PLUS = new RmlTokenType("PLUS");
    IElementType PERCENT = new RmlTokenType("PERCENT");
    IElementType PLUSDOT = new RmlTokenType("PLUSDOT");
    IElementType QUESTION_MARK = new RmlTokenType("QUESTION_MARK");
    IElementType QUOTE = new RmlTokenType("QUOTE");
    IElementType RAISE = new RmlElementType("RAISE");
    IElementType RARRAY = new RmlTokenType("RARRAY");
    IElementType RBRACE = new RmlTokenType("RBRACE");
    IElementType RBRACKET = new RmlTokenType("RBRACKET");
    IElementType REC = new RmlTokenType("REC");
    IElementType REF = new RmlTokenType("REF");
    IElementType RPAREN = new RmlTokenType("RPAREN");
    IElementType SEMI = new RmlTokenType("SEMI");
    IElementType SIG = new RmlTokenType("SIG");
    IElementType SIMPLE_ARROW = new RmlTokenType("SIMPLE_ARROW");
    IElementType SHARP = new RmlTokenType("SHARP");
    IElementType SHORTCUT = new RmlTokenType("SHORTCUT");
    IElementType SLASH = new RmlTokenType("SLASH");
    IElementType SLASHDOT = new RmlTokenType("SLASHDOT");
    IElementType SOME = new RmlTokenType("SOME");
    IElementType STAR = new RmlTokenType("STAR");
    IElementType STARDOT = new RmlTokenType("STARDOT");
    IElementType STRING = new RmlTokenType("STRING");
    IElementType STRUCT = new RmlTokenType("STRUCT");
    IElementType SWITCH = new RmlTokenType("SWITCH");
    IElementType TILDE = new RmlElementType("TILDE");
    IElementType TO = new RmlElementType("TO");
    IElementType THEN = new RmlTokenType("TRUE");
    IElementType TRUE = new RmlTokenType("TRUE");
    IElementType TRY = new RmlTokenType("TRY");
    IElementType TYPE = new RmlTokenType("TYPE");
    IElementType UIDENT = new RmlTokenType("UIDENT");
    IElementType UNIT = new RmlTokenType("UNIT");
    IElementType VAL = new RmlTokenType("VAL");
    IElementType WITH = new RmlTokenType("WITH");
    IElementType WHEN = new RmlTokenType("WHEN");
}
