package com.edel.rulelib.java8.execution;

import lombok.*;

/**
 * Created by jitheshrajan on 10/11/18.
 */
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExecutionEvent {


    @Getter @Setter
    boolean outcome;

    @Getter @Setter
    String ruleString; // TODO crete a string for the rule!

    @Getter @Setter
    Object detail;


}
