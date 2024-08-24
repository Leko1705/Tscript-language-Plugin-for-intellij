package com.tscript.ide.run;

import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.NotNullLazyValue;

public class TscriptRunConfigurationType extends ConfigurationTypeBase {

    static final String ID = "TestRunConfiguration";

    public TscriptRunConfigurationType() {
        super(ID, "Tscript", "Tscript run configuration type",
                NotNullLazyValue.createValue(() -> AllIcons.Nodes.Console));
        addFactory(new TscriptRunConfigurationFactory(this));
    }

}
