package com.reason.ide.docs;

import com.intellij.psi.*;
import com.reason.ide.files.*;
import com.reason.lang.core.psi.*;
import com.reason.lang.odoc.*;
import org.jetbrains.annotations.*;

import static com.reason.lang.odoc.ODocMarkup.*;

class DocFormatter {
    public static final String NAME_START = "<b>";
    public static final String NAME_END = "</b>";

    @NotNull
    static String format(@NotNull PsiFile file, PsiElement element, @NotNull String text) {
        if (file instanceof FileBase) {
            ODocLexer lexer = new ODocLexer();
            return formatDefinition(file, element)
                    + CONTENT_START
                    + (new ODocConverter(lexer).convert(text + "\n"))
                    + CONTENT_END;
        }
        return text;
    }

    @NotNull
    private static String formatDefinition(@NotNull PsiFile file, PsiElement element) {
        StringBuilder sb = new StringBuilder();

        sb.append(HEADER_START).append(((FileBase) file).getModuleName());
        if (element instanceof PsiVal) {
            sb.append("<br/>").append(element.getText());
        }
        sb.append(HEADER_END);

        return sb.toString();
    }

    @NotNull
    static String escapeCodeForHtml(@Nullable PsiElement code) {
        if (code == null) {
            return "";
        }

        return escapeCodeForHtml(code.getText());
    }

    @Nullable
    public static String escapeCodeForHtml(@Nullable String code) {
        return code == null ? null : code.
                replaceAll("<", "&lt;").
                replaceAll(">", "&gt;");
    }
}
