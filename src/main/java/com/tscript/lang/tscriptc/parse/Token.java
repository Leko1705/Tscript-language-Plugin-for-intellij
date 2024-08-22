package com.tscript.lang.tscriptc.parse;

import com.tscript.lang.tscriptc.util.Location;

public interface Token {

    Location getLocation();

    String getLexem();

    Object getTag();

    default boolean hasTag(Object tag, Object... tags){
        if (tag.equals(getTag())) return true;
        for (Object t : tags)
            if (t.equals(getTag()))
                return true;
        return false;
    }

}
