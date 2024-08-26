package com.tscript.ide;

import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TscriptModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleType<?> getModuleType() {
        return TscriptModuleType.getInstance();
    }

    @Override
    public void setupRootModel(@NotNull ModifiableRootModel modifiableRootModel) throws ConfigurationException {
// Get the content root path (where the module is created)
        String contentEntryPath = getContentEntryPath();
        if (contentEntryPath == null) {
            throw new ConfigurationException("Content entry path is not specified");
        }

        // Find the virtual file for the content root
        VirtualFile contentRoot = LocalFileSystem.getInstance().refreshAndFindFileByPath(contentEntryPath);
        if (contentRoot == null) {
            throw new ConfigurationException("Unable to locate the content root: " + contentEntryPath);
        }

        // Add content entry to the modifiable root model
        ContentEntry contentEntry = modifiableRootModel.addContentEntry(contentRoot);

        // Create a source directory (src)
        String sourcePath = contentEntryPath + File.separator + "src";
        createDirectory(sourcePath);

        // Find the virtual file for the source path
        VirtualFile sourceRoot = LocalFileSystem.getInstance().refreshAndFindFileByPath(sourcePath);
        if (sourceRoot != null) {
            contentEntry.addSourceFolder(sourceRoot, false); // false indicates it's a regular source folder
            createInitialTscriptFile(sourceRoot);
        }
    }

    private void createDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new RuntimeException("Failed to create directory: " + path);
            }
        }
    }

    private void createInitialTscriptFile(VirtualFile directory){
        try {
            // Define the initial file name and content
            String fileName = "main.tscript"; // or Main.java, Main.py, etc.
            String fileContent = """
                        
                        # prints a text to the console
                        function say(text){
                            print(text);
                        }
                        
                        say("hello world!");
                        """;

            // Create the file in the src directory
            VirtualFile initialFile = directory.createChildData(this, fileName);

            // Write the initial content to the file
            VfsUtil.saveText(initialFile, fileContent);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create initial code file: " + e.getMessage(), e);
        }
    }


}
