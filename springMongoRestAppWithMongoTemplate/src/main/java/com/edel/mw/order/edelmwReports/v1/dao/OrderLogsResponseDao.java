package com.edel.mw.order.edelmwReports.v1.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderLogsResponseDao {

    @SerializedName("sym")
    @Expose
    public String sym;

    @SerializedName("dpName")
    @Expose
    public String dpName;

    @SerializedName("asTyp")
    @Expose
    public String asTyp;

    @SerializedName("cpName")
    @Expose
    public String cpName;

    @SerializedName("dpExpDt")
    @Expose
    public String dpExpDt;

    @SerializedName("dpInsTyp")
    @Expose
    public String dpInsTyp;

    @SerializedName("dsQty")
    @Expose
    public String dsQty;

    @SerializedName("desc")
    @Expose
    public String desc;

    @SerializedName("dur")
    @Expose
    public String dur;

    @SerializedName("exc")
    @Expose
    public String exc;

    @SerializedName("ltSz")
    @Expose
    public String ltSz;

    @SerializedName("ordID")
    @Expose
    public String ordID;

    @SerializedName("ogt")
    @Expose
    public String ogt;

    @SerializedName("opTyp")
    @Expose
    public String opTyp;

    @SerializedName("pcktTyp")
    @Expose
    public String pcktTyp;

    @SerializedName("srs")
    @Expose
    public String srs;

    @SerializedName("stkPrc")
    @Expose
    public String stkPrc;

    @SerializedName("trdSym")
    @Expose
    public String trdSym;

    @SerializedName("tkSz")
    @Expose
    public String tkSz;

    @SerializedName("userCmnt")
    @Expose
    private String userCmnt;

    @SerializedName("rcvDt")
    @Expose
    public String rcvDt;

    @SerializedName("ordStsLst")
    @Expose
    public List<OrderStatusDao> ordStsLst = null;

}
