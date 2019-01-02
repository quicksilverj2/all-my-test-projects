package com.edel.livesquawk.mongorepos;

import com.edel.livesquawk.dao.SingleNewsItem;
import com.edel.livesquawk.mongomodels.GeneralNews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDateTime;
import java.util.List;


public interface GeneralNewsRepositoryCustom {
    Page<GeneralNews> findByCustomQuery(Query query, Pageable pageable);
    List<SingleNewsItem> getNewsForSymbol(int page, LocalDateTime date, String symbol);
}
