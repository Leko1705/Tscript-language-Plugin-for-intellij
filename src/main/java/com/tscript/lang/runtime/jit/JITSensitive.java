package com.tscript.lang.runtime.jit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
public @interface JITSensitive {
}
