package com.edel.messagebroker.util;

import com.edel.order.EdelOrderUpdateModel;
import com.edel.order.EdelTradeUpdateModel;
import lombok.Getter;
import lombok.Setter;

public class OrderDataPacket {

    @Getter
    @Setter
    private String type;

    @Getter @Setter
    private String accTyp;

    @Getter @Setter
    private String usrId;

    @Getter @Setter
    private String accId;

    @Getter @Setter
    private EdelOrderUpdateModel data;

    public OrderDataPacket(){

    }
}
