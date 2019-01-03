package com.edel.messagebroker.util;

/**
 * Created by jitheshrajan on 1/28/18.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

class RichContent {
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
}
