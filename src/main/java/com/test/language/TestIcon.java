package com.test.language;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class TestIcon extends AnAction {

    public static final Icon FILE = IconLoader.getIcon("icons/test_icon.png", TestIcon.class);

    public TestIcon(){
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

    }

}
