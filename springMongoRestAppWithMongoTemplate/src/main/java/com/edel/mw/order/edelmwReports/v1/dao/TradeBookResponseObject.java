package com.edel.mw.order.edelmwReports.v1.dao;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class TradeBookResponseObject {
    @Getter
    @Setter
    private String trdSym;
    @Getter
    @Setter
    private String exONo;
    @Getter
    @Setter
    private String dpInsTyp;
    @Getter
    @Setter
    private String ntPrc;
    @Getter
    @Setter
    private String rmk;
    @Getter
    @Setter
    private String dpExpDt;
    @Getter
    @Setter
    private String ltSz;
    @Getter
    @Setter
    private String cpName;
    @Getter
    @Setter
    private String prdCode;

    @Getter
    @Setter
    private String psCnv;
    @Getter
    @Setter
    private String qty;
    @Getter
    @Setter
    private String rjRsn;
    @Getter
    @Setter
    private String opTyp;
    @Getter
    @Setter
    private String sym;
    @Getter
    @Setter
    private String dpName;
    @Getter
    @Setter
    private String ordID;
    @Getter
    @Setter
    private String trsTyp;
    @Getter
    @Setter
    private String stkPrc;
    @Getter
    @Setter
    private String exc;
    @Getter
    @Setter
    private String chgP;
    @Getter
    @Setter
    private String sts;
    @Getter
    @Setter
    private String asTyp;
    @Getter
    @Setter
    private String tkSz;
    @Getter
    @Setter
    private String ordType;
    @Getter
    @Setter
    private String trdID;
    @Getter
    @Setter
    private String ltp;

    @Getter
    @Setter
    private String chg;
    @Getter
    @Setter
    private String srs;
    @Getter
    @Setter
    private String rcvTim;
    @Getter
    @Setter
    private String nstReqID;

    @Getter
    @Setter
    private List<TradeFillItem> flDtlLst;

    @Getter
    @Setter
    private String exp;
    @Getter
    @Setter
    private String qtyUnits;
}
