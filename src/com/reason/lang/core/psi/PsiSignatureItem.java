package com.reason.lang.core.psi;

import com.intellij.psi.*;
import com.reason.lang.core.psi.impl.*;
import org.jetbrains.annotations.*;

public interface PsiSignatureItem extends PsiElement, PsiLanguageConverter {
    boolean isNamedItem();

    @Nullable String getName();

    boolean isOptional();

    @Nullable PsiElement getSignature();

    @Nullable PsiElement getDefaultValue();
}
