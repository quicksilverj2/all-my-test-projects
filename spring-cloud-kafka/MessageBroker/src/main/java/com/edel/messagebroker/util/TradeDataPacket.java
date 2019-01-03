package com.edel.messagebroker.util;

import com.edel.order.EdelTradeUpdateModel;
import lombok.Getter;
import lombok.Setter;

public class TradeDataPacket {

    @Getter @Setter
    private String type;

    @Getter @Setter
    private String accTyp;

    @Getter @Setter
    private String usrId;

    @Getter @Setter
    private String accId;

    @Getter @Setter
    private EdelTradeUpdateModel data;

    public TradeDataPacket(){

    }
}
