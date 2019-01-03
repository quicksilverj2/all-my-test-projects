package com.edel.rulelib.operation;

import com.edel.rulelib.base.BaseType;
import com.edel.rulelib.base.Expression;
import com.edel.rulelib.impl.Operation;
import com.edel.rulelib.impl.Variable;

import java.util.Map;
import java.util.Stack;

/**
 * Created by jitheshrajan on 10/10/18.
 */
@Deprecated
public class GreaterThan extends Operation {

    public GreaterThan()
    {
        super(">");
    }

    public GreaterThan copy()
    {
        return new GreaterThan();
    }

    @Override
    public int parse(final String[] tokens, int pos, Stack<Expression> stack)
    {
        if (pos-1 >= 0 && tokens.length >= pos+1)
        {
//            String var = tokens[pos-1];

            this.leftOperand = BaseType.getBaseType(tokens[pos-1]);
            this.rightOperand = BaseType.getBaseType(tokens[pos+1]);
            stack.push(this);

            return pos+1;
        }
        throw new IllegalArgumentException("Cannot assign value to variable");
    }

    @Override
    public boolean interpret(Map<String, ?> bindings)
    {

        Variable v = (Variable)this.leftOperand;
        Object obj = bindings.get(v.getName());
        if (obj == null)
            return false;

        BaseType<?> type = (BaseType<?>)this.rightOperand;
        if (type.getType().equals(obj.getClass()))
        {



            if (type.getValue().equals(obj))
                return true;
        }
        return false;
    }

    @Override
    public boolean interpretGeneric(Map<?, ?> bindings) {
        return false; //todo
    }

    @Override
    public String toString(){
        return leftOperand.toString() +" > " + rightOperand.toString();
    }
}
