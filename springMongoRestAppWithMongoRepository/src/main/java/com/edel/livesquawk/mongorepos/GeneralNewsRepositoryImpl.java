package com.edel.livesquawk.mongorepos;

import com.edel.livesquawk.dao.SingleNewsItem;
import com.edel.livesquawk.mongomodels.GeneralNews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;


/**
 * Created by iqbal on 5/25/18.
 */


@Service
public class GeneralNewsRepositoryImpl implements GeneralNewsRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public GeneralNewsRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<GeneralNews> findByCustomQuery(Query query, Pageable pageable) {
        List<GeneralNews> list = mongoTemplate.find(query, GeneralNews.class);

        return PageableExecutionUtils.getPage(
                list,
                pageable,
                () -> mongoTemplate.count(query, GeneralNews.class));
    }

    @Override
    public List<SingleNewsItem> getNewsForSymbol(int page, LocalDateTime date, String symbol) {
        UnwindOperation unwindOperation = unwind("customElements");
        MatchOperation symbolMatchOperation = match(Criteria.where("customElements.sym").is(symbol));
        SortOperation newstSortOperation = sort(Sort.Direction.DESC, "date");
        List<SingleNewsItem> newsItems;

        if (date != null) {
            MatchOperation matchOperation = match(Criteria.where("date").lte(date));
            newsItems = mongoTemplate.aggregate(newAggregation(
                    matchOperation,
                    unwindOperation,
                    symbolMatchOperation,
                    newstSortOperation
            ), GeneralNews.class, SingleNewsItem.class).getMappedResults();

        } else {
            newsItems = mongoTemplate.aggregate(newAggregation(
                    unwindOperation,
                    symbolMatchOperation,
                    newstSortOperation
            ), GeneralNews.class, SingleNewsItem.class).getMappedResults();
        }

        return newsItems;
    }
}
