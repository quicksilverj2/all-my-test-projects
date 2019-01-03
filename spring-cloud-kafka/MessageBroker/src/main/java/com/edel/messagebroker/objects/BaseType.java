package com.edel.messagebroker.objects;

import lombok.Getter;
import lombok.Setter;

public class BaseType {

    @Getter @Setter
    private String typ;

    @Getter @Setter
    private String exc;

    public BaseType(){

    }
}
