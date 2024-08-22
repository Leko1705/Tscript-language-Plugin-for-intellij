package com.tscript.ide.run.debug.breakpoints;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.DocumentUtil;
import com.intellij.xdebugger.XDebuggerUtil;
import com.tscript.ide.TscriptFileType;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public class LineIterator {

    public static boolean canPutAtElement(@NotNull final VirtualFile file,
                                             final int line,
                                             @NotNull Project project,
                                             @NotNull BiFunction<? super PsiElement, ? super Document, Boolean> processor){

        PsiFile psiFile = PsiManager.getInstance(project).findFile(file);

        if (psiFile == null) {
            return false;
        }

        if (!TscriptFileType.INSTANCE.equals(psiFile.getFileType())) {
            return false;
        }

        Document document = FileDocumentManager.getInstance().getDocument(file);
        if (document == null) return false;

        Ref<Boolean> res = Ref.create(false);

        XDebuggerUtil.getInstance().iterateLine(project, document, line, element -> {

            if ((element instanceof PsiWhiteSpace)
                    || (PsiTreeUtil.getParentOfType(element, PsiComment.class, PsiImportStatementBase.class, PsiPackageStatement.class) != null)) {
                return true;
            }

            PsiElement parent = element;
            while (element != null) {
                final int offset = element.getTextOffset();
                if (!DocumentUtil.isValidOffset(offset, document) || document.getLineNumber(offset) != line) {
                    break;
                }
                parent = element;
                element = element.getParent();
            }

            if (processor.apply(parent, document)) {
                res.set(true);
                return false;
            }

            return true;
        });

        return res.get();
    }

}
