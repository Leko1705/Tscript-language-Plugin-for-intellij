package com.tscript.ide.formatting;

import com.intellij.codeInsight.editorActions.BackspaceHandlerDelegate;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class TscriptBackspaceHandler extends BackspaceHandlerDelegate {


    @Override
    public void beforeCharDeleted(char c, @NotNull PsiFile file, @NotNull Editor editor) {

    }

    @Override
    public boolean charDeleted(char c, @NotNull PsiFile file, @NotNull Editor editor) {
        if (TscriptTypeHandler.surroundMap.containsKey(c)){
            char nextChar = TscriptTypeHandler.getCharAfterCaret(editor);

            if (TscriptTypeHandler.surroundMap.get(c) == nextChar){
                CaretModel caretModel = editor.getCaretModel();
                int offset = caretModel.getOffset();
                Document document = editor.getDocument();

                if (offset < document.getTextLength()) {
                    // Run the document modification in a write action to ensure thread safety
                    com.intellij.openapi.application.ApplicationManager.getApplication().runWriteAction(() -> {
                        document.deleteString(offset, offset + 1);
                    });
                }

                return true;
            }
        }



        return false;
    }

}

