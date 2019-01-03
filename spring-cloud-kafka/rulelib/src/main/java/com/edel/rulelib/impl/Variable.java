package com.edel.rulelib.impl;

import com.edel.rulelib.base.Expression;

import java.util.Map;

/**
 * Created by jitheshrajan on 10/8/18.
 */
@Deprecated
public class Variable implements Expression
{
    private String name;

    public Variable(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    @Override
    public boolean interpret(Map<String, ?> bindings)
    {
        return true;
    }

    @Override
    public boolean interpretGeneric(Map<?, ?> bindings) {
        return false;
    }

    @Override
    public String toString(){
        return name;
    }
}
