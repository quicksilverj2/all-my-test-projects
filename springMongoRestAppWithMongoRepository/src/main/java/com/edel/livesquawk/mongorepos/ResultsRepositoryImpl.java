package com.edel.livesquawk.mongorepos;

import com.edel.livesquawk.dao.*;
import com.edel.livesquawk.mongomodels.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.edel.livesquawk.application.AppConstants.MAX_NEWS_IN_RESULTS;
import static com.edel.livesquawk.application.AppConstants.PAGE_SIZE;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * Created by iqbal on 5/28/18.
 */

@Service
public class ResultsRepositoryImpl implements ResultsRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public ResultsRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<Results> findByDateOnOrBefore(Query query, Pageable pageable) {
        List<Results> list = mongoTemplate.find(query, Results.class);

        return PageableExecutionUtils.getPage(
                list,
                pageable,
                () -> mongoTemplate.count(query, Results.class));
    }

    @Override
    public ResultsResponseDao aggregateOnSymbol(int page, LocalDateTime date, String keyword) {

        MatchOperation textMatchOperation = null;

        if(keyword != null && !keyword.isEmpty()){
            textMatchOperation = match(TextCriteria.forDefaultLanguage().matching(keyword));
        }

        UnwindOperation unwindOperation = unwind("customElements");

        SortOperation sortOperation = sort(Sort.Direction.DESC, "date");

        GroupOperation groupOperation = group("customElements.sym")
                .push("$$ROOT").as("list")
                .first("date").as("firstItemDate")
                .first("customElements.trdSym").as("trdSym")
                .first("customElements.sym").as("sym");

        SortOperation resultSortOperation = sort(Sort.Direction.DESC, "firstItemDate");

        List<ResultsAggResponse> resultsAggResponses;

        List<AggregationOperation> operations = new ArrayList<>();

        if(textMatchOperation != null){
            operations.add(textMatchOperation);
        }

        if(date != null){
            operations.add(match(Criteria.where("date").lte(date)));
        }

        operations.add(unwindOperation);
        operations.add(sortOperation); // added this for fixing issue
        operations.add(groupOperation);
        operations.add(resultSortOperation);

        System.out.println("Aggregation Query : "+newAggregation(operations));

        resultsAggResponses =
                mongoTemplate.aggregate(
                        newAggregation(operations),
                        Results.class,
                        ResultsAggResponse.class)
                            .getMappedResults();


//        if (date != null) {
//
//            MatchOperation matchOperation = match(Criteria.where("date").lte(date));
//
//
//            if(textMatchOperation != null){
//
//                System.out.println(newAggregation(
//                        textMatchOperation,
//                        matchOperation,
//                        unwindOperation,
////                    sortOperation,
//                        groupOperation,
//                        resultSortOperation
//                ));
//
//
//                resultsAggResponses = mongoTemplate.aggregate(newAggregation(
//                        textMatchOperation,
//                        matchOperation,
//                        unwindOperation,
////                    sortOperation,
//                        groupOperation,
//                        resultSortOperation
//                ), Results.class, ResultsAggResponse.class).getMappedResults();
//
//            }else {
//
//                System.out.println(newAggregation(
////                        textMatchOperation,
//                        matchOperation,
//                        unwindOperation,
////                    sortOperation,
//                        groupOperation,
//                        resultSortOperation
//                ));
//
//                resultsAggResponses = mongoTemplate.aggregate(newAggregation(
////                        textMatchOperation,
//                        matchOperation,
//                        unwindOperation,
////                    sortOperation,
//                        groupOperation,
//                        resultSortOperation
//                ), Results.class, ResultsAggResponse.class).getMappedResults();
//            }
//
//
//
//        } else {
//
//            if(textMatchOperation != null){
//
//                resultsAggResponses = mongoTemplate.aggregate(newAggregation(
//                        textMatchOperation,
//                        unwindOperation,
////                    sortOperation,
//                        groupOperation,
//                        resultSortOperation
//                ), Results.class, ResultsAggResponse.class).getMappedResults();
//
//            } else{
//
//                resultsAggResponses = mongoTemplate.aggregate(newAggregation(
//                        unwindOperation,
////                    sortOperation,
//                        groupOperation,
//                        resultSortOperation
//                ), Results.class, ResultsAggResponse.class).getMappedResults();
//            }
//
//        }

        List<SingleSymItem> symItems = new ArrayList<>();

        int startIndex = PAGE_SIZE * page;
        int endIndex = resultsAggResponses.size() < (startIndex + PAGE_SIZE) ? resultsAggResponses.size() : startIndex + PAGE_SIZE;

        List<ResultsAggResponse> resultsAggResponseSubList = new ArrayList(resultsAggResponses.subList(startIndex, endIndex));

        for (ResultsAggResponse resultsAggResponse : resultsAggResponseSubList) {
            int endNewsItemIndex;
            boolean hasMoreNewsItems;

            if (resultsAggResponse.getList().size() > MAX_NEWS_IN_RESULTS) {
                endNewsItemIndex = MAX_NEWS_IN_RESULTS;
                hasMoreNewsItems = true;
            } else {
                endNewsItemIndex = resultsAggResponse.getList().size();
                hasMoreNewsItems = false;
            }

            symItems.add(
                    SingleSymItem.builder()
                            .symbol(resultsAggResponse.getSym())
//                            .trdSym(resultsAggResponse.getTrdSym())
                            .newsItems(new ArrayList(resultsAggResponse.getList().subList(0, endNewsItemIndex)))
                            .hasMoreNewsItems(hasMoreNewsItems)
                            .build()
            );
        }

        return ResultsResponseDao.builder()
                .content(symItems)
                .totalElements(resultsAggResponses.size())
                .first(page == 0)
                .last(endIndex == resultsAggResponses.size())
                .totalPages((int) Math.ceil(resultsAggResponses.size() / (float) PAGE_SIZE))
                .size(PAGE_SIZE)
                .number(page)
                .build();
    }

    @Override
    public SingleSymbolNewsDao getSingleSymbolResults(int page, LocalDateTime date, String symbol) {
        UnwindOperation unwindOperation = unwind("customElements");
        MatchOperation symbolMatchOperation = match(Criteria.where("customElements.sym").is(symbol));
        SortOperation resultSortOperation = sort(Sort.Direction.DESC, "date");
        List<SingleNewsItem> resultsItems;

        if (date != null) {
            MatchOperation matchOperation = match(Criteria.where("date").lte(date));
            resultsItems = mongoTemplate.aggregate(newAggregation(
                    matchOperation,
                    unwindOperation,
                    symbolMatchOperation,
                    resultSortOperation
            ), Results.class, SingleNewsItem.class).getMappedResults();

        } else {
            resultsItems = mongoTemplate.aggregate(newAggregation(
                    unwindOperation,
                    symbolMatchOperation,
                    resultSortOperation
            ), Results.class, SingleNewsItem.class).getMappedResults();
        }

        int startIndex = PAGE_SIZE * page;
        int endIndex = resultsItems.size() < (startIndex + PAGE_SIZE) ? resultsItems.size() : startIndex + PAGE_SIZE;

        return SingleSymbolNewsDao.builder()
                .symbol(symbol)
                .content(new ArrayList(resultsItems.subList(startIndex, endIndex)))
                .totalElements(resultsItems.size())
                .first(page == 0)
                .last(endIndex == resultsItems.size())
                .totalPages((int) Math.ceil(resultsItems.size() / (float) PAGE_SIZE))
                .size(PAGE_SIZE)
                .number(page)
                .build();
    }

    @Override
    public List<SingleNewsItem> getNewsForSymbol(int page, LocalDateTime date, String symbol) {
        UnwindOperation unwindOperation = unwind("customElements");
        MatchOperation symbolMatchOperation = match(Criteria.where("customElements.sym").is(symbol));
        SortOperation resultSortOperation = sort(Sort.Direction.DESC, "date");
        List<SingleNewsItem> resultsItems;

        if (date != null) {
            MatchOperation matchOperation = match(Criteria.where("date").lte(date));
            resultsItems = mongoTemplate.aggregate(newAggregation(
                    matchOperation,
                    unwindOperation,
                    symbolMatchOperation,
                    resultSortOperation
            ), Results.class, SingleNewsItem.class).getMappedResults();

        } else {
            resultsItems = mongoTemplate.aggregate(newAggregation(
                    unwindOperation,
                    symbolMatchOperation,
                    resultSortOperation
            ), Results.class, SingleNewsItem.class).getMappedResults();
        }

        return resultsItems;
    }
}
