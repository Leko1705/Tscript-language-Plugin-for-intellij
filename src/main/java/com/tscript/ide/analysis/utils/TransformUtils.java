package com.tscript.ide.analysis.utils;

public class TransformUtils {

    public static int parseInt(String s){
        s = s.replaceAll("_", "");
        if (s.length() == 1) return Integer.parseInt(s);

        int radix;
        if (s.charAt(1) == 'b')
            radix = 2;
        else if (s.charAt(1) == 'o')
            radix = 8;
        else if (s.charAt(1) == 'x')
            radix = 16;
        else
            return Integer.parseInt(s);

        return Integer.parseInt(s, 2, s.length(), radix);
    }

}
