package com.tscript.lang.tscriptc.parse;

import com.tscript.lang.tscriptc.tree.DefinitionTree;
import com.tscript.lang.tscriptc.tree.Trees;
import com.tscript.lang.tscriptc.util.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class Builtins {
    private Builtins(){}

    private static final List<DefinitionTree> builtins = new ArrayList<>();
    public static List<DefinitionTree> getBuiltins(){
        return builtins;
    }



    static {
        init();
    }

    private static void loadNative(String name){
        builtins.add(new Trees.BasicNativeFunctionTree(Location.emptyLocation(), name, Set.of()));
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
    }



}
