package com.edel.rulelib.java8.util;

import com.edel.rulelib.java8.RuleStore;
import com.edel.rulelib.java8.dao.BaseExpression;
import com.edel.rulelib.java8.impl.AllowedExpressionOperators;
import com.edel.rulelib.java8.rule.BaseRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jitheshrajan on 10/15/18.
 */
public class ExpressionHelper {


    public static Boolean compareObjectsForEquals(Object leftField, Object rightField) {


        if(leftField instanceof Number
                && rightField instanceof Number){

//            System.out.println(((Number) leftField).floatValue());
//            System.out.println(((Number) rightField).floatValue());

            return ((Number)leftField).floatValue()
                    == ((Number)rightField).floatValue();

        }else{

            String leftFieldString;
            String rightFieldString;


            if(!(leftField instanceof String)){
                leftFieldString = leftField + "";
            }else{
                leftFieldString = (String) leftField;
            }

            if(!(rightField instanceof String)){
                rightFieldString = rightField + "";
            }else{
                rightFieldString = (String) rightField;
            }

            if(rightFieldString.contains(".") || leftFieldString.contains(".")){
                return Float.parseFloat(leftFieldString) == Float.parseFloat(rightFieldString);
            }else if((leftFieldString ).compareTo(rightFieldString) == 0){
                return true;
            }
        }

        return false;
    }

    public static Boolean compareObjectsForGreaterThan(Object leftField, Object rightField) {

        System.out.println("Comparing : "+leftField +" and for gt "+ rightField);

        if(leftField instanceof Number
                && rightField instanceof Number){

            return ((Number)leftField).floatValue()
                    > ((Number)rightField).floatValue();

        }else{

            String leftFieldString;
            String rightFieldString;


            if(!(leftField instanceof String)){
                leftFieldString = leftField + "";
            }else{
                leftFieldString = (String) leftField;
            }

            if(!(rightField instanceof String)){
                rightFieldString = rightField + "";
            }else{
                rightFieldString = (String) rightField;
            }

            System.out.println("Comparing : "+leftFieldString +" and for gt "+ rightFieldString);

            if(rightFieldString.contains(".") || leftFieldString.contains(".")){
                return Float.parseFloat(leftFieldString) > Float.parseFloat(rightFieldString);
            }else if((leftFieldString ).compareTo(rightFieldString) > 0){
                return false;
            }else if((leftFieldString ).compareTo(rightFieldString) < 0){
                return true;
            }else{
                return false;
            }
        }

//        return false;
    }

    public static Boolean compareObjectsForLessThan(Object leftField, Object rightField) {
        if(leftField instanceof Number
                && rightField instanceof Number){

            return ((Number)leftField).floatValue()
                    < ((Number)rightField).floatValue();

        }else{

            String leftFieldString;
            String rightFieldString;


            if(!(leftField instanceof String)){
                leftFieldString = leftField + "";
            }else{
                leftFieldString = (String) leftField;
            }

            if(!(rightField instanceof String)){
                rightFieldString = rightField + "";
            }else{
                rightFieldString = (String) rightField;
            }

            if(rightFieldString.contains(".") || leftFieldString.contains(".")){
                return Float.parseFloat(leftFieldString) < Float.parseFloat(rightFieldString);
            }else if(( leftFieldString ).compareTo(rightFieldString) > 0){
                return true;
            }else if(( leftFieldString ).compareTo(rightFieldString) < 0){
                return false;
            }else{
                return false;
            }
        }

//        return false;
    }

    public static Boolean compareObjectsForLessThanOrEquals(Object leftField, Object rightField) {
        if(leftField instanceof Number
                && rightField instanceof Number){

            return ((Number)leftField).floatValue()
                    <= ((Number)rightField).floatValue();

        }else{

            String leftFieldString;
            String rightFieldString;


            if(!(leftField instanceof String)){
                leftFieldString = leftField + "";
            }else{
                leftFieldString = (String) leftField;
            }

            if(!(rightField instanceof String)){
                rightFieldString = rightField + "";
            }else{
                rightFieldString = (String) rightField;
            }

            if(rightFieldString.contains(".") || leftFieldString.contains(".")){
                return Float.parseFloat(leftFieldString) <= Float.parseFloat(rightFieldString);
            }else if((leftFieldString ).compareTo(rightFieldString) > 0){
                return true;
            }else if((leftFieldString ).compareTo(rightFieldString) < 0){
                return false;
            }else{
                return true;
            }
        }

//        return false;
    }


    public static Boolean compareObjectsForGreaterThanOrEquals(Object leftField, Object rightField) {

        System.out.println("Comparing : "+leftField +" and for gt "+ rightField);

        if(leftField instanceof Number
                && rightField instanceof Number){

            return ((Number)leftField).floatValue()
                    >= ((Number)rightField).floatValue();

        }else{

            String leftFieldString;
            String rightFieldString;


            if(!(leftField instanceof String)){
                leftFieldString = leftField + "";
            }else{
                leftFieldString = (String) leftField;
            }

            if(!(rightField instanceof String)){
                rightFieldString = rightField + "";
            }else{
                rightFieldString = (String) rightField;
            }

            System.out.println("Comparing : "+leftFieldString +" and for gt "+ rightFieldString);

            if(rightFieldString.contains(".") || leftFieldString.contains(".")){
                return Float.parseFloat(leftFieldString) >= Float.parseFloat(rightFieldString);
            }else if((leftFieldString ).compareTo(rightFieldString) > 0){
                return false;
            }else if((leftFieldString ).compareTo(rightFieldString) < 0){
                return true;
            }else{
                return false;
            }
        }

//        return false;
    }


    public static boolean loadRules(List<BaseExpression> expressionDaos){
        List<BaseRule> rules = new ArrayList<>();

        for(BaseExpression expressionDao : expressionDaos) {

            BaseRule baseRule = null;
            baseRule = AllowedExpressionOperators.valueOf(expressionDao.getOper())
                    .getRule(expressionDao.getCategory(),
                            expressionDao.getLeftParam(),
                            expressionDao.getRightParam(),
                            expressionDao.isCnst());

            rules.add(baseRule);
        }

        return RuleStore.getInstance().reloadRules(rules);

    }
}
