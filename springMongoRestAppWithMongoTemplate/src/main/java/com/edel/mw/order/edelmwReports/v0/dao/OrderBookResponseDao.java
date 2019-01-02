package com.edel.mw.order.edelmwReports.v0.dao;

import com.edel.order.EdelOrderUpdateModel;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by jitheshrajan on 7/2/18.
 */

public class OrderBookResponseDao extends EdelOrderUpdateModel {

    @Expose
    @Getter
    @Setter
    public String accId;
}
