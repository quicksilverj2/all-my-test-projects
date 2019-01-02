package com.edel.livesquawk.mongorepos;

import com.edel.livesquawk.dao.ResultsResponseDao;
import com.edel.livesquawk.dao.SingleNewsItem;
import com.edel.livesquawk.dao.SingleSymbolNewsDao;
import com.edel.livesquawk.mongomodels.Results;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDateTime;
import java.util.List;


public interface ResultsRepositoryCustom {
    Page<Results> findByDateOnOrBefore(Query query, Pageable pageable);
    ResultsResponseDao aggregateOnSymbol(int page, LocalDateTime date, String keyword);
    SingleSymbolNewsDao getSingleSymbolResults(int page, LocalDateTime date, String symbol);
    List<SingleNewsItem> getNewsForSymbol(int page, LocalDateTime date, String symbol);
}
