package com.edel.livesquawk.dao;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by iqbal on 5/28/18.
 */

public class ResultsAggResponse {
    @Getter
    private String id;

    @Getter
    private List<SingleNewsItem> list;

    @Getter
    private LocalDateTime firstItemDate;

    @Getter
    private String sym;

    @Getter
    private String trdSym;
}
