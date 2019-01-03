package com.edel.rulelib.java8.impl;

import com.edel.rulelib.java8.execution.ExecutionEvent;
import com.edel.rulelib.java8.rule.MatchRuleWithObject;
import com.edel.rulelib.java8.util.ExpressionHelper;
import com.google.gson.JsonObject;
import lombok.*;

import java.lang.reflect.Field;

/**
 * Created by jitheshrajan on 10/12/18.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LesserThanComparisonWithObject implements MatchRuleWithObject {

    @Setter
    private String category;

    @Setter
    private boolean isConstant;

    @Setter
    private String left;

    @Setter
    private String right;


    @Override
    public String getRuleCategory() {
        return category;
    }

    @Override
    public ExecutionEvent matches(Object inputObject) {


        if(inputObject instanceof JsonObject){
            return matchesJson((JsonObject) inputObject);
        }

        ExecutionEvent returnableExecutionEvent =
                ExecutionEvent.builder().outcome(false).detail(inputObject).build();

        Object leftFieldObject;
        Object rightFieldObject;

        System.out.println("Rule comparison started");

        try{
            Field leftField = inputObject.getClass().getDeclaredField(left);
            leftField.setAccessible(true);
            System.out.println(leftField.get(inputObject));
            leftFieldObject = leftField.get(inputObject);

            if(isConstant){
                rightFieldObject = right;
            }else{
                Field rightField = inputObject.getClass().getDeclaredField(right);
                rightField.setAccessible(true);
                System.out.println(rightField.get(inputObject));
                rightFieldObject = rightField.get(inputObject);
            }

            returnableExecutionEvent.setOutcome(ExpressionHelper.compareObjectsForLessThan(leftFieldObject, rightFieldObject));
            returnableExecutionEvent.setRuleString(left+" LESS THAN "+right+" : "+leftFieldObject+" LESS THAN "+rightFieldObject);
            return returnableExecutionEvent;

        } catch (NoSuchFieldException e) {
            System.err.println("couldnt get fields :");
            e.printStackTrace();
            return returnableExecutionEvent;
        } catch (IllegalAccessException e) {
            System.err.println("illegal argument exception :");
            e.printStackTrace();
            return returnableExecutionEvent;
        } catch (Exception e){
            System.err.println("Error while processing the rule!");
            e.printStackTrace();
            return returnableExecutionEvent;
        }


    }

    public <T extends JsonObject> ExecutionEvent matchesJson(T inputObject){
        ExecutionEvent returnableExecutionEvent =
                ExecutionEvent.builder().outcome(false).detail(inputObject).build();
        try{
            Object leftFieldObject;
            Object rightFieldObject;
            leftFieldObject = inputObject.get(left).getAsString();

            if(isConstant){
                rightFieldObject = right;
            }else{
                rightFieldObject = inputObject.get(right).getAsString();
            }

            returnableExecutionEvent.setOutcome(ExpressionHelper.compareObjectsForLessThan(leftFieldObject, rightFieldObject));
            returnableExecutionEvent.setRuleString(left+" LESS THAN "+right+" : "+leftFieldObject+" LESS THAN "+rightFieldObject);
            return returnableExecutionEvent;


        }catch (Exception e){
            System.err.println("Error while processing the rule!");
            e.printStackTrace();
            return returnableExecutionEvent;
        }

    }




    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EqualsComparisonRuleWithObject{");
        sb.append("category='").append(category).append('\'');
        sb.append(", left='").append(left).append('\'');
        sb.append(", right='").append(right).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
