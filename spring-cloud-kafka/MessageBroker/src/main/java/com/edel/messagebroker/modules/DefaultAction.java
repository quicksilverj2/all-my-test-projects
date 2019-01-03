package com.edel.messagebroker.modules;

/**
 * Created by jitheshrajan on 1/28/18.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.HashMap;

public class DefaultAction {

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

    @SerializedName("kvPairs")
    @Getter
    @Setter
    @Expose
    private HashMap<String, String> kvPairs;
}
