package com.edel.rulelib.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jitheshrajan on 10/8/18.
 */
@Deprecated
public class Rules {

    List<Rule> rules;

    public Rules() {
        this.rules = new ArrayList<>();
    }

    public void addRule(Rule rule){
        this.rules.add(rule);
    }

    public boolean eval(Map<String, ?> bindings){

        boolean answer = true;

        for(Rule rule : rules){
            boolean evaluation = rule.eval(bindings);
            System.out.println("Rule : "+rule.toString() +" evaluation  : " +evaluation);
            answer = answer && evaluation ;
        }

        return answer;

    }
}
