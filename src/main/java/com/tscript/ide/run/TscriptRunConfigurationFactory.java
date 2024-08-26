package com.tscript.ide.run;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.components.BaseState;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class TscriptRunConfigurationFactory extends ConfigurationFactory {

    protected TscriptRunConfigurationFactory(ConfigurationType type) {
        super(type);
    }

    @Override
    public @NotNull String getId() {
        return TscriptRunConfigurationType.ID;
    }

    @NotNull
    @Override
    public RunConfiguration createTemplateConfiguration(
            @NotNull Project project) {
        return new TscriptRunConfiguration(project, this, "Tscript");
    }

    @Nullable
    @Override
    public Class<? extends BaseState> getOptionsClass() {
        return TscriptRunConfigurationOptions.class;
    }

}
