package com.test.language.conversion;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.test.exec.tscript.tscriptc.log.Logger;
import com.test.exec.tscript.tscriptc.log.LoggerAdapter;
import com.test.exec.tscript.tscriptc.log.VoidLogger;
import com.test.exec.tscript.tscriptc.tools.Compiler;
import com.test.exec.tscript.tscriptc.tools.CompilerProvider;
import com.test.exec.tscript.tscriptc.tools.Language;
import com.test.exec.tscript.tscriptc.util.Diagnostics;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ToWebScriptAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }

        // Get the VirtualFile from the event
        VirtualFile virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if (virtualFile == null) {
            return;
        }

        // Get the document from the virtual file
        Document document = FileDocumentManager.getInstance().getDocument(virtualFile);
        if (document == null) {
            return;
        }

        // Call the method to override the content
        overrideFileContent(virtualFile.getPath(), document, project);
    }

    private void overrideFileContent(String path, Document document, Project project) {
        // New content to override the file with

        Compiler compiler = CompilerProvider.getCompiler(Language.TO_WEB_TSCRIPT_CONVERTER);

        ByteArrayOutputStream bout;
        try {
            bout = new ByteArrayOutputStream();
            compiler.run(new ByteArrayInputStream(document.getText().getBytes()), bout, new LoggerAdapter() {
                @Override
                public void error(Diagnostics.Error error) {
                    Messages.showErrorDialog("Can not convert script file " + path + ".\n\nReason:\n" + error, "Compile Error While Conversion");
                    throw new ProcessCanceledException();
                }
            });
        }
        catch (ProcessCanceledException e) {
            return;
        }

        String newContent = bout.toString();

        // Perform the content replacement inside a write command action
        WriteCommandAction.runWriteCommandAction(project, () -> {
            document.setText(newContent);
        });
    }

}
