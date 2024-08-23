package com.tscript.ide.analysis.hierarchy;

import com.tscript.ide.analysis.symtab.Symbol;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public record Hierarchy(Layer topLevel) {

    public Symbol search(List<String> list) {
        return topLevel.search(list.iterator());
    }

    public int getFailIndex(List<String> list) {
        return topLevel.getFailIndex(list.iterator(), 0);
    }


    public static class Layer {

        private final Symbol symbol;
        public final Map<String, Layer> subLayers = new HashMap<>();

        public Layer(Symbol symbol) {
            this.symbol = symbol;
        }

        public Symbol search(Iterator<String> iterator) {
            if (!iterator.hasNext()) return symbol;
            String key = iterator.next();
            Hierarchy.Layer layer = subLayers.get(key);
            if (layer == null) return null;
            return layer.search(iterator);
        }

        public int getFailIndex(Iterator<String> iterator, int index) {
            if (!iterator.hasNext()) return index;
            String key = iterator.next();
            Hierarchy.Layer layer = subLayers.get(key);
            if (layer == null) return index;
            return layer.getFailIndex(iterator, index + 1);
        }

    }
}
