package com.edel.rulelib.base;

/**
 * Created by jitheshrajan on 10/8/18.
 */
import com.edel.rulelib.impl.NullActionDispatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Deprecated
public class Rule
{
    private List<Expression> expressions;
    private ActionDispatcher dispatcher;

    public static class Builder
    {
        private List<Expression> expressions = new ArrayList<>();
        private ActionDispatcher dispatcher = new NullActionDispatcher();

        public Builder withExpression(Expression expr)
        {
            expressions.add(expr);
            return this;
        }

        public Builder withDispatcher(ActionDispatcher dispatcher)
        {
            this.dispatcher = dispatcher;
            return this;
        }

        public Rule build()
        {
            return new Rule(expressions, dispatcher);
        }
    }

    private Rule(List<Expression> expressions, ActionDispatcher dispatcher)
    {
        this.expressions = expressions;
        this.dispatcher = dispatcher;
    }

    public boolean eval(Map<String, ?> bindings)
    {
        boolean eval = false;
        for (Expression expression : expressions)
        {
            eval = expression.interpret(bindings);
            if (eval)
                dispatcher.fire();
        }
        return eval;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(Expression exp : expressions){
            sb.append(exp.toString()).append(" & ");
        }
        String returnString = sb.toString();
        return returnString.substring(0,returnString.length() - 3)+ " " +dispatcher;
    }
}
