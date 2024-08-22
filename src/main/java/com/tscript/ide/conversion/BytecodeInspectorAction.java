package com.tscript.ide.conversion;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.tscript.lang.tscriptc.log.LoggerAdapter;
import com.tscript.lang.tscriptc.tools.Compiler;
import com.tscript.lang.tscriptc.tools.CompilerProvider;
import com.tscript.lang.tscriptc.util.Diagnostics;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BytecodeInspectorAction extends AnAction {

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

        String newFilePath = virtualFile.getPath() + "i";
        try(FileOutputStream fout = new FileOutputStream(newFilePath)) {
            Compiler compiler = CompilerProvider.getDefaultTscriptCompiler();
            compiler.dis(new ByteArrayInputStream(document.getText().getBytes()), fout, new LoggerAdapter() {
                @Override
                public void error(Diagnostics.Error error) {
                    Messages.showErrorDialog(error.toString(), "Compile-Time Error");
                    throw new ProcessCanceledException();
                }
            });

            VirtualFile newFile = LocalFileSystem.getInstance().refreshAndFindFileByPath(newFilePath);
            if (newFile != null) {
                newFile.refresh(false, false);
                FileEditorManager.getInstance(project).openFile(newFile, true);
            }
        }
        catch (IOException ex){
            Messages.showErrorDialog(project, "File creation failed: " + ex.getMessage(), "Error");
        }
        catch (ProcessCanceledException ignored){
        }

     }

}
