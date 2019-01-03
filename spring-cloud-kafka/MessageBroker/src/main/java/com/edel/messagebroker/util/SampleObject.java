package com.edel.messagebroker.util;

import lombok.Getter;
import lombok.Setter;

public class SampleObject {

    @Getter
    @Setter
    private String type;
    @Getter
    @Setter
    private String accTyp;
    @Getter
    @Setter
    private String accId;
    @Getter
    @Setter
    private String usrId;

    private Object data;

}
