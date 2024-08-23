package com.tscript.ide.analysis.typing;

import com.tscript.ide.analysis.utils.Visibility;

import java.util.Map;
import java.util.function.Function;

public record Member(String name, Visibility visibility, Function<Map<String, Type>, Type> type, boolean isStatic){

    public Member(String name, Visibility visibility, String type, boolean isStatic){
        this(name, visibility, m -> m.get(type), isStatic);
    }

}