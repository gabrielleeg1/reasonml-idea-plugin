package com.reason.ide.search;

import com.intellij.lang.HelpID;
import com.intellij.lang.cacheBuilder.WordOccurrence;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lexer.LexerBase;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.ElementDescriptionUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.usageView.UsageViewLongNameLocation;
import com.intellij.usageView.UsageViewNodeTextLocation;
import com.intellij.usageView.UsageViewTypeLocation;
import com.reason.lang.LexerAdapter;
import com.reason.lang.core.psi.PsiTypeName;
import com.reason.lang.core.psi.PsiUpperSymbol;
import com.reason.lang.core.psi.PsiVarName;
import com.reason.lang.reason.RmlTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RmlFindUsagesProvider implements com.intellij.lang.findUsages.FindUsagesProvider {
    @Nullable
    @Override
    public WordsScanner getWordsScanner() {
        //return new DefaultWordsScanner(new RmlLexerAdapter(), TokenSet.create(RmlTypes.UIDENT, RmlTypes.VALUE_NAME), TokenSet.EMPTY, TokenSet.EMPTY);
        return (fileText, processor) -> {
            RmlTypes types = RmlTypes.INSTANCE;
            LexerBase lexer = new LexerAdapter(types);
            lexer.start(fileText);
            IElementType tokenType;
            while ((tokenType = lexer.getTokenType()) != null) {
                //TODO process occurrences in string literals and comments
                if (tokenType == types.LIDENT || tokenType == types.UIDENT || tokenType == types.UPPER_SYMBOL || tokenType == types.VALUE_NAME) {
                    int tokenStart = lexer.getTokenStart();
                    for (TextRange wordRange : StringUtil.getWordIndicesIn(lexer.getTokenText())) {
                        int start = tokenStart + wordRange.getStartOffset();
                        int end = tokenStart + wordRange.getEndOffset();
//                        System.out.println("scan: " + start + "," + end + " -> " + lexer.getTokenText());
                        processor.process(new WordOccurrence(fileText, start, end, WordOccurrence.Kind.CODE));
                    }
                }
                lexer.advance();
            }
        };
    }

    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement element) {
        return element instanceof PsiUpperSymbol || element instanceof PsiTypeName || element instanceof PsiVarName;
    }

    @Nullable
    @Override
    public String getHelpId(@NotNull PsiElement psiElement) {
        return HelpID.FIND_OTHER_USAGES;
    }

    @NotNull
    @Override
    public String getType(@NotNull PsiElement element) {
        return ElementDescriptionUtil.getElementDescription(element, UsageViewTypeLocation.INSTANCE);
    }

    @NotNull
    @Override
    public String getDescriptiveName(@NotNull PsiElement element) {
        return ElementDescriptionUtil.getElementDescription(element, UsageViewLongNameLocation.INSTANCE);
    }

    @NotNull
    @Override
    public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
        return ElementDescriptionUtil.getElementDescription(element, UsageViewNodeTextLocation.INSTANCE);
    }
}
