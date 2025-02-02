package com.reason.ide.highlight;

import com.intellij.lang.annotation.*;
import com.intellij.openapi.editor.colors.*;
import com.intellij.openapi.editor.markup.*;
import com.intellij.openapi.util.*;
import com.intellij.psi.*;
import com.intellij.psi.tree.*;
import com.reason.lang.core.psi.impl.*;
import com.reason.lang.core.type.*;
import org.jetbrains.annotations.*;

import static com.intellij.lang.annotation.HighlightSeverity.*;
import static com.reason.ide.highlight.ORSyntaxHighlighter.*;

public abstract class ORSyntaxAnnotator implements Annotator {
    private final ORTypes myTypes;

    ORSyntaxAnnotator(@NotNull ORTypes types) {
        myTypes = types;
    }

    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        IElementType elementType = element.getNode().getElementType();

        if (elementType == myTypes.C_TAG_START) {
            PsiElement nameIdentifier = ((PsiTagStart) element).getNameIdentifier();
            if (nameIdentifier != null) {
                TextRange range = TextRange.create(element.getTextRange().getStartOffset(), nameIdentifier.getTextRange().getEndOffset());
                enforceColor(holder, range, MARKUP_TAG_);
                enforceColor(holder, element.getLastChild(), MARKUP_TAG_);
            }
        } else if (elementType == myTypes.C_TAG_CLOSE) {
            enforceColor(holder, element, MARKUP_TAG_);
        } else if (elementType == myTypes.PROPERTY_NAME) {
            enforceColor(holder, element, MARKUP_ATTRIBUTE_);
        } else if (elementType == myTypes.C_MACRO_NAME) {
            enforceColor(holder, element, ANNOTATION_);
        } else if (elementType == myTypes.C_INTERPOLATION_PART) {
            enforceColor(holder, element, STRING_);
        } else if (element instanceof PsiInterpolationReference) {
            enforceColor(holder, element, INTERPOLATED_REF_);
        }
        // remapped tokens are not seen by syntaxAnnotator
        else if (elementType == myTypes.A_VARIANT_NAME) {
            color(holder, element, VARIANT_NAME_);
        } else if (elementType == myTypes.A_MODULE_NAME) {
            color(holder, element, MODULE_NAME_);
        }
    }

    private void enforceColor(@NotNull AnnotationHolder holder, @NotNull TextRange range, @NotNull TextAttributesKey key) {
        holder.newSilentAnnotation(INFORMATION).range(range).enforcedTextAttributes(TextAttributes.ERASE_MARKER).create();
        holder.newSilentAnnotation(INFORMATION).range(range).textAttributes(key).create();
    }

    private void enforceColor(@NotNull AnnotationHolder holder, @Nullable PsiElement element, @NotNull TextAttributesKey key) {
        if (element != null) {
            holder.newSilentAnnotation(INFORMATION).range(element).enforcedTextAttributes(TextAttributes.ERASE_MARKER).create();
            holder.newSilentAnnotation(INFORMATION).range(element).textAttributes(key).create();
        }
    }

    private void color(@NotNull AnnotationHolder holder, @Nullable PsiElement element, @NotNull TextAttributesKey key) {
        if (element != null) {
            holder.newSilentAnnotation(INFORMATION).range(element).textAttributes(key).create();
        }
    }
}
