package com.edel.rulelib.java8.impl;

import com.edel.rulelib.java8.rule.MatchRuleWithInput;
import com.edel.rulelib.java8.util.ExpressionHelper;
import lombok.*;

/**
 * Created by jitheshrajan on 10/11/18.
 */
@Deprecated
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EqualsComparisonRuleWithInput implements MatchRuleWithInput {


    @Setter
    Object left;

    @Setter
    String category;

    @Override
    public String getRuleCategory() {
        return category;
    }

    @Override
    public boolean matches(Object input) {
        return ExpressionHelper.compareObjectsForEquals(left, input);
    }

}
