package com.edel.livesquawk.dao;

import com.edel.livesquawk.mongomodels.CustomElements;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

/**
 * Created by iqbal on 5/29/18.
 */

public class SingleNewsItem {
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
    private CustomElements customElements;
}
