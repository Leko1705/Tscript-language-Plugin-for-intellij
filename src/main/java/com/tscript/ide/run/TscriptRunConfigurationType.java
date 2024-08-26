package com.tscript.ide.run;

import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.NotNullLazyValue;
import com.tscript.ide.TscriptIcon;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class TscriptRunConfigurationType extends ConfigurationTypeBase {

    static final String ID = "TestRunConfiguration";

    public TscriptRunConfigurationType() {
        super(ID, "Tscript", "Tscript run configuration type",
                NotNullLazyValue.createValue(() -> TscriptIcon.FILE));
        addFactory(new TscriptRunConfigurationFactory(this));
    }

}
