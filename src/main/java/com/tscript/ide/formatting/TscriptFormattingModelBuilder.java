package com.tscript.ide.formatting;

import com.intellij.formatting.*;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.tscript.ide.TscriptLanguage;
import com.tscript.ide.psi.TestTypes;
import org.jetbrains.annotations.NotNull;

final class TscriptFormattingModelBuilder implements FormattingModelBuilder {

    private static SpacingBuilder createSpaceBuilder(CodeStyleSettings settings) {
        return new SpacingBuilder(settings, TscriptLanguage.INSTANCE)
                .around(TestTypes.ASSIGN)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(TestTypes.ADD)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(TestTypes.SUB)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(TestTypes.MUL)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(TestTypes.DIV)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(TestTypes.IDIV)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(TestTypes.MOD)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(TestTypes.POW)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(TestTypes.AND)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(TestTypes.OR)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(TestTypes.XOR)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(TestTypes.TYPEOF)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(TestTypes.EQUALS)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(TestTypes.NOT_EQUALS)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(TestTypes.SAL)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(TestTypes.SAR)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(TestTypes.SLR)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(TestTypes.GT)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(TestTypes.GEQ)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(TestTypes.LT)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(TestTypes.LEQ)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(TestTypes.ADD_ASSIGN)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(TestTypes.SUB_ASSIGN)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(TestTypes.MUL_ASSIGN)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(TestTypes.DIV_ASSIGN)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(TestTypes.IDIV_ASSIGN)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(TestTypes.MOD_ASSIGN)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(TestTypes.POW_ASSIGN)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(TestTypes.SLR_ASSIGN)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(TestTypes.SAR_ASSIGN)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(TestTypes.SLR_ASSIGN)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .after(TestTypes.NOT)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(TestTypes.DOT)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AROUND_UNARY_OPERATOR)

                .before(TestTypes.SEMI)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_BEFORE_SEMICOLON)
                .after(TestTypes.SEMI)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AFTER_SEMICOLON)

                .before(TestTypes.COMMA)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_BEFORE_COMMA)
                .after(TestTypes.COMMA)
                .spaceIf(settings.getCommonSettings(TscriptLanguage.INSTANCE.getID()).SPACE_AFTER_COMMA)

                .before(TestTypes.EXPR)
                .none();
    }

    @Override
    public @NotNull FormattingModel createModel(@NotNull FormattingContext formattingContext) {
        final CodeStyleSettings codeStyleSettings = formattingContext.getCodeStyleSettings();
        return FormattingModelProvider
                .createFormattingModelForPsiFile(
                        formattingContext.getContainingFile(),
                        new TscriptBlock(
                                formattingContext.getNode(),
                                Wrap.createWrap(WrapType.NONE, false),
                                Alignment.createAlignment(),
                                createSpaceBuilder(codeStyleSettings)),
                        codeStyleSettings);
    }

}
