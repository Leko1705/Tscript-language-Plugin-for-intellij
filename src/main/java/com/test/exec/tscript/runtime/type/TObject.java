package com.test.exec.tscript.runtime.type;

import com.test.exec.tscript.runtime.core.Data;

public interface TObject extends Data {

    TType getType();

    Member get(int index);

    default Member get(String key){
        int idx = getIndex(key);
        if (idx < 0) return null;
        return get(idx);
    }

    int getIndex(String key);

    Iterable<Member> getMembers();

    boolean equals(Object o);


}
