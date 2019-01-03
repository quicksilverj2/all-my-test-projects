package com.edel.messagebroker.modules;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by jitheshrajan on 1/30/18.
 */
public class TargetUserAttributes {

    @Getter @Setter @Expose
    private String attribute;

    @Getter @Setter @Expose
    private String comparisonParameter;

    @Getter @Setter @Expose
    private String attributeValue;
}
