package com.tscript.ide.run;

import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.actions.RunConfigurationProducer;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class TscriptRunConfigProducer extends RunConfigurationProducer<TscriptRunConfiguration> {

    protected TscriptRunConfigProducer() {
        super(new TscriptRunConfigurationType().getConfigurationFactories()[0]);
    }

    @Override
    protected boolean setupConfigurationFromContext(@NotNull TscriptRunConfiguration configuration, @NotNull ConfigurationContext context, @NotNull Ref<PsiElement> sourceElement) {
        if (context.getPsiLocation() == null) return false;
        PsiFile file = context.getPsiLocation().getContainingFile();
        if (file == null) return false;

        configuration.setName("Run " + file.getName());
        // Set any other configuration parameters needed here
        return true;
    }

    @Override
    public boolean isConfigurationFromContext(@NotNull TscriptRunConfiguration configuration, @NotNull ConfigurationContext context) {
        if (context.getPsiLocation() == null) return false;
        PsiFile file = context.getPsiLocation().getContainingFile();
        return file != null && configuration.getName().equals("Run " + file.getName());
    }
}
