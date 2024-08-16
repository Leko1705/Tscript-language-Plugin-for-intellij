package com.test.exec.tscript.runtime.type;

import java.util.List;

public class VirtualObject extends BaseObject {

    private final TType type;

    public VirtualObject(TType type, List<Member> members) {
        super(members);
        this.type = type;
    }

    @Override
    public TType getType() {
        return type;
    }

}
