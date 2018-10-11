package com.reason.lang.ocaml;

import com.reason.lang.BaseParsingTestCase;
import com.reason.lang.core.HMSignature;
import com.reason.lang.core.psi.PsiFunction;
import com.reason.lang.core.psi.PsiFunctionParameter;
import com.reason.lang.core.psi.PsiLet;
import com.reason.lang.core.psi.PsiSignature;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class SignatureParsingTest extends BaseParsingTestCase {
    public SignatureParsingTest() {
        super("", "ml", new OclParserDefinition());
    }

    public void testMandatoryVal() {
        PsiLet let = first(letExpressions(parseCode("let x:int = 1")));

        HMSignature signature = let.getHMSignature();
        assertEquals("int", signature.toString());
        assertTrue(signature.isMandatory(0));
    }

    public void testTrimming() {
        PsiLet let = first(letExpressions(parseCode("let statelessComponent:\n  string ->\n  componentSpec(\n    stateless,\n    stateless,\n    noRetainedProps,\n    noRetainedProps,\n    actionless,\n  );\n")));

        PsiSignature signature = let.getSignature();
        assertEquals("string -> componentSpec(stateless, stateless, noRetainedProps, noRetainedProps, actionless)", signature.asString());
    }

    public void testParsingRml() {
        PsiLet let = first(letExpressions(parseCode("let padding: v:length -> h:length -> rule")));

        HMSignature signature = let.getHMSignature();
        assertEquals(3, signature.getTypes().length);
        assertEquals("(~v:length, ~h:length) -> rule", signature.toString());
        assertTrue(signature.isMandatory(0));
        assertTrue(signature.isMandatory(1));
    }

    public void testOptionalFun() {
        PsiLet let = first(letExpressions(parseCode("let x: int -> string option -> string = fun a  -> fun b  -> c")));

        HMSignature signature = let.getHMSignature();
        assertEquals(3, signature.getTypes().length);
        assertEquals("(int, string option) -> string", signature.toString());
        assertTrue(signature.isMandatory(0));
        assertFalse(signature.isMandatory(1));
    }

    public void testOptionalFunParameters() {
        PsiLet let = first(letExpressions(parseCode("let x (a : int) (b : string option) (c : bool) (d : float) = 3")));

        PsiFunction function = (PsiFunction) let.getBinding().getFirstChild();
        List<PsiFunctionParameter> parameters = new ArrayList<>(function.getParameterList());

        assertTrue(parameters.get(0).getSignature().asHMSignature().isMandatory(0));
        assertFalse(parameters.get(1).getSignature().asHMSignature().isMandatory(0));
        assertTrue(parameters.get(2).getSignature().asHMSignature().isMandatory(0));
        assertTrue(parameters.get(3).getSignature().asHMSignature().isMandatory(0));
    }

}