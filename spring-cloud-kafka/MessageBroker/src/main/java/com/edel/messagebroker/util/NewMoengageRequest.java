package com.edel.messagebroker.util;

import com.edel.messagebroker.modules.*;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Iqbal on 2018-04-05.
 */

@ApiModel(value = "Request body for individual platform")
public class NewMoengageRequest {
    @Getter
    @Setter
    @SerializedName("campaignName")
    @Expose
    @ApiModelProperty(value = "field may be omitted.")
    private String campaignName = "";

    @Getter
    @Setter
    @SerializedName("platform")
    @Expose
    @ApiModelProperty(value = "PLATFORM to be used for communication", allowableValues = "ANDROID,IOS")
    private String platform;

    @Getter
    @Setter
    @SerializedName("targetAudience")
    @Expose
    @ApiModelProperty(value = "Only value of first element in the array is considered.", allowableValues = "User,All Users")
    private String targetAudience;

    @Getter
    @Setter
    @SerializedName("targetUserAttributes")
    @Expose
    @ApiModelProperty(value = "Only value of first element in the array is considered. May be omitted if targetAudience = All Users", allowableValues = "User,All Users")
    private TargetUserAttributes targetUserAttributes;

    @Getter
    @Setter
    @SerializedName("customSegmentName")
    @Expose
    @ApiModelProperty(value = "Only value of first element in the array is considered.")
    private String customSegmentName;

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
    @SerializedName("summary")
    @Expose
    private String summary;

    @Getter
    @Setter
    @SerializedName("subtitle")
    @Expose
    private String subtitle;

    @Getter
    @Setter
    @SerializedName("richContent")
    @Expose
    private List<RichContent> richContent = new ArrayList<>();

    @Getter
    @Setter
    @SerializedName("defaultAction")
    @Expose
    private DefaultAction defaultAction;

    @Getter
    @Setter
    @SerializedName("additionalActions")
    @Expose
    private List<AdditionalAction> additionalActions = new ArrayList<>();

    @Getter
    @Setter
    @SerializedName("iosAdditionalActions")
    @Expose
    @ApiModelProperty(value = "additionalActions for IOS")
    private List<IosAdditionalAction> iosAdditionalActions = new ArrayList<>();

    @Getter
    @Setter
    @SerializedName("fallback")
    @Expose
    @ApiModelProperty(value = "Only for IOS")
    private ArrayList<Fallback> fallbacks = new ArrayList<>();
}
