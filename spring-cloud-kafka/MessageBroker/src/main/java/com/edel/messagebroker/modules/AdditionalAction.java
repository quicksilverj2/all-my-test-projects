package com.edel.messagebroker.modules;

/**
 * Created by jitheshrajan on 1/28/18.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

class AdditionalAction {
    @Getter
    @Setter
    @SerializedName("type")
    @Expose
    private String type;

    @Getter
    @Setter
    @SerializedName("value")
    @Expose
    private String value;

    @Getter
    @Setter
    @SerializedName("name")
    @Expose
    private String name;

    @Getter
    @Setter
    @SerializedName("iconURL")
    @Expose
    private String iconURL;
}
