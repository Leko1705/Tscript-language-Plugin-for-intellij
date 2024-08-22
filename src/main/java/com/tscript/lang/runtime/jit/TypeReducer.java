package com.tscript.lang.runtime.jit;

import com.tscript.lang.runtime.core.Data;
import com.tscript.lang.runtime.type.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TypeReducer extends ParentDelegationPhase<TypeReducer.TypeInfo> {

    private final TypeInfo INTEGER = new TypeInfo("Integer");
    private final TypeInfo REAL = new TypeInfo("Real");
    private final TypeInfo STRING = new TypeInfo("String");
    private final TypeInfo NULL = new TypeInfo("NullType");
    private final TypeInfo RANGE = new TypeInfo("Range");
    private final TypeInfo ARRAY = new TypeInfo("Array");
    private final TypeInfo DICTIONARY = new TypeInfo("Dictionary");
    private final TypeInfo TYPE = new TypeInfo("Type");
    private final TypeInfo BOOLEAN = new TypeInfo("Boolean");


    private final Map<Integer, TypeInfo> localTypeInfo = new HashMap<>();


    @Override
    public TypeInfo visitRootTree(BytecodeParser.RootTree rootTree, BytecodeParser.Tree parent) {
        for (int i = 0; i < args.length; i++)
            localTypeInfo.put(i, new TypeInfo(args[i]));
        return super.visitRootTree(rootTree, parent);
    }

    @Override
    public TypeInfo visitNullTree(BytecodeParser.NullTree nullTree, BytecodeParser.Tree parent) {
        return NULL;
    }

    @Override
    public TypeInfo visitIntegerTree(BytecodeParser.IntegerTree integerTree, BytecodeParser.Tree parent) {
        return INTEGER;
    }

    @Override
    public TypeInfo visitRealTree(BytecodeParser.RealTree realTree, BytecodeParser.Tree tree) {
        return REAL;
    }

    @Override
    public TypeInfo visitBooleanTree(BytecodeParser.BooleanTree booleanTree, BytecodeParser.Tree parent) {
        return BOOLEAN;
    }

    @Override
    public TypeInfo visitStringTree(BytecodeParser.StringTree stringTree, BytecodeParser.Tree tree) {
        return STRING;
    }

    @Override
    public TypeInfo visitRangeTree(BytecodeParser.RangeTree rangeTree, BytecodeParser.Tree parent) {
        return RANGE;
    }

    @Override
    public TypeInfo visitGetTypeTree(BytecodeParser.GetTypeTree getTypeTree, BytecodeParser.Tree parent) {
        super.visitGetTypeTree(getTypeTree, parent);
        return TYPE;
    }

    @Override
    public TypeInfo visitArrayTree(BytecodeParser.ArrayTree arrayTree, BytecodeParser.Tree parent) {
        super.visitArrayTree(arrayTree, parent);
        return ARRAY;
    }

    @Override
    public TypeInfo visitDictionaryTree(BytecodeParser.DictionaryTree dictionaryTree, BytecodeParser.Tree parent) {
        super.visitDictionaryTree(dictionaryTree, parent);
        return DICTIONARY;
    }

    @Override
    public TypeInfo visitConstantTree(BytecodeParser.ConstantTree constantTree, BytecodeParser.Tree parent) {
        return null;
    }

    @Override
    public TypeInfo visitLoadMemberTree(BytecodeParser.LoadMemberTree loadTree, BytecodeParser.Tree parent) {
        TypeInfo typeInfo = scan(loadTree.exp, loadTree);

        if (typeInfo != null && typeInfo.data != null){
            TObject object = jit.unpack(typeInfo.data);
            int address = object.getIndex(loadTree.address);
            if (address >= 0){
                BytecodeParser.Tree folded = foldConstantField(object.get(address));
                parent.replace(loadTree, Objects.requireNonNullElseGet(folded, () -> new BytecodeParser.AccessUnknownFastTree(address, loadTree.exp)));
                optimizationPerformed();
            }
        }

        return null;
    }

    private BytecodeParser.Tree foldConstantField(Member member){
        if (member.kind == Member.Kind.MUTABLE) return null;
        TObject object = jit.unpack(member.data);

        if (object instanceof TInteger i){
            return new BytecodeParser.IntegerTree(i.get());
        }
        if (object instanceof TReal r){
            return new BytecodeParser.RealTree(r.get());
        }
        if (object instanceof TNull){
            return new BytecodeParser.NullTree();
        }
        if (object instanceof TBoolean b){
            return new BytecodeParser.BooleanTree(b.get());
        }
        if (object instanceof TString s){
            return new BytecodeParser.StringTree(s.get());
        }
        if (object instanceof TRange r){
            return new BytecodeParser.RangeTree(new BytecodeParser.IntegerTree(r.getFrom()), new BytecodeParser.IntegerTree(r.getTo()));
        }

        return null;
    }

    @Override
    public TypeInfo visitEqualsTree(BytecodeParser.EqualsTree equalsTree, BytecodeParser.Tree parent) {
        TypeInfo left = scan(equalsTree.left, equalsTree);
        TypeInfo right = scan(equalsTree.right, equalsTree);

        if (left != null && right != null && !typeInfosAreEqual(left, right)){
            parent.replace(equalsTree, new BytecodeParser.BooleanTree(false));
            optimizationPerformed();
        }

        return BOOLEAN;
    }

    @Override
    public TypeInfo visitLoadLocalTree(BytecodeParser.LoadLocalTree loadLocalTree, BytecodeParser.Tree parent) {
        return localTypeInfo.get(loadLocalTree.address());
    }

    @Override
    public TypeInfo visitStoreLocalTree(BytecodeParser.StoreLocalTree storeLocalTree, BytecodeParser.Tree parent) {
        TypeInfo typeInfo = scan(storeLocalTree.child, storeLocalTree);
        localTypeInfo.put(storeLocalTree.address, typeInfo);
        return null;
    }

    public class TypeInfo{
        public final String name;
        public final Data data;
        public TypeInfo(Data data){
            this.name = jit.unpack(data).getType().getName();
            this.data = data;
        }
        public TypeInfo(String name){
            this.name = name;
            this.data = null;
        }

        @Override
        public String toString() {
            return "TypeInfo{" +
                    "name='" + name + '\'' +
                    ", data=" + data +
                    '}';
        }
    }

    private boolean typeInfosAreEqual(TypeInfo t1, TypeInfo t2){
        if (t1.name == null) return false;
        if (t2.name == null) return false;
        return t1.name.equals(t2.name);
    }


}
