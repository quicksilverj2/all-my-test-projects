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
public class SingleSymItem {
    @Getter
    @Setter
    private String symbol;

//    @Getter
//    @Setter
//    private String trdSym;

    @Getter
    @Setter
    private boolean hasMoreNewsItems;

    @Getter
    @Setter
    @Builder.Default
    private List<SingleNewsItem> newsItems = new ArrayList<>();
}
