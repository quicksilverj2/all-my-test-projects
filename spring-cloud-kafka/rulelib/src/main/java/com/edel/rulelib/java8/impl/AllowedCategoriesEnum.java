package com.edel.rulelib.java8.impl;

public enum AllowedCategoriesEnum {

    // have to convert this into a singleton enum pattern where a new item can be
    // inserted dynamically.

    SCREENER_GROUP {
        @Override
        public String getName() {
            return "SCREENER_GROUP";
        }
    };

    public abstract String getName();
}
