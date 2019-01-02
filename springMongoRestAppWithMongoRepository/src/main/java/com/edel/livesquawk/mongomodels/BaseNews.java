package com.edel.livesquawk.mongomodels;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Iqbal on 5/11/2018.
 */


class BaseNews {
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
    private String category;

    @Getter
    @Setter
    private LocalDateTime date;

    @Getter
    @Setter
    @Field("custom_elements")
    private List<CustomElements> customElements = new ArrayList<>();
}
