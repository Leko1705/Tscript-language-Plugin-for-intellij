package com.tscript.lang.runtime.core;

import com.tscript.lang.runtime.debug.DataInfo;
import com.tscript.lang.runtime.debug.Debuggable;
import com.tscript.lang.runtime.heap.Heap;
import com.tscript.lang.runtime.type.Member;
import com.tscript.lang.runtime.type.TObject;
import com.tscript.lang.runtime.type.TString;

import java.util.ArrayList;
import java.util.List;

public interface Data extends Debuggable<DataInfo> {

    default boolean isValue(){
        return this instanceof TObject;
    }

    default TObject asValue(){
        return (TObject) this;
    }

    default boolean isReference(){
        return this instanceof Reference;
    }

    default Reference asReference(){
        return (Reference) this;
    }

    boolean equals(Object o);

    @Override
    default DataInfo loadInfo(Heap heap) {

        return new DataInfo() {

            private final List<DataInfo> children = new ArrayList<>();

            @Override
            public List<DataInfo> getChildren() {
                TObject object = unpack();
                for (Member member : object.getMembers())
                    if (member.data != null)
                        children.add(member.data.loadInfo(heap));
                return children;
            }

            private TObject unpack(){
                if (isReference())
                    return heap.load(asReference());
                else
                    return asValue();
            }

            @Override
            public String toString() {
                String s = Data.this.toString();
                if (Data.this instanceof TString)
                    s = "\"" + s + "\"";
                return s;
            }
        };
    }
}
