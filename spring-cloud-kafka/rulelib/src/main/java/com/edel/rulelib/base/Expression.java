package com.edel.rulelib.base;

/**
 * Created by jitheshrajan on 10/8/18.
 */

import java.util.Map;
@Deprecated
public interface Expression
{
    boolean interpret(final Map<String, ?> bindings);

    boolean interpretGeneric(final Map<?, ?> bindings);
}