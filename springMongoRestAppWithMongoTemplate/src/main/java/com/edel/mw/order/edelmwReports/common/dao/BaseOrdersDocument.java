package com.edel.mw.order.edelmwReports.common.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by jitheshrajan on 7/2/18.
 */
public class BaseOrdersDocument<T> {

    @SerializedName("_id")
    @Expose @Getter @Setter
    public String id;

    @SerializedName("type")
    @Expose @Getter @Setter
    public String type;

    @SerializedName("accTyp")
    @Expose @Getter @Setter
    public String accTyp;

    @SerializedName("accId")
    @Expose @Getter @Setter
    public String accId;

    @SerializedName("usrId")
    @Expose @Getter @Setter
    public String usrId;

    @SerializedName("data")
    @Expose @Getter @Setter
    public T data;
}
