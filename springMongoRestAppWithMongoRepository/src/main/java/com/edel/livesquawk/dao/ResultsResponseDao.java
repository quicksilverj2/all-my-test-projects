package com.edel.livesquawk.dao;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iqbal on 5/28/18.
 */

@Builder
public class ResultsResponseDao {
    @Getter
    @Setter
    @Builder.Default
    private List<SingleSymItem> content = new ArrayList<>();

    @Getter
    @Setter
    private int totalElements;

    @Getter
    @Setter
    private boolean first;

    @Getter
    @Setter
    private boolean last;

    @Getter
    @Setter
    private int totalPages;

    @Getter
    @Setter
    private int size;

    @Getter
    @Setter
    private int number;
}
