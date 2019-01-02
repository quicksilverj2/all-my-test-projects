package com.edel.mw.order.edelmwReports.v1.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderStatusDao {

    @SerializedName("isCOSecLeg")
    @Expose
    public String isCOSecLeg;
    @SerializedName("extOrdTim")
    @Expose
    public String extOrdTim;
    @SerializedName("sts")
    @Expose
    public String sts;
    @SerializedName("avgPrc")
    @Expose
    public String avgPrc;
    @SerializedName("exONo")
    @Expose
    public String exONo;
    @SerializedName("flQty")
    @Expose
    public String flQty;
    @SerializedName("nstReqID")
    @Expose
    public String nstReqID;
    @SerializedName("ordTyp")
    @Expose
    public String ordTyp;

    @SerializedName("prdCode")
    @Expose
    public String prdCode;
    @SerializedName("prc")
    @Expose
    public String prc;
    /* @SerializedName("reqQty")
     @Expose
     public String reqQty;*/
    @SerializedName("pdQty")
    @Expose
    public String pdQty;
    @SerializedName("rjRsn")
    @Expose
    public String rjRsn;
    @SerializedName("rcvTim")
    @Expose
    public String rcvTim;
    @SerializedName("ntQty") // have to check this TODO
    @Expose
    public String ntQty;
    @SerializedName("trsTyp")
    @Expose
    public String trsTyp;
    @SerializedName("trgPrc")
    @Expose
    public String trgPrc;

}
