package com.edel.rulelib.java8.test;

import com.edel.rulelib.java8.RuleStore;
import com.edel.rulelib.java8.execution.RulesProcessor;
import com.edel.rulelib.java8.impl.EqualsComparisonRuleWithInput;
import com.edel.rulelib.java8.impl.EqualsComparisonRuleWithObject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jitheshrajan on 10/11/18.
 */
public class TestRules {

    public static void main(String[] args) {

        System.out.println("Starting Test rule application");

        // the operator will be selected based on the operator used!


        EqualsComparisonRuleWithInput equalsComparisonRule = new EqualsComparisonRuleWithInput();
        equalsComparisonRule.setCategory("BASIC");
        equalsComparisonRule.setLeft(100.22);

        RuleStore.getInstance().loadRule(equalsComparisonRule);


        RulesProcessor rulesProcessor = new RulesProcessor();

        List<Object> objectList = Arrays.asList(100.22, "jithesh", 0, BigDecimal.ONE);

//        for(Object object : objectList){
//            System.out.println(rulesProcessor.processMatches(object,"BASIC"));
//        }


        List<TempClass> tempClassList = new ArrayList<>();
        tempClassList.add( new TempClass("hellow","world"));
        tempClassList.add( new TempClass("something","something"));
        tempClassList.add( new TempClass(10.00,10.0));
        tempClassList.add( new TempClass(100,10));
        tempClassList.add( new TempClass(true,true));


        EqualsComparisonRuleWithObject equalsComparisonRuleWithObject = EqualsComparisonRuleWithObject.builder()
                .category("QuoteCompare")
                .left("some1")
                .right("some2").build();



        RuleStore.getInstance().loadRule(equalsComparisonRuleWithObject);

        for(Object object : tempClassList){
            System.out.println(rulesProcessor.processMatches(object, "QuoteCompare"));
        }








    }

    @NoArgsConstructor
    @AllArgsConstructor
    static class TempClass {
        private Object some1;
        private Object some2;


    }
}
