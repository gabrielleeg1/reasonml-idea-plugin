package com.reason.lang.core.psi.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.tree.IElementType;
import com.reason.lang.core.type.ORCompositePsiElement;
import com.reason.lang.core.type.ORTypes;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

public class PsiDeconstruction extends ORCompositePsiElement<ORTypes> {

    protected PsiDeconstruction(@NotNull ORTypes types, @NotNull IElementType elementType) {
        super(types, elementType);
    }

    public @NotNull List<PsiElement> getDeconstructedElements() {
        List<PsiElement> result = new ArrayList<>();

        for (PsiElement child : getChildren()) {
            if (child instanceof PsiDeconstruction) {
                // nested deconstructions
                result.addAll(((PsiDeconstruction) child).getDeconstructedElements());
            } else {
                IElementType elementType = child.getNode().getElementType();
                if (elementType != myTypes.LPAREN
                        && elementType != myTypes.COMMA
                        && elementType != myTypes.RPAREN
                        && elementType != myTypes.UNDERSCORE
                        && !(child instanceof PsiWhiteSpace)) {
                    result.add(child);
                }
            }
        }

        return result;
    }
}
