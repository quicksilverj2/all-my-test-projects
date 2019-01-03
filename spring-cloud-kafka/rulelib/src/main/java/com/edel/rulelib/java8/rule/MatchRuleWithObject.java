package com.edel.rulelib.java8.rule;

import com.edel.rulelib.java8.execution.ExecutionEvent;

/**
 * Created by jitheshrajan on 10/12/18.
 */
public interface MatchRuleWithObject<I> extends BaseRule {


    /**
     * Rule interface for comparing two values based on the use case.
     * You may compare two values to be equal or compare both values and return
     * a value.
     *
     * eg 1 : return 10 == 10 ? true : false
     * eg 2 : return 10 - 100
     *
     * Each operator ('=', '<' ...) must be configured in an ENUM
     *
     * @param inputObject
     * @return
     */
    ExecutionEvent matches(I inputObject);


//    /**
//     * This creates a visual representation of the rule which was processed.
//     * @param inputObject
//     * @return
//     */
//    String createMatchRuleString(I inputObject);

}
