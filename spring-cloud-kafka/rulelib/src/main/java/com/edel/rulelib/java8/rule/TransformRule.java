package com.edel.rulelib.java8.rule;

/**
 * Created by jitheshrajan on 10/11/18.
 */
public interface TransformRule<I, O> extends BaseRule {

    /**
     * Generic method which has to be implemented by rules which take in an input and transform
     * it into an object of type Output.
     *
     * For example transforming a lowercase string into uppercase like
     *
     * input --> INPUT
     *
     * @param input
     * @return
     */
    O process(I input);
}
