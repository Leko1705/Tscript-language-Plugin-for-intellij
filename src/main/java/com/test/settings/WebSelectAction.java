package com.test.settings;

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ex.CheckboxAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.test.language.TestFileType;
import org.jetbrains.annotations.NotNull;

public class WebSelectAction extends CheckboxAction {

    public static final String KEY = "com.test.settings.TargetWebVersion";

    public WebSelectAction(){
        super("Target web tscript");
    }

    @Override
    public boolean isSelected(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return false;
        }
        PropertiesComponent properties = PropertiesComponent.getInstance(project);
        return properties.getBoolean(KEY, false);
    }

    @Override
    public void setSelected(@NotNull AnActionEvent e, boolean state) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }
        PropertiesComponent properties = PropertiesComponent.getInstance(project);
        properties.setValue(KEY, state);

        VirtualFile virtualFile = e.getDataContext().getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE);
        if (virtualFile == null) {
            return;
        }


        PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
        if (psiFile == null || psiFile.getFileType() != TestFileType.INSTANCE) {
            return;
        }

        // Trigger re-analysis for the specific file
        DaemonCodeAnalyzer.getInstance(project).restart(psiFile);
        Settings.TARGET_WEB_VERSION = state;
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return super.getActionUpdateThread();
    }

}
