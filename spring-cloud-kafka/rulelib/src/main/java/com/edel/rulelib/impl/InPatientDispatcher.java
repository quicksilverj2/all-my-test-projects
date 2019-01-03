package com.edel.rulelib.impl;

import com.edel.rulelib.base.ActionDispatcher;

/**
 * Created by jitheshrajan on 10/8/18.
 */
@Deprecated
public class InPatientDispatcher implements ActionDispatcher
{
    @Override
    public void fire()
    {
        // send patient to in_patient
        System.out.println("Send patient to IN");
    }
}