package com.edel.messagebroker.objects;

import lombok.Getter;
import lombok.Setter;

public class Message<T> {

    @Getter @Setter
    private String typ;

    @Getter @Setter
    private T data;

    @Getter @Setter
    private String exc;

    public Message(){

    }
}
