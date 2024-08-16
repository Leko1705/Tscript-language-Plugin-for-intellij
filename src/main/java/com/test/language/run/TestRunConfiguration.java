package com.test.language.run;

import com.intellij.execution.*;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
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

        return new RunProfileState() {
            @Override
            public @Nullable ExecutionResult execute(Executor executor, @NotNull ProgramRunner<?> runner) {
                AnAction action = ActionManager.getInstance().getAction("com.example.MyCustomAction");
                AnActionEvent event = AnActionEvent.createFromAnAction(action, null, "MyCustomPlace", DataManager.getInstance().getDataContext());
                action.actionPerformed(event);
                return null;
            }
        };

    }

}
