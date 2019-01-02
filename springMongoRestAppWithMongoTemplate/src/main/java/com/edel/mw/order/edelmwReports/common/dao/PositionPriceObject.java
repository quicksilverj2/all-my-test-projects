package com.edel.mw.order.edelmwReports.common.dao;

import lombok.Getter;
import lombok.Setter;

public class PositionPriceObject {

    @Getter @Setter
    private Double avgBuyPrice = 0.00d;
    @Getter @Setter
    private Double avgSellPrice = 0.00d;
    @Getter @Setter
    private Long sellQty = 0L;
    @Getter @Setter
    private Long buyQty = 0L;

    @Getter
    @Setter
    private String type;
}
