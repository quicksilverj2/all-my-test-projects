package com.edel.rulelib.java8.rule;

/**
 * Created by jitheshrajan on 10/11/18.
 */
public interface MatchRuleWithInput<I> extends BaseRule{

    /**
     * Generic method which has to be implemented by rules which take in an input
     * and responds with a boolean value.
     *
     * For example comparing two values like
     * a >= b
     *
     * @param input
     * @return
     */
    boolean matches(I input);


}
