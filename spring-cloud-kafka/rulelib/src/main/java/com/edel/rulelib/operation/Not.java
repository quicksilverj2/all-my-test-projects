package com.edel.rulelib.operation;

/**
 * Created by jitheshrajan on 10/8/18.
 */
import com.edel.rulelib.base.Expression;
import com.edel.rulelib.impl.Operation;

import java.util.Map;
import java.util.Stack;
@Deprecated
public class Not extends Operation
{
    public Not()
    {
        super("NOT");
    }

    public Not copy()
    {
        return new Not();
    }

    @Override
    public int parse(String[] tokens, int pos, Stack<Expression> stack)
    {
        int i = findNextExpression(tokens, pos+1, stack);
        Expression right = stack.pop();

        this.rightOperand = right;
        stack.push(this);

        return i;
    }

    @Override
    public boolean interpret(final Map<String, ?> bindings)
    {
        return !this.rightOperand.interpret(bindings);
    }

    @Override
    public boolean interpretGeneric(Map<?, ?> bindings) {
        return false;// todo
    }

    @Override
    public String toString(){
        return  "! "+rightOperand.toString();
    }
}