package com.reason.ide.reference;

import com.intellij.psi.*;
import com.reason.ide.*;
import com.reason.lang.core.psi.PsiParameter;
import com.reason.lang.core.psi.*;
import com.reason.lang.core.psi.PsiType;

public class ResolveLowerElementRESTest extends ORBasePlatformTestCase {
    @Override
    protected String getTestDataPath() {
        return "testData/com/reason/ide/reference";
    }

    public void test_let_in_module_binding() {
        configureCode("A.res", "let foo = 2\n module X = { let foo = 1\n let z = foo<caret> }");

        PsiLet e = (PsiLet) myFixture.getElementAtCaret();
        assertEquals("A.X.foo", e.getQualifiedName());
    }

    public void test_let_inner_scope() {
        configureCode("A.res", "let x = 1\n let a = { let x = 2\n x<caret> + 10 }");

        PsiLet e = (PsiLet) myFixture.getElementAtCaret();
        assertEquals("A.a.x", e.getQualifiedName());
    }

    public void test_inner_scope_in_function() {
        configureCode("A.res", "let x = 1\n let fn = { let x = 2\n fn1(x<caret>)\n }");

        PsiLet e = (PsiLet) myFixture.getElementAtCaret();
        assertEquals("A.fn.x", e.getQualifiedName());
    }

    public void test_inner_scope_in_impl() {
        configureCode("A.rei", "let x:int");
        configureCode("A.res", "let x = 1\n let fn = { let foo = 2\n fn1(foo<caret>) }");

        PsiLet e = (PsiLet) myFixture.getElementAtCaret();
        assertEquals("A.fn.foo", e.getQualifiedName());
        assertEquals("A.res", e.getContainingFile().getName());
    }

    public void test_let_local_module_alias() {
        configureCode("A.rei", "let x:int");
        configureCode("B.res", "let x = 1\n module X = A\n X.x<caret>");

        PsiLet e = (PsiLet) myFixture.getElementAtCaret();
        assertEquals("A.x", e.getQualifiedName());
    }

    public void test_alias_path() {
        configureCode("A.res", "module W = { module X = { module Y = { module Z = { let z = 1 } } } }");
        configureCode("B.res", "module C = A.W.X\n module D = C.Y.Z\n D.z<caret>");

        PsiLet e = (PsiLet) myFixture.getElementAtCaret();
        assertEquals("A.W.X.Y.Z.z", e.getQualifiedName());
    }

    public void test_alias_x() {
        configureCode("A.res", "module Mode = { type t }");
        configureCode("B.res", "module B1 = { module Mode = A.Mode }");
        configureCode("C.res", "B.B1.Mode.t<caret>");        // B.B1.Mode.t -> A.Mode.t

        PsiType e = (PsiType) myFixture.getElementAtCaret();
        assertEquals("A.Mode.t", e.getQualifiedName());
    }

    public void test_open() {
        configureCode("B.res", "let x = 1");
        configureCode("A.res", "let x = 2\n open B\n x<caret>");

        PsiLet e = (PsiLet) myFixture.getElementAtCaret();
        assertEquals("B.x", e.getQualifiedName());
    }

    public void test_alias_open() {
        configureCode("B.res", "let x = 1");
        configureCode("A.res", "let x = 2\n module C = B\n open C\n x<caret>");

        PsiLet e = (PsiLet) myFixture.getElementAtCaret();
        assertEquals("B.x", e.getQualifiedName());
    }

    public void test_let_local_open_parens() {
        configureCode("A.res", "module A1 = { let a = 1 }");
        configureCode("B.res", "let a = 2; let b = A.(A1.a<caret>);");

        PsiLet e = (PsiLet) myFixture.getElementAtCaret();
        assertEquals("A.A1.a", e.getQualifiedName());
    }

    public void test_let_local_open_parens_2() {
        configureCode("A.res", "module A1 = { let a = 3 }");
        configureCode("B.res", "let a = A.A1.(a<caret>)");

        PsiLet e = (PsiLet) myFixture.getElementAtCaret();
        assertEquals("A.A1.a", e.getQualifiedName());
    }

    public void test_type() {
        configureCode("A.res", "type t\n type t' = t<caret>");

        PsiType e = (PsiType) myFixture.getElementAtCaret();
        assertEquals("A.t", e.getQualifiedName());
    }

    public void test_type_with_path() {
        configureCode("A.res", "type t");
        configureCode("B.res", "type t = A.t<caret>");

        PsiType e = (PsiType) myFixture.getElementAtCaret();
        assertEquals("A.t", e.getQualifiedName());
    }

    public void test_type_with_path_2() {
        configureCode("A.res", "type t\n type y = X.Y.t<caret>");

        assertThrows(AssertionError.class, "element not found in file A.res", () -> {
            PsiElement e = myFixture.getElementAtCaret();
        });
    }

    public void test_function() {
        configureCode("A.res", "module B = { let bb = 1; }\n module C = { let cc = x => x }\n let z = C.cc(B.bb<caret>)");

        PsiLet e = (PsiLet) myFixture.getElementAtCaret();
        assertEquals("A.B.bb", e.getQualifiedName());
    }

    public void test_function_open() {
        configureCode("B.res", "module C = { let make = x => x\n let convert = x => x }");
        configureCode("A.res", "open B\n C.make([| C.convert<caret> |])");

        PsiLet e = (PsiLet) myFixture.getElementAtCaret();
        assertEquals("B.C.convert", e.getQualifiedName());
    }

    public void test_param_parenLess() {
        configureCode("A.res", "let add10 = x => x<caret> + 10");

        PsiParameter e = (PsiParameter) myFixture.getElementAtCaret();
        assertEquals("A.add10[x]", e.getQualifiedName());
    }

    public void test_local_open_parens() {
        configureCode("A.res", "module A1 = { external a : int = \"\" }");
        configureCode("B.res", "let b = A.(A1.a<caret>)");

        PsiExternal e = (PsiExternal) myFixture.getElementAtCaret();
        assertEquals("A.A1.a", e.getQualifiedName());
    }

    public void test_local_open_parens_2() {
        configureCode("A.res", "module A1 = { external a : int = \"\" }");
        configureCode("B.res", "let a = A.A1.(a<caret>)");

        PsiExternal e = (PsiExternal) myFixture.getElementAtCaret();
        assertEquals("A.A1.a", e.getQualifiedName());
    }

    public void test_local_open_parens_3() {
        configureCode("A.res", "module A1 = { type t = | Variant\n let toString = x => x }");
        configureCode("B.res", "A.A1.(Variant->toString<caret>);");

        PsiLet e = (PsiLet) myFixture.getElementAtCaret();
        assertEquals("A.A1.toString", e.getQualifiedName());
    }

    public void test_include() {
        configureCode("A.res", "module B = { type t; }\n module C = B\n include C\n type x = t<caret>");

        PsiType e = (PsiType) myFixture.getElementAtCaret();
        assertEquals("A.B.t", e.getQualifiedName());
    }

    public void test_include_2() {
        configureCode("Css_AtomicTypes.resi", "module Visibility: { type t = [ #visible | #hidden | #collapse ] }");
        configureCode("Css_Legacy_Core.res", "module Types = Css_AtomicTypes");
        configureCode("Css.res", "include Css_Legacy_Core");
        configureCode("A.res", "type layoutRule\n let visibility: [< Css.Types.Length.t | Css.Types.Visibility.t<caret> ] => layoutRule");

        PsiType e = (PsiType) myFixture.getElementAtCaret();
        assertEquals("Css_AtomicTypes.Visibility.t", e.getQualifiedName());
    }

    public void test_include_qualified() {
        configureCode("A.res", "module B = { module C = { type t } }\n module D = B\n include D.C");
        configureCode("C.res", "type t = A.t<caret>");

        PsiType e = (PsiType) myFixture.getElementAtCaret();
        assertEquals("A.B.C.t", e.getQualifiedName());
    }

    public void test_module_signature() {
        configureCode("A.res", "module B: { type t\n let toString: t => string }\n module C: { type t\n let toString: t<caret> => string }");

        PsiType e = (PsiType) myFixture.getElementAtCaret();
        assertEquals("A.C.t", e.getQualifiedName());
    }

    public void test_let_Local_open_pipe_first() {
        configureCode("A.res", "module A1 = { let add = x => x + 3 }");
        configureCode("B.res", "let x = A.A1.(x->add<caret>)");

        PsiLet e = (PsiLet) myFixture.getElementAtCaret();
        assertEquals("A.A1.add", e.getQualifiedName());
    }

    public void test_external_local_open_pipe_first() {
        configureCode("A.res", "module A1 = { external add : int => int = \"\" }");
        configureCode("B.res", "let x = A.A1.(x->add<caret>)");

        PsiExternal e = (PsiExternal) myFixture.getElementAtCaret();
        assertEquals("A.A1.add", e.getQualifiedName());
    }

    public void test_pipe_first() {
        configureCode("Css.mli", "val px: int => string");
        configureCode("A.res", "Dimensions.spacing.small->Css.px<caret>");

        PsiVal e = (PsiVal) myFixture.getElementAtCaret();
        assertEquals("Css.px", e.getQualifiedName());
    }

    public void test_pipe_first_open() {
        configureCode("Css.mli", "val px: int => string");
        configureCode("A.res", "let make = () => { open Css; Dimensions.spacing.small->px<caret> }");

        PsiVal e = (PsiVal) myFixture.getElementAtCaret();
        assertEquals("Css.px", e.getQualifiedName());
    }

    public void test_pipe_first_open_2() {
        configureCode("Core.res", "module Async = { let get = x => x }");
        configureCode("A.res", "open Core.Async\n request->get<caret>(\"windows/settings\")");

        PsiLet e = (PsiLet) myFixture.getElementAtCaret();
        assertEquals("Core.Async.get", e.getQualifiedName());
    }

    public void test_pipe_first_open_with_path() {
        configureCode("Css.mli", "module Rule = { val px: int => string }");
        configureCode("A.res", "let make = () => { open Css\n Dimensions.spacing.small->Rule.px<caret> }");

        PsiVal e = (PsiVal) myFixture.getElementAtCaret();
        assertEquals("Css.Rule.px", e.getQualifiedName());
    }

    public void test_multiple_module() {
        configureCode("Command.res", "module Settings = { module Action = { let convert = x => x } }");
        configureCode("A.res", "module C = Y\n open Command\n Settings.Action.convert<caret>");

        PsiLet e = (PsiLet) myFixture.getElementAtCaret();
        assertEquals("Command.Settings.Action.convert", e.getQualifiedName());
    }

    public void test_variant_constructor() {
        configureCode("B.res", "let convert = x => x");
        configureCode("A.res", "X.Variant(B.convert<caret>())");

        PsiLet e = (PsiLet) myFixture.getElementAtCaret();
        assertEquals("B.convert", e.getQualifiedName());
    }

    public void test_variant_constructor_tuple() {
        configureCode("B.res", "type t('a) = | Variant('a, 'b)");
        configureCode("A.res", "let x = 1\n B.Variant(X.Y, x<caret>)");

        PsiLet e = (PsiLet) myFixture.getElementAtCaret();
        assertEquals("A.x", e.getQualifiedName());
    }

    public void test_open_include() {
        configureCode("Css_Core.res", "let fontStyle = x => x");
        configureCode("Css.res", "include Css_Core");
        configureCode("A.res", "open Css\n fontStyle<caret>");

        PsiLet e = (PsiLet) myFixture.getElementAtCaret();
        assertEquals("Css_Core.fontStyle", e.getQualifiedName());
    }

    public void test_open_include_deep() {
        configureCode("Css_Rule.res", "let fontStyle = x => x");
        configureCode("Css_Core.res", "module Rules = { include Css_Rule }");
        configureCode("Css.res", "include Css_Core");
        configureCode("A.res", "open Css.Rules\n fontStyle<caret>");

        PsiLet e = (PsiLet) myFixture.getElementAtCaret();
        assertEquals("Css_Rule.fontStyle", e.getQualifiedName());
    }

    public void test_resolution_1() {
        configureCode("Belt_MapString.mli", "val get: 'v t -> key -> 'v option");
        configureCode("Belt_Map.ml", "module String = Belt_MapString");
        configureCode("Belt_Option.mli", "val flatMap : 'a option -> ('a -> 'b option) -> 'b option");
        configureCode("Belt.res", "module Option = Belt_Option\n module Map = Belt_Map");
        configureCode("A.res", "let x = (dict, locale) => locale->Belt.Option.flatMap<caret>(dict->Belt.Map.String.get)");

        PsiVal e = (PsiVal) myFixture.getElementAtCaret();
        assertEquals("Belt_Option.flatMap", e.getQualifiedName());
    }

    public void test_resolution_2() {
        configureCode("Belt_MapString.mli", "val get: 'v t -> key -> 'v option");
        configureCode("Belt_Map.ml", "module String = Belt_MapString");
        configureCode("Belt_Option.mli", "val flatMap : 'a option -> ('a -> 'b option) -> 'b option");
        configureCode("Belt.res", "module Option = Belt_Option\n module Map = Belt_Map;");
        configureCode("A.res", "let x = (dict, locale) => locale->Belt.Option.flatMap(dict->Belt.Map.String.get<caret>)");

        PsiVal e = (PsiVal) myFixture.getElementAtCaret();
        assertEquals("Belt_MapString.get", e.getQualifiedName());
    }

    /* zzz functor
    public void test_functor_body() {
        configureCode("A.res", "module Make = (M:I) => { let a = 3; };");
        configureCode("B.res", "module Instance = A.Make({}); let b = Instance.a<caret>;");

        PsiLet e = (PsiLet) myFixture.getElementAtCaret();
        assertEquals("A.Make.a", e.getQualifiedName());
    }

    public void test_file_include_functor() {
        configureCode("A.res", "module Make = (M:I) => { let a = 3; }; include Make({})");
        configureCode("B.res", "let b = A.a<caret>;");

        PsiLet e = (PsiLet) myFixture.getElementAtCaret();
        assertEquals("A.Make.a", e.getQualifiedName());
    }

    public void test_functor_result_with_alias() {
        configureCode("A.res", "module type Result = { let a: int; };");
        configureCode("B.res", "module T = A; module Make = (M:Intf): T.Result => { let b = 3; };");
        configureCode("C.res", "module Instance = Make({}); let c = Instance.a<caret>;");

        PsiLet e = (PsiLet) myFixture.getElementAtCaret();
        assertEquals("A.Result.a", e.getQualifiedName());
    }
    */

    public void test_path_functor() {
        configureCode("pervasives.mli", "external compare : 'a -> 'a -> int = \"%compare\"");
        configureCode("A.res", "module B = X.Functor({ let cmp = Pervasives.compare<caret>; })");

        PsiExternal e = (PsiExternal) myFixture.getElementAtCaret();
        assertEquals("Pervasives.compare", e.getQualifiedName());
    }

    //region record
    public void test_record() {
        configureCode("B.res", "let b = { a: 1, b: 2 }\n b<caret>");

        PsiLet e = (PsiLet) myFixture.getElementAtCaret();
        assertEquals("B.b", e.getQualifiedName());
    }

    public void test_record_l1() {
        configureCode("B.res", "let b = { a: 1, b: 2 }\n b.b<caret>");

        PsiRecordField e = (PsiRecordField) myFixture.getElementAtCaret();
        assertEquals("B.b.b", e.getQualifiedName());
    }

    public void test_record_l3() {
        configureCode("A.res", "let a = { b: { c: { d: 1 } } }\n a.b.c.d<caret>");

        PsiRecordField e = (PsiRecordField) myFixture.getElementAtCaret();
        assertEquals("A.a.b.c.d", e.getQualifiedName());
    }
    //endregion

    public void test_GH_167_deconstruction() {
        configureCode("A.res", "let (count, setCount) = React.useState(() => 0)\n setCount<caret>(1)");

        PsiElement elementAtCaret = myFixture.getElementAtCaret();
        assertEquals(12, elementAtCaret.getTextOffset());
    }

    // TODO later
    //public void test_GH_303() {
    //    configureCode("B.res", "type t1 = {bar: string}");
    //    configureCode("A.res", "type t = {bar: string}\n let bar = item => item.bar<caret>");
    //
    //    PsiElement e = myFixture.getElementAtCaret();
    //    assertEquals("A.t.bar", e.getQualifiedName());
    //}

    public void test_GH_303_2() {
        configureCode("B.res", "type t1 = {bar:string}");
        configureCode("A.res", "type t = {bar: string}\n let bar<caret> = item => item.bar");

        PsiLet e = (PsiLet) myFixture.getElementAtCaret();
        assertEquals("A.bar", e.getQualifiedName());
    }
}
