package com.tscript.ide.highlight;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;

import java.util.Arrays;

public class Styles {

    private Styles(){}

    public static TextAttributesKey mergeAttributes(TextAttributesKey... keys) {
        TextAttributes result = new TextAttributes();
        for (TextAttributesKey key : keys) {
            TextAttributes attributes = key.getDefaultAttributes();
            if (attributes != null) {
                result = TextAttributes.merge(result, attributes);
            }
        }
        return TextAttributesKey.createTextAttributesKey("MERGED_" + Arrays.hashCode(keys), result);
    }

}
