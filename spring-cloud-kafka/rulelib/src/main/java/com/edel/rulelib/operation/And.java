package com.edel.rulelib.operation;

/**
 * Created by jitheshrajan on 10/8/18.
 */
import com.edel.rulelib.base.Expression;
import com.edel.rulelib.impl.Operation;

import java.util.Map;
import java.util.Stack;

@Deprecated
public class And extends Operation
{
    public And()
    {
        super("AND");
    }

    public And copy()
    {
        return new And();
    }

    @Override
    public int parse(String[] tokens, int pos, Stack<Expression> stack)
    {
        Expression left = stack.pop();
        int i = findNextExpression(tokens, pos+1, stack);
        Expression right = stack.pop();

        this.leftOperand = left;
        this.rightOperand = right;

        stack.push(this);

        return i;
    }

    @Override
    public boolean interpret(Map<String, ?> bindings)
    {
        // this is where the  actual operation is happening! where && is used!
        return leftOperand.interpret(bindings) && rightOperand.interpret(bindings);
    }

    @Override
    public boolean interpretGeneric(Map<?, ?> bindings) {
        return leftOperand.interpretGeneric(bindings) && rightOperand.interpretGeneric(bindings);
    }

    @Override
    public String toString(){
        return leftOperand.toString() +" AND " + rightOperand.toString();
    }
}