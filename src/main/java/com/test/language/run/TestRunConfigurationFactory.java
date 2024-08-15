package com.test.language.run;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.components.BaseState;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TestRunConfigurationFactory extends ConfigurationFactory {

    protected TestRunConfigurationFactory(ConfigurationType type) {
        super(type);
    }

    @Override
    public @NotNull String getId() {
        return TestRunConfigurationType.ID;
    }

    @NotNull
    @Override
    public RunConfiguration createTemplateConfiguration(
            @NotNull Project project) {
        return new TestRunConfiguration(project, this, "Test");
    }

    @Nullable
    @Override
    public Class<? extends BaseState> getOptionsClass() {
        return TestRunConfigurationOptions.class;
    }

}
