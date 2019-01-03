package com.edel.rulelib.java8.dao;

import lombok.*;

/**
 * Created by jitheshrajan on 10/22/18.
 */
@NoArgsConstructor
@AllArgsConstructor
public class BaseExpression<T> {
    @Getter
    @Setter
    private String category;

    @Getter @Setter
    private String oper;

    @Getter @Setter
    private String leftParam;

    @Getter @Setter
    private boolean cnst;

    @Getter @Setter
    private String rightParam;


    /**
     * This object is purely for reference. Will have to decide if this is needed!
     */
    private T obj;
}
