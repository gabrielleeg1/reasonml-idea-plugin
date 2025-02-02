package com.reason.lang.core.psi.impl;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.*;
import com.intellij.psi.tree.*;
import com.reason.lang.core.*;
import com.reason.lang.core.psi.*;
import com.reason.lang.core.psi.reference.*;
import com.reason.lang.core.type.*;
import org.jetbrains.annotations.*;

public class PsiLowerSymbol extends LeafPsiElement implements PsiORAtom {
    protected final ORTypes myTypes;

    // region Constructors
    public PsiLowerSymbol(@NotNull ORTypes types, @NotNull IElementType tokenType, CharSequence text) {
        super(tokenType, text);
        myTypes = types;
    }
    // endregion

    @Override
    public PsiReference getReference() {
        return new PsiLowerSymbolReference(this, myTypes);
    }

    @Override public String toString() {
        return "PsiLowerSymbol:" + getElementType();
    }
}
