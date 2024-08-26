package com.tscript.ide;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeManager;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class TscriptModuleType extends ModuleType<TscriptModuleBuilder> {


    private static final String ID = "TSCRIPT_MODULE_TYPE";

    TscriptModuleType() {
        super(ID);
    }

    public static TscriptModuleType getInstance() {
        return (TscriptModuleType) ModuleTypeManager.getInstance().findByID(ID);
    }

    @NotNull
    @Override
    public TscriptModuleBuilder createModuleBuilder() {
        return new TscriptModuleBuilder();
    }

    @NotNull
    @Override
    public String getName() {
        return "Tscript";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Create a <b>Tscript</b> project";
    }

    @NotNull
    @Override
    public Icon getNodeIcon(@Deprecated boolean b) {
        return TscriptIcon.FILE;
    }

    @Override
    public ModuleWizardStep @NotNull [] createWizardSteps(@NotNull WizardContext wizardContext,
                                                          @NotNull TscriptModuleBuilder moduleBuilder,
                                                          @NotNull ModulesProvider modulesProvider) {
        return super.createWizardSteps(wizardContext, moduleBuilder, modulesProvider);
    }

}
