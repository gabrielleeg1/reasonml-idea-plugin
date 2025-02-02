package com.reason.lang.reason;

import com.intellij.psi.util.*;
import com.reason.ide.files.*;
import com.reason.lang.core.*;
import com.reason.lang.core.psi.*;
import com.reason.lang.core.psi.impl.*;

import java.util.*;

@SuppressWarnings("ConstantConditions")
public class FunctionCallParsingTest extends RmlParsingTestCase {
    public void test_call() {
        PsiLetBinding e = first(letExpressions(parseCode("let _ = string_of_int(1)"))).getBinding();

        PsiFunctionCall call = PsiTreeUtil.findChildOfType(e, PsiFunctionCall.class);
        assertNotNull(ORUtil.findImmediateFirstChildOfClass(call, PsiLowerSymbol.class));
        assertNull(PsiTreeUtil.findChildOfType(e, PsiParameterDeclaration.class));
        assertEquals("string_of_int(1)", call.getText());
        assertEquals(1, call.getParameters().size());
    }

    public void test_call2() {
        PsiLet e = first(letExpressions(parseCode("let _ = Belt.Option.map(self.state.timerId^, Js.Global.clearInterval)")));

        PsiFunctionCall fnCall = PsiTreeUtil.findChildOfType(e.getBinding(), PsiFunctionCall.class);
        List<PsiParameterReference> parameters = fnCall.getParameters();
        assertEquals(2, parameters.size());
        assertEquals("self.state.timerId^", parameters.get(0).getText());
        assertEquals("Js.Global.clearInterval", parameters.get(1).getText());
    }

    public void test_call3() {
        PsiLet e = first(letExpressions(parseCode("let _ = subscriber->Topic.unsubscribe()")));

        PsiFunctionCall fnCall = PsiTreeUtil.findChildOfType(e.getBinding(), PsiFunctionCall.class);
        assertEmpty(fnCall.getParameters());
    }

    public void test_end_comma() {
        PsiLet e = first(letExpressions(parseCode("let _ = style([ color(red), ])")));

        assertEquals("style([ color(red), ])", e.getBinding().getText());
        PsiFunctionCall f = PsiTreeUtil.findChildOfType(e.getBinding(), PsiFunctionCall.class);
        assertNull(PsiTreeUtil.findChildOfType(f, PsiDeconstruction.class));
        assertSize(1, f.getParameters());
    }

    public void test_unit_last() {
        PsiLetBinding e = first(letExpressions(parseCode("let _ = f(1, ());"))).getBinding();

        PsiFunctionCall fnCall = PsiTreeUtil.findChildOfType(e, PsiFunctionCall.class);
        assertSize(2, fnCall.getParameters());
    }

    public void test_params() {
        FileBase f = parseCode("call(~decode=x => Ok(), ~task=() => y,);");
        PsiFunctionCall fnCall = ORUtil.findImmediateFirstChildOfClass(f, PsiFunctionCall.class);

        assertSize(2, fnCall.getParameters());
    }

    public void test_param_name() {
        List<PsiLet> expressions = letAllExpressions(parseCode("describe(\"context\", () => { test(\"should do something\", () => { let inner = 1; }) })"));
        PsiLet e = first(expressions);

        assertEquals("Dummy.describe[1].test[1].inner", e.getQualifiedName());
    }

    public void test_nested_parenthesis() {
        PsiFunctionCall f = firstOfType(parseCode("set(x->keep(((y, z)) => y), xx);"), PsiFunctionCall.class);

        assertEquals("set(x->keep(((y, z)) => y), xx)", f.getText());
        assertEquals("x->keep(((y, z)) => y)", f.getParameters().get(0).getText());
        assertEquals("xx", f.getParameters().get(1).getText());
    }

    public void test_body() {
        PsiLet e = first(letExpressions(parseCode("let _ = x => { M.{k: v} };")));

        PsiFunctionBody body = PsiTreeUtil.findChildOfType(e, PsiFunctionBody.class);
        assertEquals("{ M.{k: v} }", body.getText());
    }

    public void test_in_functor() {
        //                                    0        |         |          |         |         |        |         |         |          |
        PsiFunctor e = firstOfType(parseCode("module Make = (M: Intf) : Result => { let fn = target => (. store) => call(input, item => item); };"), PsiFunctor.class);

        PsiFunctionCall fc = PsiTreeUtil.findChildOfType(e, PsiFunctionCall.class);
        assertEquals("call(input, item => item)", fc.getText());
    }

    // https://github.com/giraud/reasonml-idea-plugin/issues/120
    public void test_GH_120() {
        PsiLet e = first(letExpressions(parseCode("let _ = f(x == U.I, 1)")));

        PsiFunctionCall fnCall = PsiTreeUtil.findChildOfType(e, PsiFunctionCall.class);
        assertSize(2, fnCall.getParameters());
    }
}
