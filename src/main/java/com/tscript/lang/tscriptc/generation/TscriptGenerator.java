package com.tscript.lang.tscriptc.generation;

import com.tscript.lang.tscriptc.parse.Builtins;

import com.tscript.lang.tscriptc.util.TreeScanner;
import com.tscript.lang.tscriptc.tree.*;

import java.util.Iterator;
import java.util.Set;

public class TscriptGenerator extends TreeScanner<StringBuilder, Void> {
    
    private final boolean webVersion;

    private int indentation = 0;
    private boolean inFunction = false;
    
    public TscriptGenerator(){
        this(false);
    }

    public TscriptGenerator(boolean webVersion) {
        this.webVersion = webVersion;
    }

    private void indent(StringBuilder stringBuilder){
        stringBuilder.append("\t".repeat(indentation));
    }

    private void tabIn(){
        indentation++;
    }

    private void tabOut(){
        indentation--;
    }

    public String generate(Tree tree){
        StringBuilder result = new StringBuilder();
        tree.accept(this, result);
        return result.toString();
    }

    @Override
    public Void visitArgumentTree(ArgumentTree node, StringBuilder stringBuilder) {
        if (node.getReferencedName() != null) {
            stringBuilder.append(node.getReferencedName());
            if (node.getExpression() != null) {
                stringBuilder.append("=");
                scan(node.getExpression(), stringBuilder);
            }
        }
        else {
            scan(node.getExpression(), stringBuilder);
        }
        return null;
    }

    @Override
    public Void visitArrayTree(ArrayTree node, StringBuilder stringBuilder) {
        stringBuilder.append("[");
        Iterator<? extends ExpressionTree> itr = node.getContent().iterator();

        if (itr.hasNext()){
            scan(itr.next(), stringBuilder);
            while (itr.hasNext()){
                stringBuilder.append(", ");
                scan(itr.next(), stringBuilder);
            }
        }
        stringBuilder.append("]");
        return null;
    }

    @Override
    public Void visitAssignTree(AssignTree node, StringBuilder stringBuilder) {
        scan(node.getLeft(), stringBuilder);
        stringBuilder.append(" = ");
        scan(node.getRight(), stringBuilder);
        return null;
    }

    @Override
    public Void visitOperationTree(BinaryOperationTree node, StringBuilder stringBuilder) {

        stringBuilder.append("(");

        switch (node.getOperation()){
            case SHIFT_AL -> {
                if (webVersion) {
                    scan(node.getLeft(), stringBuilder);
                    stringBuilder.append(" // (2^");
                    scan(node.getRight(), stringBuilder);
                    stringBuilder.append(")");
                }
                else {
                    scan(node.getLeft(), stringBuilder);
                    stringBuilder.append(" ").append(node.getOperation().encoding).append(" ");
                    scan(node.getRight(), stringBuilder);
                }
            }
            case SHIFT_AR -> {
                if (webVersion) {
                    scan(node.getLeft(), stringBuilder);
                    stringBuilder.append(" * (2^");
                    scan(node.getRight(), stringBuilder);
                    stringBuilder.append(")");
                }
                else {
                    scan(node.getLeft(), stringBuilder);
                    stringBuilder.append(" ").append(node.getOperation().encoding).append(" ");
                    scan(node.getRight(), stringBuilder);
                }
            }
            case TYPEOF -> {
                if (webVersion) {
                    stringBuilder.append("Type(");
                    scan(node.getLeft(), stringBuilder);
                    stringBuilder.append(") == ");
                    scan(node.getRight(), stringBuilder);
                }
                else {
                    scan(node.getLeft(), stringBuilder);
                    stringBuilder.append(" ").append(node.getOperation().encoding).append(" ");
                    scan(node.getRight(), stringBuilder);
                }
            }
            default -> {
                scan(node.getLeft(), stringBuilder);
                stringBuilder.append(" ").append(node.getOperation().encoding).append(" ");
                scan(node.getRight(), stringBuilder);
            }
        }


        stringBuilder.append(")");
        return null;
    }

    @Override
    public Void visitBlockTree(BlockTree node, StringBuilder stringBuilder) {
        indent(stringBuilder);
        stringBuilder.append("{\n");
        tabIn();
        for (StatementTree statement : node.getStatements()) {
            statement.accept(this, stringBuilder);
        }
        tabOut();
        indent(stringBuilder);
        stringBuilder.append("}\n");
        return null;
    }
    
    @Override
    public Void visitBooleanTree(BooleanLiteralTree node, StringBuilder stringBuilder) {
        stringBuilder.append(node.get());
        return null;
    }
    
    @Override
    public Void visitBreakTree(BreakTree node, StringBuilder stringBuilder) {
        indent(stringBuilder);
        stringBuilder.append("break;\n");
        return null;
    }

    @Override
    public Void visitCallTree(CallTree node, StringBuilder stringBuilder) {
        scan(node.getExpression(), stringBuilder);
        stringBuilder.append("(");
        Iterator<? extends ArgumentTree> argItr = node.getArguments().iterator();
        if (argItr.hasNext()){
            scan(argItr.next(), stringBuilder);
            while (argItr.hasNext()){
                stringBuilder.append(", ");
                scan(argItr.next(), stringBuilder);
            }
        }
        stringBuilder.append(")");
        return null;
    }

    @Override
    public Void visitClassTree(ClassTree node, StringBuilder stringBuilder) {
        indent(stringBuilder);
        stringBuilder.append("class ").append(node.getName()).append(" {");
        tabIn();
        scan(node.getConstructor(), stringBuilder);
        scan(node.getDefinitions(), stringBuilder);
        stringBuilder.append("}\n\n");
        tabOut();
        return null;
    }

    @Override
    public Void visitClosureTree(ClosureTree node, StringBuilder stringBuilder) {
        stringBuilder.append(node.getName());
        if (node.getExpression() != null){
            stringBuilder.append("=");
            scan(node.getExpression(), stringBuilder);
        }
        return null;
    }

    @Override
    public Void visitConstructorTree(ConstructorTree node, StringBuilder stringBuilder) {
        indent(stringBuilder);
        scan(node.getModifiers(), stringBuilder);
        stringBuilder.append(" constructor(");
        Iterator<? extends ParameterTree> paramItr = node.getParameters().iterator();
        if (paramItr.hasNext()){
            scan(paramItr.next(), stringBuilder);
            while (paramItr.hasNext()){
                stringBuilder.append(", ");
                scan(paramItr.next(), stringBuilder);
            }
        }
        stringBuilder.append(")");
        if (!node.getSuperArguments().isEmpty()){
            stringBuilder.append(" : super(");
            Iterator<? extends ArgumentTree> superItr = node.getSuperArguments().iterator();
            if (superItr.hasNext()){
                scan(superItr.next(), stringBuilder);
                while (superItr.hasNext()){
                    stringBuilder.append(", ");
                    scan(superItr.next(), stringBuilder);
                }
            }
            stringBuilder.append(")");
        }

        scan(node.getBody(), stringBuilder);
        return null;
    }

    @Override
    public Void visitContainerAccessTree(ContainerAccessTree node, StringBuilder stringBuilder) {
        scan(node.getExpression(), stringBuilder);
        stringBuilder.append("[");
        scan(node.getKey(), stringBuilder);
        stringBuilder.append("]");
        return null;
    }

    @Override
    public Void visitContinueTree(ContinueTree node, StringBuilder stringBuilder) {
        indent(stringBuilder);
        stringBuilder.append("continue;\n");
        return null;
    }

    @Override
    public Void visitDictionaryTree(DictionaryTree node, StringBuilder stringBuilder) {
        Iterator<? extends ExpressionTree> keyItr = node.getKeys().iterator();
        Iterator<? extends ExpressionTree> valItr = node.getValues().iterator();

        stringBuilder.append("{");

        if (keyItr.hasNext()){
            scan(keyItr.next(), stringBuilder);
            stringBuilder.append(": ");
            scan(valItr.next(), stringBuilder);

            while (keyItr.hasNext()){
                stringBuilder.append(", ");
                scan(keyItr.next(), stringBuilder);
                stringBuilder.append(": ");
                scan(valItr.next(), stringBuilder);
            }
        }

        stringBuilder.append("}");
        return null;
    }

    @Override
    public Void visitDoWhileTree(DoWhileTree node, StringBuilder stringBuilder) {
        indent(stringBuilder);
        stringBuilder.append("do ");
        boolean prevInFunction = this.inFunction;
        this.inFunction = false;
        scan(node.getBody(), stringBuilder);
        this.inFunction = prevInFunction;
        stringBuilder.append(" while ");
        scan(node.getCondition(), stringBuilder);
        stringBuilder.append(";\n");
        return null;
    }

    @Override
    public Void visitExpressionStatementTree(ExpressionStatementTree node, StringBuilder stringBuilder) {
        indent(stringBuilder);
        scan(node.getExpression(), stringBuilder);
        stringBuilder.append(";\n");
        return null;
    }

    @Override
    public Void visitFloatTree(FloatLiteralTree node, StringBuilder stringBuilder) {
        stringBuilder.append(node.get());
        return null;
    }

    @Override
    public Void visitForLoopTree(ForLoopTree node, StringBuilder stringBuilder) {
        indent(stringBuilder);
        stringBuilder.append("for ");
        if (node.getName() != null){
            stringBuilder.append("var ").append(node.getName()).append(" in ");
        }
        scan(node.getIterable(), stringBuilder);
        stringBuilder.append(" do ");
        boolean prevInFunction = this.inFunction;
        this.inFunction = false;
        scan(node.getBody(), stringBuilder);
        this.inFunction = prevInFunction;
        stringBuilder.append("\n");

        return null;
    }

    @Override
    public Void visitNativeFunctionTree(NativeFunctionTree node, StringBuilder stringBuilder) {
        indent(stringBuilder);
        if (Builtins.getBuiltins().contains(node)) return null;
        scan(node.getModifiers(), stringBuilder);
        stringBuilder.append("native function ").append(node.getName()).append(";\n");
        return null;
    }

    @Override
    public Void visitAbstractMethodTree(AbstractMethodTree node, StringBuilder stringBuilder) {
        indent(stringBuilder);
        scan(node.getModifiers(), stringBuilder);
        stringBuilder.append("function ").append(node.getName()).append(";\n");
        return null;
    }

    @Override
    public Void visitFunctionTree(FunctionTree node, StringBuilder stringBuilder) {
        indent(stringBuilder);
        scan(node.getModifiers(), stringBuilder);
        stringBuilder.append("function ").append(node.getName()).append("(");
        Iterator<? extends ParameterTree> paramItr = node.getParameters().iterator();
        if (paramItr.hasNext()){
            scan(paramItr.next(), stringBuilder);
            while (paramItr.hasNext()){
                stringBuilder.append(", ");
                scan(paramItr.next(), stringBuilder);
            }
        }
        stringBuilder.append(")");
        boolean prevInFunction = this.inFunction;
        this.inFunction = true;
        scan(node.getBody(), stringBuilder);
        this.inFunction = prevInFunction;
        stringBuilder.append("\n");

        return null;
    }

    @Override
    public Void visitGetTypeTree(GetTypeTree node, StringBuilder stringBuilder) {
        if (webVersion){
            stringBuilder.append("Type(");
            scan(node.getExpression(), stringBuilder);
            stringBuilder.append(")");
        }
        else {
            stringBuilder.append("typeof ");
            scan(node.getExpression(), stringBuilder);
        }
        return null;
    }

    @Override
    public Void visitIfElseTree(IfElseTree node, StringBuilder stringBuilder) {
        indent(stringBuilder);
        stringBuilder.append("if ");
        scan(node.getCondition(), stringBuilder);
        stringBuilder.append(" then");
        handleBlockCandidate(node.getIfBody(), stringBuilder);
        if (node.getElseBody() != null){
            indent(stringBuilder);
            stringBuilder.append("else");
            handleBlockCandidate(node.getElseBody(), stringBuilder);
        }
        stringBuilder.append("\n");
        return null;
    }

    @Override
    public Void visitImportTree(ImportTree node, StringBuilder stringBuilder) {
        if (!webVersion) return null;
        indent(stringBuilder);
        stringBuilder.append("import ");
        Iterator<String> impItr = new ArrayItr<>(node.getPath());
        if (impItr.hasNext()){
            stringBuilder.append(impItr.next());
            while (impItr.hasNext()){
                stringBuilder.append(".");
                stringBuilder.append(impItr.next());
            }
        }

        stringBuilder.append(";\n");
        return null;
    }

    @Override
    public Void visitIntegerTree(IntegerLiteralTree node, StringBuilder stringBuilder) {
        stringBuilder.append(node.get());
        return null;
    }

    @Override
    public Void visitLambdaTree(LambdaTree node, StringBuilder stringBuilder) {
        stringBuilder.append("function ");
        if (!node.getClosures().isEmpty()){
            stringBuilder.append("[");
            Iterator<? extends ClosureTree> closures = node.getClosures().iterator();
            if (closures.hasNext()){
                scan(closures.next(), stringBuilder);
                while (closures.hasNext()){
                    stringBuilder.append(", ");
                    scan(closures.next(), stringBuilder);
                }
            }
            stringBuilder.append("]");
        }

        stringBuilder.append("(");
        Iterator<? extends ParameterTree> paramItr = node.getParameters().iterator();
        if (paramItr.hasNext()){
            scan(paramItr.next(), stringBuilder);
            while (paramItr.hasNext()){
                stringBuilder.append(", ");
                scan(paramItr.next(), stringBuilder);
            }
        }
        stringBuilder.append(")");

        boolean prevInFunction = this.inFunction;
        this.inFunction = true;
        scan(node.getBody(), stringBuilder);
        this.inFunction = prevInFunction;

        return null;
    }

    @Override
    public Void visitMemberAccessTree(MemberAccessTree node, StringBuilder stringBuilder) {
        scan(node.getExpression(), stringBuilder);
        stringBuilder.append(".");
        stringBuilder.append(node.getMemberName());
        return null;
    }

    private void scan(Set<Modifier> modifiers, StringBuilder stringBuilder){
        for (Modifier modifier : modifiers) {
            if (modifier.isVisibility()){
                String v = switch (modifier){
                    case PUBLIC -> "public";
                    case PROTECTED -> "protected";
                    case PRIVATE -> "private";
                    default -> throw new IllegalStateException("unreachable");
                };
                stringBuilder.append(v).append(": ");
            }
        }

        if (!webVersion && modifiers.contains(Modifier.IMMUTABLE))
            stringBuilder.append("const ");

        if (modifiers.contains(Modifier.STATIC))
            stringBuilder.append("static ");

        if (!webVersion) {
            if (modifiers.contains(Modifier.ABSTRACT))
                stringBuilder.append("abstract ");

            if (modifiers.contains(Modifier.OVERRIDDEN))
                stringBuilder.append("overridden ");
        }
    }

    @Override
    public Void visitNamespaceTree(NamespaceTree node, StringBuilder stringBuilder) {
        indent(stringBuilder);
        scan(node.getModifiers(), stringBuilder);
        stringBuilder.append("namespace ").append(node.getName()).append("{");
        scan(node.getDefinitions(), stringBuilder);
        stringBuilder.append("}\n\n");

        return null;
    }

    @Override
    public Void visitNotTree(NotTree node, StringBuilder stringBuilder) {
        stringBuilder.append("not ");
        scan(node.getExpression(), stringBuilder);
        return null;
    }

    @Override
    public Void visitNullTree(NullLiteralTree node, StringBuilder stringBuilder) {
        stringBuilder.append("null");
        return null;
    }

    @Override
    public Void visitParameterTree(ParameterTree node, StringBuilder stringBuilder) {
        if (!webVersion && node.isConstant()){
            stringBuilder.append("const ");
        }
        stringBuilder.append(node.getName());
        if (node.getInitializer() != null){
            stringBuilder.append("=");
            scan(node.getInitializer(), stringBuilder);
        }
        return null;
    }

    @Override
    public Void visitRangeTree(RangeTree node, StringBuilder stringBuilder) {
        scan(node.getFrom(), stringBuilder);
        stringBuilder.append(":");
        scan(node.getTo(), stringBuilder);
        return null;
    }

    @Override
    public Void visitReturnTree(ReturnTree node, StringBuilder stringBuilder) {
        if (node.getExpression() instanceof NullLiteralTree && inFunction)
            return null;

        indent(stringBuilder);
        stringBuilder.append("return");
        if (node.getExpression() != null){
            stringBuilder.append(" ");
            scan(node.getExpression(), stringBuilder);
        }
        stringBuilder.append(";\n");
        return null;
    }

    @Override
    public Void visitRootTree(RootTree node, StringBuilder stringBuilder) {
        scan(node.getDefinitions(), stringBuilder);
        scan(node.getStatements(), stringBuilder);
        return null;
    }

    @Override
    public Void visitSignTree(SignTree node, StringBuilder stringBuilder) {
        stringBuilder.append("-");
        scan(node.getExpression(), stringBuilder);
        return null;
    }

    @Override
    public Void visitStringTree(StringLiteralTree node, StringBuilder stringBuilder) {
        stringBuilder.append("\"").append(node.get()).append("\"");
        return null;
    }

    @Override
    public Void visitSuperTree(SuperTree node, StringBuilder stringBuilder) {
        stringBuilder.append("super.").append(node.getName());
        return null;
    }

    @Override
    public Void visitThisTree(ThisTree node, StringBuilder stringBuilder) {
        stringBuilder.append("this");
        return null;
    }

    @Override
    public Void visitThrowTree(ThrowTree node, StringBuilder stringBuilder) {
        indent(stringBuilder);
        stringBuilder.append("throw ");
        scan(node.getExpression(), stringBuilder);
        stringBuilder.append(";\n");
        return null;
    }

    @Override
    public Void visitTryCatchTree(TryCatchTree node, StringBuilder stringBuilder) {
        indent(stringBuilder);
        stringBuilder.append("try");
        handleBlockCandidate(node.getTryBody(), stringBuilder);
        stringBuilder.append("\n");
        indent(stringBuilder);
        stringBuilder.append("catch var ").append(node.getExceptionName());
        stringBuilder.append(" do");
        handleBlockCandidate(node.getCatchBody(), stringBuilder);
        stringBuilder.append("\n");
        return null;
    }

    @Override
    public Void visitUseTree(UseTree node, StringBuilder stringBuilder) {
        indent(stringBuilder);
        stringBuilder.append("use ").append(node.getName()).append(";\n");
        return null;
    }

    @Override
    public Void visitMultiVarDecTree(MultiVarDecTree varDecTrees, StringBuilder stringBuilder) {
        indent(stringBuilder);

        Iterator<? extends VarDecTree> varDefItr = varDecTrees.getDeclarations().iterator();
        if (varDefItr.hasNext()){
            VarDecTree first = varDefItr.next();
            if (!webVersion && first.isConstant()){
                stringBuilder.append("const ");
            }
            else {
                stringBuilder.append("var ");
            }

            scan(first, stringBuilder);
            while (varDefItr.hasNext()){
                stringBuilder.append(", ");
                scan(varDefItr.next(), stringBuilder);
            }
            stringBuilder.append(";\n");
        }

        return null;
    }

    @Override
    public Void visitVarDecTree(VarDecTree node, StringBuilder stringBuilder) {
        stringBuilder.append(node.getName());
        if (node.getInitializer() != null){
            stringBuilder.append(" = ");
            scan(node.getInitializer(), stringBuilder);
        }
        return null;
    }

    @Override
    public Void visitIdentifierTree(IdentifierTree node, StringBuilder stringBuilder) {
        stringBuilder.append(node.getName());
        return null;
    }

    @Override
    public Void visitWhileDoTree(WhileDoTree node, StringBuilder stringBuilder) {
        indent(stringBuilder);
        stringBuilder.append("while ");
        scan(node.getCondition(), stringBuilder);
        stringBuilder.append(" do");
        handleBlockCandidate(node.getBody(), stringBuilder);
        stringBuilder.append("\n");
        return null;
    }

    private void handleBlockCandidate(StatementTree statementTree, StringBuilder stringBuilder) {
        stringBuilder.append("\n");

        if (!(statementTree instanceof BlockTree)){
            tabIn();
        }

        boolean prevInFunction = this.inFunction;
        this.inFunction = false;
        scan(statementTree, stringBuilder);
        this.inFunction = prevInFunction;

        if (!(statementTree instanceof BlockTree)){
            tabOut();
        }
    }


    private static class ArrayItr<T> implements Iterator<T> {
        private int idx;
        private final T[] arr;

        private ArrayItr(T[] arr) {
            this.arr = arr;
        }

        @Override
        public boolean hasNext() {
            return idx < arr.length;
        }

        @Override
        public T next() {
            return arr[idx++];
        }
    }
    
}
