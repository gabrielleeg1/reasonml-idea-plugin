package com.reason.lang;

import com.intellij.core.*;
import com.intellij.lang.*;
import com.intellij.psi.tree.*;
import org.jetbrains.annotations.*;

public abstract class CommonPsiParser implements PsiParser {
    protected final boolean myIsSafe;

    protected CommonPsiParser(boolean isSafe) {
        myIsSafe = isSafe;
    }

    @Override
    public @NotNull ASTNode parse(@NotNull IElementType elementType, @NotNull PsiBuilder builder) {
        //builder.setDebugMode(true);
        PsiBuilder.Marker r = builder.mark();

        ORParser<?> state = getORParser(builder);
        state.parse();

        // if we have a scope at last position in a file, without SEMI, we need to handle it here
        if (!state.empty()) {
            state.clear();
        }

        state.eof();

        // end stream
        if (!builder.eof()) {
            builder.mark().error(JavaPsiBundle.message("unexpected.token"));
            while (!builder.eof()) {
                builder.advanceLexer();
            }
        }

        r.done(elementType);

        return builder.getTreeBuilt();
    }

    protected abstract ORParser<?> getORParser(@NotNull PsiBuilder builder);
}
