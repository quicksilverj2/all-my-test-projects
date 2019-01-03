package com.edel.messagebroker.modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Iqbal on 2018-04-12.
 */
class IosAdditionalAction {
    @Getter
    @Setter
    @SerializedName("category")
    @Expose
    private String category;
}
