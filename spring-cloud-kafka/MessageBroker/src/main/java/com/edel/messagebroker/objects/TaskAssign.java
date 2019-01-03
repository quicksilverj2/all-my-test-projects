package com.edel.messagebroker.objects;

import lombok.Getter;
import lombok.Setter;

public class TaskAssign {

    @Getter @Setter
    private int tskNo;

    public TaskAssign(){

    }

    public TaskAssign(int tskNo){
        this.tskNo = tskNo;
    }
}
