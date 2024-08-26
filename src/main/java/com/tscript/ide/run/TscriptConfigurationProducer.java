package com.tscript.ide.run;

import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.actions.LazyRunConfigurationProducer;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class TscriptConfigurationProducer extends LazyRunConfigurationProducer<TscriptRunConfiguration> {

    @NotNull
    @Override
    public ConfigurationFactory getConfigurationFactory() {
        return new TscriptRunConfigurationFactory(new TscriptRunConfigurationType());
    }

    @Override
    protected boolean setupConfigurationFromContext(@NotNull TscriptRunConfiguration configuration, @NotNull ConfigurationContext context, @NotNull Ref<PsiElement> sourceElement) {
        return true;
    }

    @Override
    public boolean isConfigurationFromContext(@NotNull TscriptRunConfiguration configuration, @NotNull ConfigurationContext context) {
        return true;
    }
}
