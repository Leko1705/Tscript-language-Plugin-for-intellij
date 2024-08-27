package com.tscript.ide.run.build;

import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompileTask;
import com.intellij.openapi.compiler.CompilerMessageCategory;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindowManager;
import com.tscript.lang.tscriptc.log.Logger;
import com.tscript.lang.tscriptc.tools.Compiler;
import com.tscript.lang.tscriptc.tools.CompilerProvider;
import com.tscript.lang.tscriptc.util.Diagnostics;
import com.tscript.ide.TscriptFileType;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildTscriptTask implements CompileTask {

    public static Map<String, String> compiledFiles = new HashMap<>();

    @Override
    public boolean execute(@NotNull CompileContext context) {
        context.addMessage(CompilerMessageCategory.INFORMATION, "Run tscriptc", null, -1, -1, null, List.of());
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(context.getProject());
        toolWindowManager.invokeLater(() -> {
            toolWindowManager.getToolWindow("Build").activate(null);
        });

        compiledFiles.clear();

        Project project = context.getProject();
        VirtualFile[] files = context.getCompileScope().getFiles(TscriptFileType.INSTANCE, false);

        for (VirtualFile file : files) {
            String outPath = project.getBasePath() + File.separator + "out" + File.separator + file.getName() + "c";

            String path = file.getPath();


            try (InputStream in = new FileInputStream(path)) {
                Compiler compiler = CompilerProvider.getDefaultTscriptCompiler();

                File outFile = new File(outPath);
                outFile.getParentFile().mkdirs(); // Create the parent directory
                outFile.createNewFile(); // Create the file

                OutputStream out = new FileOutputStream(outPath);
                compiler.run(in, out, new IDELogger(context));

                compiledFiles.put(path, outPath);
            }
            catch (IOException e) {
                deleteCompiled(outPath);
                throw new RuntimeException(e);
            }
            catch (ProcessCanceledException e) {
                deleteCompiled(outPath);
                return false;
            }

        }

        return true;
    }

    private void deleteCompiled(String path){
        try {
            Files.delete(Path.of(path));
        }
        catch (IOException ignored){}
    }

    private static class IDELogger implements Logger {

        private final CompileContext context;

        private IDELogger(CompileContext context) {
            this.context = context;
        }

        @Override
        public void error(Diagnostics.Error error) {
            context.addMessage(CompilerMessageCategory.ERROR, error.toString(), null, error.getLocation().line(), -1, null, List.of());
            throw new ProcessCanceledException();
        }

        @Override
        public void warning(Diagnostics.Warning warning) {
            // warnings already highlighted in IDE
        }
    }
}