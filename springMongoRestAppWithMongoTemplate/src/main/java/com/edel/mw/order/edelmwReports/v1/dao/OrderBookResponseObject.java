package com.edel.mw.order.edelmwReports.v1.dao;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class OrderBookResponseObject {

    @Setter
    @Getter
    private String stkPrc;

    @Setter
    @Getter
    private String isCOSecLeg;

    @Setter
    @Getter
    private String dur;

    @Setter
    @Getter
    private String vlDt;

    @Setter
    @Getter
    private String rcvTim;

    @Setter
    @Getter
    private String rcvEpTim;

    @Setter
    @Getter
    private String sym;

    @Setter
    @Getter
    private String cpName;

    @Setter
    @Getter
    private String exit;

    @Getter
    @Setter
    private String syomID;

    @Setter
    @Getter
    private String exc;

    @Setter
    @Getter
    private String ntQty;

    @Setter
    @Getter
    private String dpName;

    @Setter
    @Getter
    private String cancel;

    @Setter
    @Getter
    private String sipID;

    @Setter
    @Getter
    private String nstReqID;

    @Setter
    @Getter
    private String ordTyp;

    @Setter
    @Getter
    private String qtyUnits;

    @Setter
    @Getter
    private String opTyp;

    @Setter
    @Getter
    private String trsTyp;

    @Setter
    @Getter
    private String srs;

    @Setter
    @Getter
    private String prdCode;

    @Setter
    @Getter
    private String ogt;

    @Setter
    @Getter
    private String flQty;

    @Setter
    @Getter
    private String trdSym;

    @Setter
    @Getter
    private String edit;

    @Setter
    @Getter
    private String asTyp;

    @Setter
    @Getter
    private String trgPrc;

    @Setter
    @Getter
    private String avgPrc;

    @Setter
    @Getter
    private String dsQty;

    @Setter
    @Getter
    private String ordID;

    @Setter
    @Getter
    private String sts;

    @Setter
    @Getter
    private String dpInsTyp;

    @Setter
    @Getter
    private String rjRsn;

    @Setter
    @Getter
    private String userID;

    @Setter
    @Getter
    private String dpExpDt;

    @Setter
    @Getter
    private String ltSz;

    @Setter
    @Getter
    private String tkSz;

    @Setter
    @Getter
    private String desc;

    @Setter
    @Getter
    private String prc;

    @Setter
    @Getter
    private String exONo;

    @Setter
    @Getter
    private String exp;
    @Setter
    @Getter
    private String rcvDt;
    @Setter
    @Getter
    private String pdQty;

    @Getter
    @Setter
    private String userCmnt;

    @Getter
    @Setter
    private String boSeqId;

    @Getter
    @Setter
    private List<TradeFillItem> flDtlLst;


}
