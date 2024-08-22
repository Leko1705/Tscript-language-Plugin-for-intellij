package com.tscript.lang.runtime.jit;

import com.tscript.lang.tscriptc.generation.Opcode;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ConstantFolder extends ParentDelegationPhase<Void> {

    
    private interface Operation<L, R>{
        BytecodeParser.Tree operate(L l, R r);
    }

    private static final HashMap<Opcode, HashMap<Class<? extends BytecodeParser.Tree>, HashMap<Class<? extends BytecodeParser.Tree>, Operation>>> operationTable = new HashMap<>();
    private static <L extends BytecodeParser.Tree, R extends BytecodeParser.Tree> void addOperation(Opcode opcode, Class<L> first, Class<R> second, Operation<L, R> operation){
        HashMap<Class<? extends BytecodeParser.Tree>, HashMap<Class<? extends BytecodeParser.Tree>, Operation>> operationAssociations;

        if (operationTable.containsKey(opcode)){
            operationAssociations = operationTable.get(opcode);
        }
        else { 
            operationAssociations = new HashMap<>();
            operationTable.put(opcode, operationAssociations);
        }

        if (operationAssociations.containsKey(first)){
            HashMap<Class<? extends BytecodeParser.Tree>, Operation> operationsPerType = operationAssociations.get(first);
            operationsPerType.put(second, operation);
        }
        else {
            HashMap<Class<? extends BytecodeParser.Tree>, Operation> operationsPerType = new HashMap<>();
            operationsPerType.put(second, operation);
            operationAssociations.put(first, operationsPerType);
        }

    }

    static {
        init();
    }

    private static void init(){
        addOperation(Opcode.ADD, BytecodeParser.IntegerTree.class, BytecodeParser.IntegerTree.class, (i1, i2) -> new BytecodeParser.IntegerTree(i1.value + i2.value));
        addOperation(Opcode.SUB, BytecodeParser.IntegerTree.class, BytecodeParser.IntegerTree.class, (i1, i2) -> new BytecodeParser.IntegerTree(i1.value - i2.value));
        addOperation(Opcode.MUL, BytecodeParser.IntegerTree.class, BytecodeParser.IntegerTree.class, (i1, i2) -> new BytecodeParser.IntegerTree(i1.value * i2.value));
        addOperation(Opcode.DIV, BytecodeParser.IntegerTree.class, BytecodeParser.IntegerTree.class, (i1, i2) -> new BytecodeParser.RealTree((double) i1.value / i2.value));
        addOperation(Opcode.IDIV, BytecodeParser.IntegerTree.class, BytecodeParser.IntegerTree.class, (i1, i2) -> new BytecodeParser.IntegerTree(i1.value / i2.value));
        addOperation(Opcode.MOD, BytecodeParser.IntegerTree.class, BytecodeParser.IntegerTree.class, (i1, i2) -> new BytecodeParser.IntegerTree(i1.value % i2.value));
        addOperation(Opcode.POW, BytecodeParser.IntegerTree.class, BytecodeParser.IntegerTree.class, (i1, i2) -> new BytecodeParser.IntegerTree((int) Math.pow(i1.value, i2.value)));

        addOperation(Opcode.SLA, BytecodeParser.IntegerTree.class, BytecodeParser.IntegerTree.class, (i1, i2) -> new BytecodeParser.IntegerTree( i1.value << i2.value));
        addOperation(Opcode.SRA, BytecodeParser.IntegerTree.class, BytecodeParser.IntegerTree.class, (i1, i2) -> new BytecodeParser.IntegerTree( i1.value >> i2.value));
        addOperation(Opcode.SLA, BytecodeParser.IntegerTree.class, BytecodeParser.IntegerTree.class, (i1, i2) -> new BytecodeParser.IntegerTree(i1.value >>> i2.value));

        addOperation(Opcode.LT, BytecodeParser.IntegerTree.class, BytecodeParser.IntegerTree.class, (i1, i2) -> new BytecodeParser.BooleanTree(i1.value < i2.value));
        addOperation(Opcode.LEQ, BytecodeParser.IntegerTree.class, BytecodeParser.IntegerTree.class, (i1, i2) -> new BytecodeParser.BooleanTree(i1.value <= i2.value));
        addOperation(Opcode.GT, BytecodeParser.IntegerTree.class, BytecodeParser.IntegerTree.class, (i1, i2) -> new BytecodeParser.BooleanTree(i1.value > i2.value));
        addOperation(Opcode.GEQ, BytecodeParser.IntegerTree.class, BytecodeParser.IntegerTree.class, (i1, i2) -> new BytecodeParser.BooleanTree(i1.value >= i2.value));

        addOperation(Opcode.AND, BytecodeParser.IntegerTree.class, BytecodeParser.IntegerTree.class, (i1, i2) -> new BytecodeParser.IntegerTree( i1.value & i2.value));
        addOperation(Opcode.OR, BytecodeParser.IntegerTree.class, BytecodeParser.IntegerTree.class, (i1, i2) -> new BytecodeParser.IntegerTree( i1.value | i2.value));
        addOperation(Opcode.XOR, BytecodeParser.IntegerTree.class, BytecodeParser.IntegerTree.class, (i1, i2) -> new BytecodeParser.IntegerTree( i1.value ^ i2.value));

        addOperation(Opcode.AND, BytecodeParser.BooleanTree.class, BytecodeParser.BooleanTree.class, (i1, i2) -> new BytecodeParser.BooleanTree(i1.value && i2.value));
        addOperation(Opcode.OR, BytecodeParser.BooleanTree.class, BytecodeParser.BooleanTree.class, (i1, i2) -> new BytecodeParser.BooleanTree(i1.value || i2.value));
        addOperation(Opcode.XOR, BytecodeParser.BooleanTree.class, BytecodeParser.BooleanTree.class, (i1, i2) -> new BytecodeParser.BooleanTree(i1.value ^ i2.value));

        addOperation(Opcode.ADD, BytecodeParser.IntegerTree.class, BytecodeParser.RealTree.class, (i, r) -> new BytecodeParser.RealTree(i.value + r.value));
        addOperation(Opcode.ADD, BytecodeParser.RealTree.class, BytecodeParser.IntegerTree.class, (r, i) -> new BytecodeParser.RealTree(r.value + i.value));

        addOperation(Opcode.SUB, BytecodeParser.IntegerTree.class, BytecodeParser.RealTree.class, (i, r) -> new BytecodeParser.RealTree(i.value - r.value));
        addOperation(Opcode.SUB, BytecodeParser.RealTree.class, BytecodeParser.IntegerTree.class, (r, i) -> new BytecodeParser.RealTree(r.value - i.value));

        addOperation(Opcode.MUL, BytecodeParser.IntegerTree.class, BytecodeParser.RealTree.class, (i, r) -> new BytecodeParser.RealTree(i.value * r.value));
        addOperation(Opcode.MUL, BytecodeParser.RealTree.class, BytecodeParser.IntegerTree.class, (r, i) -> new BytecodeParser.RealTree(r.value * i.value));

        addOperation(Opcode.DIV, BytecodeParser.IntegerTree.class, BytecodeParser.RealTree.class, (i, r) -> new BytecodeParser.RealTree(i.value / r.value));
        addOperation(Opcode.DIV, BytecodeParser.RealTree.class, BytecodeParser.IntegerTree.class, (r, i) -> new BytecodeParser.RealTree(r.value / i.value));

        addOperation(Opcode.POW, BytecodeParser.IntegerTree.class, BytecodeParser.RealTree.class, (i, r) -> new BytecodeParser.RealTree(Math.pow(i.value, r.value)));
        addOperation(Opcode.POW, BytecodeParser.RealTree.class, BytecodeParser.IntegerTree.class, (r, i) -> new BytecodeParser.RealTree(Math.pow(r.value, i.value)));

        addOperation(Opcode.ADD, BytecodeParser.RealTree.class, BytecodeParser.RealTree.class, (i1, i2) -> new BytecodeParser.RealTree(i1.value + i2.value));
        addOperation(Opcode.SUB, BytecodeParser.RealTree.class, BytecodeParser.RealTree.class, (i1, i2) -> new BytecodeParser.RealTree(i1.value - i2.value));
        addOperation(Opcode.MUL, BytecodeParser.RealTree.class, BytecodeParser.RealTree.class, (i1, i2) -> new BytecodeParser.RealTree(i1.value * i2.value));
        addOperation(Opcode.DIV, BytecodeParser.RealTree.class, BytecodeParser.RealTree.class, (i1, i2) -> new BytecodeParser.RealTree((double) i1.value / i2.value));
        addOperation(Opcode.MOD, BytecodeParser.RealTree.class, BytecodeParser.RealTree.class, (i1, i2) -> new BytecodeParser.RealTree(i1.value % i2.value));
        addOperation(Opcode.POW, BytecodeParser.RealTree.class, BytecodeParser.RealTree.class, (i1, i2) -> new BytecodeParser.RealTree((int) Math.pow(i1.value, i2.value)));
    }

    private Map<Integer, BytecodeParser.Tree> locals = new HashMap<>();

    private void forgetValues(){
        locals.replaceAll((i, t) -> null);
    }


    @Override
    public Void visitUnaryOperationTree(BytecodeParser.UnaryOperationTree operationTree, BytecodeParser.Tree parent) {
        scan(operationTree.exp, operationTree);

        switch (operationTree.operation){
            case NOT -> {
                if (operationTree.exp instanceof BytecodeParser.BooleanTree b){
                    b.value = !b.value;
                    parent.replace(operationTree, operationTree.exp);
                    optimizationPerformed();
                }
                else if (operationTree.exp instanceof BytecodeParser.IntegerTree i){
                    i.value = ~i.value;
                    parent.replace(operationTree, operationTree.exp);
                    optimizationPerformed();
                }
            }
            case NEG -> {
                if (operationTree.exp instanceof BytecodeParser.IntegerTree i){
                    i.value = -i.value;
                    parent.replace(operationTree, operationTree.exp);
                    optimizationPerformed();
                }
            }
            case POS -> {
                if (operationTree.exp instanceof BytecodeParser.IntegerTree){
                    parent.replace(operationTree, operationTree.exp);
                    optimizationPerformed();
                }
            }
        }

        return null;
    }

    @Override
    public Void visitBinaryOperationTree(BytecodeParser.BinaryOperationTree operationTree, BytecodeParser.Tree parent) {

        scan(operationTree.left, operationTree);
        scan(operationTree.right, operationTree);

        if (operationTree.left instanceof BytecodeParser.StringTree s && operationTree.right instanceof BytecodeParser.LiteralTree<?> l){
            BytecodeParser.StringTree stringTree = new BytecodeParser.StringTree(s.value + l.value);
            parent.replace(operationTree, stringTree);
            return null;
        }
        else if (operationTree.right instanceof BytecodeParser.StringTree s && operationTree.left instanceof BytecodeParser.LiteralTree<?> l){
            BytecodeParser.StringTree stringTree = new BytecodeParser.StringTree(l.value + s.value);
            parent.replace(operationTree, stringTree);
            return null;
        }

        HashMap<Class<? extends BytecodeParser.Tree>, Operation> map = operationTable.get(operationTree.operation).get(operationTree.left.getClass());
        if (map != null){
            Operation operation = map.get(operationTree.right.getClass());
            if (operation != null){
                BytecodeParser.Tree folded = operation.operate(operationTree.left, operationTree.right);
                parent.replace(operationTree, folded);
                optimizationPerformed();
            }
        }

        return null;
    }

    @Override
    public Void visitIfElseTree(BytecodeParser.IfElseTree ifElseTree, BytecodeParser.Tree parent) {
        scan(ifElseTree.condition, ifElseTree);

        Map<Integer, BytecodeParser.Tree> rememberedLocals = new HashMap<>(locals);
        scan(ifElseTree.ifBody, ifElseTree);

        locals = rememberedLocals;
        scan(ifElseTree.ifBody, ifElseTree);
        forgetValues();

        return null;
    }

    @Override
    public Void visitForLoopTree(BytecodeParser.ForLoopTree forLoopTree, BytecodeParser.Tree parent) {
        scan(forLoopTree.iterable, forLoopTree);
        forgetValues();
        scan(forLoopTree.body, forLoopTree);
        forgetValues();
        return null;
    }

    @Override
    public Void visitLoadLocalTree(BytecodeParser.LoadLocalTree loadLocalTree, BytecodeParser.Tree parent) {
        int address = loadLocalTree.address();
        if (!locals.containsKey(address))
            locals.put(address, null);
        BytecodeParser.Tree value = locals.get(address);
        if (value != null) {
            parent.replace(loadLocalTree, value);
            optimizationPerformed();
        }
        return null;
    }

    @Override
    public Void visitStoreLocalTree(BytecodeParser.StoreLocalTree storeLocalTree, BytecodeParser.Tree parent) {
        scan(storeLocalTree.child, storeLocalTree);
        int address = storeLocalTree.address;
        locals.put(address, storeLocalTree.child);
        return null;
    }

    @Override
    public Void visitReadContainerTree(BytecodeParser.ReadContainerTree readTree, BytecodeParser.Tree parent) {
        scan(readTree.key, readTree);

        if (readTree.container instanceof BytecodeParser.ArrayTree a
                && readTree.key instanceof BytecodeParser.IntegerTree i){
            parent.replace(readTree, a.arguments().get(i.value));
            optimizationPerformed();
        }
        else if (readTree.container instanceof BytecodeParser.StringTree s
                && readTree.key instanceof BytecodeParser.IntegerTree i){
            String subString = Character.toString(s.value.charAt(i.value));
            parent.replace(readTree, new BytecodeParser.StringTree(subString));
            optimizationPerformed();
        }
        else if (readTree.container instanceof BytecodeParser.RangeTree r
                && readTree.key instanceof BytecodeParser.IntegerTree i){
            if (r.from instanceof BytecodeParser.IntegerTree f && r.to instanceof BytecodeParser.IntegerTree t){
                if (i.value >= f.value && i.value < t.value){
                    parent.replace(readTree, new BytecodeParser.IntegerTree(i.value));
                    optimizationPerformed();
                }
            }
        }

        return null;
    }

}
