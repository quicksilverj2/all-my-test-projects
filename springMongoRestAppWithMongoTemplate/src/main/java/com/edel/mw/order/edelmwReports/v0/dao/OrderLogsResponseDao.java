package com.edel.mw.order.edelmwReports.v0.dao;

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
    @SerializedName("aTyp")
    @Expose
    public String aTyp;
    @SerializedName("cName")
    @Expose
    public String cName;
    @SerializedName("dExpDt")
    @Expose
    public String dExpDt;
    @SerializedName("dInsTyp")
    @Expose
    public String dInsTyp;
    @SerializedName("dQty")
    @Expose
    public String dQty;
    @SerializedName("desc")
    @Expose
    public String desc;
    @SerializedName("dur")
    @Expose
    public String dur;
    @SerializedName("exc")
    @Expose
    public String exc;
    @SerializedName("lsz")
    @Expose
    public String lsz;
    @SerializedName("oID")
    @Expose
    public String oID;
    @SerializedName("ogt")
    @Expose
    public String ogt;
    @SerializedName("opTyp")
    @Expose
    public String opTyp;
    @SerializedName("pTyp")
    @Expose
    public String pTyp;
    @SerializedName("srs")
    @Expose
    public String srs;
    @SerializedName("stkPrc")
    @Expose
    public String stkPrc;
    @SerializedName("tSym")
    @Expose
    public String tSym;
    @SerializedName("tsz")
    @Expose
    public String tsz;
    @SerializedName("rcvDt")
    @Expose
    public String rcvDt;
    @SerializedName("ordStsLst")
    @Expose
    public List<OrderStatusDao> ordStsLst = null;

}
