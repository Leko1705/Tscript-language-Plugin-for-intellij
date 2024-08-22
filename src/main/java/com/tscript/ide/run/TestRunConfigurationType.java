package com.tscript.ide.run;

import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.NotNullLazyValue;

public class TestRunConfigurationType extends ConfigurationTypeBase {

    static final String ID = "TestRunConfiguration";

    public TestRunConfigurationType() {
        super(ID, "Test", "Test run configuration type",
                NotNullLazyValue.createValue(() -> AllIcons.Nodes.Console));
        addFactory(new TestRunConfigurationFactory(this));
    }

}
