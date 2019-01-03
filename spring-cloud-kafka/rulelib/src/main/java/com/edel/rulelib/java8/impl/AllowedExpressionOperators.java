package com.edel.rulelib.java8.impl;

import com.edel.rulelib.java8.rule.BaseRule;

/**
 * Created by jitheshrajan on 10/15/18.
 */
public enum AllowedExpressionOperators {

    EQUALS("=="){
        @Override
        public String getName() {
            return " EQUALS ";
        }

        @Override
        public EqualsComparisonRuleWithObject getRule(String category, String left, String right, boolean isConstant) {
            return EqualsComparisonRuleWithObject.builder().category(category).isConstant(isConstant).left(left).right(right).build();
        }

//        @Override
//        public EqualsComparisonRuleWithInput getRule(String category, String left) {
//            return EqualsComparisonRuleWithInput.builder().category(category).left(left).build();
//        }
    },
    GREATER_THAN(">"){
        @Override
        public String getName() {
            return " GREATER THAN ";
        }

        @Override
        public GreaterThanComparisonWithObject getRule(String category, String left, String right,  boolean isConstant) {
            return GreaterThanComparisonWithObject.builder().category(category).isConstant(isConstant).left(left).right(right).build();
        }

//        @Override
//        public GreaterThanComparisonWithInput getRule(String category, String left) {
//            return GreaterThanComparisonWithInput.builder().category(category).left(left).build();
//        }
    },
    GREATER_THAN_OR_EQUALS(">="){
        @Override
        public String getName() {
            return " GREATER THAN OR EQUALS ";
        }

        @Override
        public GreaterThanOrEqualsComparisonWithObject getRule(String category, String left, String right,  boolean isConstant) {
            return GreaterThanOrEqualsComparisonWithObject.builder().category(category).isConstant(isConstant).left(left).right(right).build();
        }

//        @Override
//        public GreaterThanOrEqualsComparisonWithInput getRule(String category, String left) {
//            return GreaterThanOrEqualsComparisonWithInput.builder().category(category).left(left).build();
//        }
    },
    LESS_THAN("<"){
        @Override
        public String getName() {
            return " LESS THAN ";
        }

        @Override
        public LesserThanComparisonWithObject getRule(String category, String left, String right,  boolean isConstant) {
            return LesserThanComparisonWithObject.builder().category(category).isConstant(isConstant).left(left).right(right).build();
        }

//        @Override
//        public LesserThanComparisonWithInput getRule(String category, String left) {
//            return LesserThanComparisonWithInput.builder().category(category).left(left).build();
//        }
    },
    LESS_THAN_OR_EQUALS("<="){
        @Override
        public String getName() {
            return " LESS THAN OR EQUALS ";
        }

        @Override
        public LesserThanOrEqualsComparisonWithObject getRule(String category, String left, String right,  boolean isConstant) {
            return LesserThanOrEqualsComparisonWithObject.builder().category(category).isConstant(isConstant).left(left).right(right).build();
        }

//        @Override
//        public LesserThanOrEqualsComparisonWithInput getRule(String category, String left) {
//            return LesserThanOrEqualsComparisonWithInput.builder().category(category).left(left).build();
//        }
    };


    AllowedExpressionOperators(String operatorSymbol){
        this.operatorSymbol = operatorSymbol;
    }


    public abstract String getName();

    public abstract BaseRule getRule(String category, String left, String right, boolean isConstant);

    private String operatorSymbol;

    public String getOperatorSymbol() {
        return operatorSymbol;
    }

    public void setOperatorSymbol(String operatorSymbol) {
        this.operatorSymbol = operatorSymbol;
    }


}
