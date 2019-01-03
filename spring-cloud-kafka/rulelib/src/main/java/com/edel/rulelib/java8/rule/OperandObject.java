package com.edel.rulelib.java8.rule;

public class OperandObject {


    private String type;
    private String fieldString;


    public String getType() {
        return type;
    }

    public OperandObject() {
    }

    public OperandObject setType(String type) {
        this.type = type;
        return this;
    }

    public String getFieldString() {
        return fieldString;
    }

    public OperandObject setFieldString(String fieldString) {
        this.fieldString = fieldString;
        return this;
    }
}
