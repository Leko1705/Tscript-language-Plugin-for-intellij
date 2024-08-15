package com.test.language.run;

import com.intellij.execution.*;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TestRunConfiguration extends RunConfigurationBase<TestRunConfigurationOptions> {

    protected TestRunConfiguration(Project project,
                                   ConfigurationFactory factory,
                                   String name) {
        super(project, factory, name);
    }

    @NotNull
    @Override
    protected TestRunConfigurationOptions getOptions() {
        return (TestRunConfigurationOptions) super.getOptions();
    }

    public String getScriptName() {
        return getOptions().getScriptName();
    }

    public void setScriptName(String scriptName) {
        getOptions().setScriptName(scriptName);
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new TestRunSettingsEditor();
    }


    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor,
                                    @NotNull ExecutionEnvironment environment) {

        return new JavaCommandLineState(environment) {
            @Override
            protected JavaParameters createJavaParameters() {
                JavaParameters params = new JavaParameters();
                // Set JDK
                params.setJdk(ProjectRootManager.getInstance(environment.getProject()).getProjectSdk());
                // Set Main Class after ensuring compilation
                params.setMainClass("com.test.exec.Main");
                return params;
            }
        };
    }

}
