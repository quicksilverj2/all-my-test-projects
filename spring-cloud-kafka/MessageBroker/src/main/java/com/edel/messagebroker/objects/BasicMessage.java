package com.edel.messagebroker.objects;

import lombok.Getter;
import lombok.Setter;

public class BasicMessage {
    @Getter @Setter
    private String msg;

    public BasicMessage(){

    }

    public BasicMessage(String msg){
        this.msg = msg;
    }
}
