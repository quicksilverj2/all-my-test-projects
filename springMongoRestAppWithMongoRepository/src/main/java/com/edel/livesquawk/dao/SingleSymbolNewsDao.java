package com.edel.livesquawk.dao;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iqbal on 5/29/18.
 */

@Builder
public class SingleSymbolNewsDao {
    @Getter
    @Setter
    private String symbol;

//    @Getter
//    @Setter
//    private String trdSym;

    @Getter
    @Setter
    @Builder.Default
    private List<SingleNewsItem> content = new ArrayList<>();

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
