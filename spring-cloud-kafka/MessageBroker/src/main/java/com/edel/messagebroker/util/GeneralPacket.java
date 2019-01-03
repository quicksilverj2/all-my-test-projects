package com.edel.messagebroker.util;

import lombok.Getter;
import lombok.Setter;

public class GeneralPacket {

    @Getter
    @Setter
    private String type;

    public GeneralPacket(){
        super();
    }
}
