package com.tscript.ide.highlight;

import com.intellij.execution.lineMarker.ExecutorAction;
import com.intellij.execution.lineMarker.RunLineMarkerContributor;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.util.containers.ContainerUtil;
import com.tscript.ide.psi.TestFunctionDef;
import com.tscript.ide.psi.TscriptASTUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MainFunctionGutterIconProvider extends RunLineMarkerContributor {

    @Nullable
    @Override
    public Info getInfo(@NotNull PsiElement element) {

        boolean isMain = element instanceof TestFunctionDef o
                && o.getName() != null
                && TscriptASTUtils.inGlobalScope(o.getParent())
                && o.getName().equals("__main__");

        if (!isMain) return null;

        final AnAction[] actions = ExecutorAction.getActions(Integer.MAX_VALUE);
        return new Info(
                AllIcons.RunConfigurations.TestState.Run,
                actions,
                e -> StringUtil.join(ContainerUtil.mapNotNull(actions, action -> getText(action, e)), "\n")
        );
    }
}
