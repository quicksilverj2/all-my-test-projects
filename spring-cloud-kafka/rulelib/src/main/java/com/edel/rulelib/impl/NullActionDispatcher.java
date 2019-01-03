package com.edel.rulelib.impl;

import com.edel.rulelib.base.ActionDispatcher;

/**
 * Created by jitheshrajan on 10/8/18.
 */
@Deprecated
public class NullActionDispatcher implements ActionDispatcher {
    @Override
    public void fire() {
        System.out.println("No action could be taken here!");
    }
}
