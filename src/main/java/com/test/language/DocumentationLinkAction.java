package com.test.language;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;

public class DocumentationLinkAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        try {
            BrowserUtil.browse(new URL("https://tglas.github.io/tscript/?doc="));
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

}
