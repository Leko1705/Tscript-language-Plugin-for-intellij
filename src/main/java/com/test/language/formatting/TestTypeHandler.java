package com.test.language.formatting;

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class TestTypeHandler extends TypedHandlerDelegate {

    public static final Map<Character, Character> surroundMap =  Map.of(
            '{', '}',
            '(', ')',
            '[', ']',
            '"', '"',
            '\'', '\''
    );

    @NotNull
    @Override
    public Result beforeCharTyped(char c, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file, @NotNull FileType fileType) {
        if (surroundMap.containsValue(c) && surroundMap.containsValue(getCharAfterCaret(editor)) && c == getCharAfterCaret(editor)){
            editor.getCaretModel().moveCaretRelatively(1, 0, false, false, true);
            return Result.STOP;
        }
        return Result.CONTINUE;
    }

    @Override
    public @NotNull Result charTyped(char c, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {

        if (surroundMap.containsKey(c) && !Character.isLetterOrDigit(getCharAfterCaret(editor))) {
            EditorModificationUtil.insertStringAtCaret(editor, surroundMap.get(c).toString());
            editor.getCaretModel().moveCaretRelatively(-1, 0, false, false, true);
            return Result.STOP;
        }
        return Result.CONTINUE;
    }


    public static char getCharAfterCaret(Editor editor) {
        CaretModel caretModel = editor.getCaretModel();
        int offset = caretModel.getOffset();
        Document document = editor.getDocument();
        if (offset < document.getTextLength()) {
            return document.getCharsSequence().charAt(offset);
        }
        return '\0';
    }

    public static char getCharBeforeCaret(Editor editor) {
        CaretModel caretModel = editor.getCaretModel();
        int offset = caretModel.getOffset();
        if (offset > 0) {
            Document document = editor.getDocument();
            return document.getCharsSequence().charAt(offset - 1);
        }
        return '\0';
    }

}
