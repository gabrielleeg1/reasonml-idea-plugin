package com.reason.lang.rescript;

import com.intellij.psi.*;
import com.intellij.psi.util.*;
import com.reason.lang.core.*;
import com.reason.lang.core.psi.*;
import com.reason.lang.core.psi.impl.*;
import jpsplugin.com.reason.*;

import java.util.*;
import java.util.stream.*;

@SuppressWarnings("ConstantConditions")
public class VariantCallParsingTest extends ResParsingTestCase {
    public void test_basic() {
        PsiLetBinding binding = firstOfType(parseCode("let x = Var"), PsiLet.class).getBinding();

        assertEquals("Var", binding.getText());
        assertNull(PsiTreeUtil.findChildOfType(binding, PsiVariantDeclaration.class));
        assertNull(PsiTreeUtil.findChildOfType(binding, PsiUpperIdentifier.class));
    }

    public void test_withPath() {
        PsiLetBinding binding = firstOfType(parseCode("let x = A.Variant(1)"), PsiLet.class).getBinding();

        assertEquals("A.Variant(1)", binding.getText());
        assertNull(PsiTreeUtil.findChildOfType(binding, PsiVariantDeclaration.class));
        assertNull(PsiTreeUtil.findChildOfType(binding, PsiUpperIdentifier.class));
    }

    public void test_withParam() {
        PsiLetBinding binding = firstOfType(parseCode("let x = Var(1)"), PsiLet.class).getBinding();

        assertEquals("Var(1)", binding.getText());
        assertNull(ORUtil.findImmediateFirstChildOfClass(binding, PsiVariantDeclaration.class));
    }

    public void test_patternMatch() {
        PsiSwitch e = firstOfType(parseCode("switch action { | UpdateDescription(desc) => ReasonReact.SideEffects.(_self => onDescriptionChange(desc)) }"), PsiSwitch.class);

        PsiPatternMatchBody body = PsiTreeUtil.findChildOfType(e, PsiPatternMatchBody.class);
        assertEquals("ReasonReact.SideEffects.(_self => onDescriptionChange(desc))", body.getText());
        List<PsiUpperSymbol> uppers = ORUtil.findImmediateChildrenOfClass(body, PsiUpperSymbol.class);
        assertEquals("ReasonReact, SideEffects", Joiner.join(", ", uppers.stream().map(PsiElement::getText).collect(Collectors.toList())));
    }

    public void test_inMethod() {
        PsiFunction e = firstOfType(parseCode("(. fileName, data) => self.send(SetErrorMessage(fileName, data[\"message\"]))"), PsiFunction.class);

        PsiUpperSymbol upper = PsiTreeUtil.findChildOfType(e, PsiUpperSymbol.class);
        assertEquals("SetErrorMessage", upper.getText());
    }
}
