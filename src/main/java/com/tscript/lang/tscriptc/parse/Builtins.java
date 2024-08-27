package com.tscript.lang.tscriptc.parse;

import com.tscript.lang.tscriptc.log.Logger;
import com.tscript.lang.tscriptc.tree.DefinitionTree;
import com.tscript.lang.tscriptc.tree.NamespaceTree;
import com.tscript.lang.tscriptc.tree.Trees;
import com.tscript.lang.tscriptc.util.Diagnostics;
import com.tscript.lang.tscriptc.util.Location;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class Builtins {
    private Builtins(){}

    private static final List<DefinitionTree> natives = new ArrayList<>();
    private static final List<NamespaceTree> nspaces = new ArrayList<>();

    public static List<DefinitionTree> getNatives(){
        return natives;
    }

    public static List<NamespaceTree> getNspaces(){
        return nspaces;
    }



    static {
        init();
    }

    private static void loadNative(String name){
        natives.add(new Trees.BasicNativeFunctionTree(Location.emptyLocation(), name, Set.of()));
    }

    private static class NSpaceBuilder {
        private final String name;
        private final List<DefinitionTree> definitions = new ArrayList<>();

        private NSpaceBuilder(String name) {
            this.name = name;
        }

        public NSpaceBuilder addNative(String name){
            definitions.add(new Trees.BasicNativeFunctionTree(Location.emptyLocation(), name, Set.of()));
            return this;
        }

        public void create(){
            Trees.BasicNamespaceTree tree = new Trees.BasicNamespaceTree(Location.emptyLocation(), name);
            tree.definitions.addAll(definitions);
            nspaces.add(tree);
        }
    }

    private static NSpaceBuilder loadNspace(String name){
        return new NSpaceBuilder(name);
    }


    private static void init(){
        loadNative("Integer");
        loadNative("Real");
        loadNative("Type");
        loadNative("String");
        loadNative("Boolean");
        loadNative("Function");
        loadNative("Range");
        loadNative("Array");
        loadNative("Dictionary");
        loadNative("Null");

        loadNative("print");
        loadNative("exit");
        loadNative("error");
        loadNative("same");
        loadNative("assert");
        loadNative("version");
        loadNative("alert");
        loadNative("confirm");
        loadNative("prompt");
        loadNative("wait");
        loadNative("time");
        loadNative("localtime");
        loadNative("deepcopy");

        loadNative("setEventHandler");
        loadNative("enterEventMode");
        loadNative("quitEventMode");

        loadNative("exists");
        loadNative("load");
        loadNative("save");
        loadNative("listKeys");

        if (true) return;

        loadNspace("math")
                .addNative("pi")
                .addNative("e")
                .addNative("abs")
                .addNative("sqrt")
                .addNative("cbrt")
                .addNative("floor")
                .addNative("round")
                .addNative("ceil")
                .addNative("sin")
                .addNative("cos")
                .addNative("tan")
                .addNative("sinh")
                .addNative("cosh")
                .addNative("tanh")
                .addNative("asin")
                .addNative("acos")
                .addNative("atan")
                .addNative("atan2")
                .addNative("asinh")
                .addNative("acosh")
                .addNative("atanh")
                .addNative("exp")
                .addNative("log")
                .addNative("log2")
                .addNative("log10")
                .addNative("pow")
                .addNative("sign")
                .addNative("min")
                .addNative("max")
                .addNative("random")
                .create();
    }



}
