package com.edel.rulelib.java8.execution;

import com.edel.rulelib.java8.RuleStore;
import com.edel.rulelib.java8.rule.MatchRuleWithObject;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jitheshrajan on 10/11/18.
 */
public class RulesProcessor {


    // TODO make this object specific to our case
    public static <T extends Object> List<ExecutionEvent> processMatches(T object, String category){


        // todo to avoid so many casting, we can define separate methods for matching and processing rules!
        // will have to store them separately inside the rule list!

        return RuleStore.getInstance()
                .getRulesForCategory(category)
                .stream()
                .filter(rule -> rule instanceof MatchRuleWithObject)
                .map(baseRule -> ((MatchRuleWithObject)baseRule).matches(object))
                .filter(ExecutionEvent::isOutcome) // filtering out only success ones
                .collect(Collectors.toList());


    }
}
