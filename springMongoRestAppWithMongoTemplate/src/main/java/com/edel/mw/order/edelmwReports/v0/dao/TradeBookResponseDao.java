package com.edel.mw.order.edelmwReports.v0.dao;

import com.edel.order.EdelTradeUpdateModel;

import java.util.List;

/**
 * Created by jitheshrajan on 7/5/18.
 */
public class TradeBookResponseDao extends EdelTradeUpdateModel {

    private List<TradeBookFillDetailDao> flDtlLst;


    public List<TradeBookFillDetailDao> getFlDtlLst() {
        return flDtlLst;
    }

    public void setFlDtlLst(List<TradeBookFillDetailDao> flDtlLst) {
        this.flDtlLst = flDtlLst;
    }
}
