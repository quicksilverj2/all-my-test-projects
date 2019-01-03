package com.edel.messagebroker.util;

/**
 * Created by jitheshrajan on 1/29/18.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

class Fallback {
    @Getter
    @Setter
    @SerializedName("message")
    @Expose
    private String message;

    @Getter
    @Setter
    @SerializedName("title")
    @Expose
    private String title;

    @Getter
    @Setter
    @SerializedName("richContent")
    @Expose
    private List<RichContent> richContent = new ArrayList<>();

    @Getter
    @Setter
    @SerializedName("additionalActions")
    @Expose
    private List<IosAdditionalAction> additionalActions = new ArrayList<>();
}
