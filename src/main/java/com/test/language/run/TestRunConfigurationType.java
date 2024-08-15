package com.test.language.run;

import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.NotNullLazyValue;

public class TestRunConfigurationType extends ConfigurationTypeBase {

    static final String ID = "TestRunConfiguration";

    TestRunConfigurationType() {
        super(ID, "Test", "Test run configuration type",
                NotNullLazyValue.createValue(() -> AllIcons.Nodes.Console));
        addFactory(new TestRunConfigurationFactory(this));
    }

}
