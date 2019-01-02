package com.edel.livesquawk.mongorepos;

import com.edel.livesquawk.dao.SingleNewsItem;
import com.edel.livesquawk.mongomodels.BlockDeals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

/**
 * Created by iqbal on 5/28/18.
 */

@Service
public class BlockDealsRepositoryImpl implements BlockDealsRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public BlockDealsRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<BlockDeals> findByDateOnOrBefore(Query query, Pageable pageable) {
        List<BlockDeals> list = mongoTemplate.find(query, BlockDeals.class);

        return PageableExecutionUtils.getPage(
                list,
                pageable,
                () -> mongoTemplate.count(query, BlockDeals.class));

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
            ), BlockDeals.class, SingleNewsItem.class).getMappedResults();

        } else {
            newsItems = mongoTemplate.aggregate(newAggregation(
                    unwindOperation,
                    symbolMatchOperation,
                    newstSortOperation
            ), BlockDeals.class, SingleNewsItem.class).getMappedResults();
        }

        return newsItems;
    }
}
