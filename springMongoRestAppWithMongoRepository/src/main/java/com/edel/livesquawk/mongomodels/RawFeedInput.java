package com.edel.livesquawk.mongomodels;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Iqbal on 5/14/2018.
 */

@Document(collection="RawFeedInput")
public class RawFeedInput {
    @Id
    private String id;

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private String guid;

    @Getter
    @Setter
    private String url;

    @Getter
    @Setter
    private String date;

    @Getter
    @Setter
    private boolean enclosure;

    @Getter
    @Setter
    private List<String> categories = new ArrayList<>();

//    @Getter
//    @Setter
//    @Field("custom_elements")
//    private List<RawCustomElement> customElements = new ArrayList<>();
}
