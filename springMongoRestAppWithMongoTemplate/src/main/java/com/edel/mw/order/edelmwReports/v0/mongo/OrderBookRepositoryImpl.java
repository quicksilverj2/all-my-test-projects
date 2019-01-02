package com.edel.mw.order.edelmwReports.v0.mongo;

import com.edel.mw.order.edelmwReports.common.EdelMWReportConstants;
import com.edel.mw.order.edelmwReports.common.EdelMWReportHelper;
import com.edel.mw.order.edelmwReports.common.dao.BaseOrdersDocument;
import com.edel.mw.order.edelmwReports.v0.dao.OrderBookResponseDao;
import com.edel.mw.order.edelmwReports.v0.dao.OrderLogsResponseDao;
import com.edel.mw.order.edelmwReports.v0.dao.TradeBookResponseDao;
import com.edel.order.EdelOrderUpdateModel;
import com.edel.order.EdelTradeUpdateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.edel.mw.order.edelmwReports.mongo.aggregation.MongoDBAggregationQueryHelper.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

/**
 * Created by jitheshrajan on 7/2/18.
 */
public class OrderBookRepositoryImpl implements OrderBookRepositoryCustom {

    private static final long ALLOWED_DATE_RANGE_GAP = 90;
    private final MongoOperations mongoOperations;

    private static final BaseOrdersDocument<EdelTradeUpdateModel> EDEL_TRADE_UPDATE_MODEL_BASE_ORDERS_DOCUMENT = new BaseOrdersDocument<EdelTradeUpdateModel>();
    private static final BaseOrdersDocument<EdelOrderUpdateModel> EDEL_ORDER_UPDATE_MODEL_BASE_ORDERS_DOCUMENT = new BaseOrdersDocument<EdelOrderUpdateModel>();




    @Autowired
    public OrderBookRepositoryImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }


    @Override
    public long getOrdersCount(String userId) {
        Query query = new Query();
        long count = mongoOperations.count(query,userId + "_EQ_ORDERS");

        System.out.println("count : "+count);
        return count;
    }

    @Override
    public List<? extends BaseOrdersDocument> getOrders(String userId) {

        String collName = userId+"_EQ_ORDERS";

        Query query = new Query();
        List<? extends BaseOrdersDocument> foundListOFdocuments =  mongoOperations.find(query, EDEL_TRADE_UPDATE_MODEL_BASE_ORDERS_DOCUMENT.getClass() ,collName);

        System.out.println(foundListOFdocuments);
        System.out.println(foundListOFdocuments.size());
        return foundListOFdocuments;
    }


    @Override
    public List<? extends BaseOrdersDocument> getOrderHistory(String userId, String startDate, String endDate) throws Exception {

        String collName = userId+"_EQ_ORDERS";

        if(EdelMWReportHelper.checkIfDateFormat(startDate, EdelMWReportConstants.DATE_FILTER_FORMAT) &&
                EdelMWReportHelper.checkIfDateFormat(endDate, EdelMWReportConstants.DATE_FILTER_FORMAT)){

            if (EdelMWReportHelper.getDateDiff(startDate, endDate, EdelMWReportConstants.DATE_FILTER_FORMAT, TimeUnit.DAYS) > ALLOWED_DATE_RANGE_GAP) {
                // invalid date range!
                throw new Exception();
            }
        }else{
            throw new Exception();
        }

        AggregationOperation matchOperation = match(getDateinBetweenCriteria(startDate, endDate));

        System.out.println(
                newAggregation(matchOperation,
                        DESCENDING_EXTORDTIM_SORT_OPERATION,
                        ORDER_BOOK_RESPONSE_GROUP_OPERATION).toString());

        return mongoOperations.aggregate(
                newAggregation(matchOperation,
                        DESCENDING_EXTORDTIM_SORT_OPERATION,
                        ORDER_BOOK_RESPONSE_GROUP_OPERATION),
                collName, EDEL_TRADE_UPDATE_MODEL_BASE_ORDERS_DOCUMENT.getClass()).getMappedResults();
    }

    @Override
    public List<BaseOrdersDocument> getTradeHistory(String userId, String sNestOrdNum) {

        String collName = userId+"_EQ_ORDERS";

        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("type").is(EdelMWReportConstants.MongoConstants.TRADE_BOOK_DEFAULT_TYPE),Criteria.where("data.oID").is(sNestOrdNum));

        MatchOperation matchOperation = match(criteria);

        return mongoOperations.aggregate(newAggregation(matchOperation), collName, BaseOrdersDocument.class).getMappedResults();
    }

    @Override
    public List<OrderBookResponseDao> getCurrentDayOrderBook(String userId) {

        String collName = userId+"_EQ_ORDERS";


        System.out.println("Query on collection : "+collName);

        Criteria joinedCriteria  = ORDER_TYPE_CRITERIA;

        joinedCriteria.orOperator(getCurrentDateCriteria(), getAMOStatusesCriteria());

//        joinedCriteria.andOperator(getCurrentDateCriteria(), ORDER_TYPE_CRITERIA);

        AggregationOperation matchOperationStage1 = match(joinedCriteria);


        System.out.println(newAggregation(matchOperationStage1,
                DESCENDING_EXTORDTIM_SORT_OPERATION,
                ORDER_BOOK_RESPONSE_GROUP_OPERATION,
//                matchOperationStage4,
                DESCENDING_EXTORDTIM_SORT_OPERATION).toString());

        return mongoOperations.aggregate(newAggregation(
                matchOperationStage1,
                DESCENDING_EXTORDTIM_SORT_OPERATION,
                ORDER_BOOK_RESPONSE_GROUP_OPERATION,
//                matchOperationStage4,
                DESCENDING_EXTORDTIM_SORT_OPERATION), collName, OrderBookResponseDao.class).getMappedResults();
    }

    @Override
    public List<TradeBookResponseDao> getCurrentDayTrades(String userId){
        String collName = userId+"_EQ_ORDERS";

        Criteria joinedCriteria = new Criteria();
        joinedCriteria.andOperator(TRADE_TYPE_CRITERIA, getCurrentDateCriteria());

        AggregationOperation matchOperation = match(joinedCriteria);

        System.out.println(
                newAggregation(matchOperation,
                        DESCENDING_EXTORDTIM_SORT_OPERATION,
                        TRADE_BOOK_RESPONSE_GROUP_OPERATION).toString());

        return mongoOperations.aggregate(
                newAggregation(
                        matchOperation,
                        DESCENDING_EXTORDTIM_SORT_OPERATION,
                        TRADE_BOOK_RESPONSE_GROUP_OPERATION),
                collName,
                TradeBookResponseDao.class).getMappedResults();


    }

//
//    public com.edel.mw.order.edelmwReports.v1.dao.OrderLogsResponseDao getOrderLogsV1(String userId, String oid){
//        String collName = userId+"_EQ_ORDERS";
//
//        Criteria joinedCriteria = new Criteria();
//        joinedCriteria.andOperator(ORDER_TYPE_CRITERIA, getOrderNumberCriteria(oid));
//
//        AggregationOperation matchOperation = match(joinedCriteria);
//
//        System.out.println(
//                newAggregation(matchOperation,
//                        DESCENDING_EXTORDTIM_SORT_OPERATION,
//                        ORDER_LOGS_RESPONSE_GROUP_OPERATION_V1).toString());
//
//        List<com.edel.mw.order.edelmwReports.v1.dao.OrderLogsResponseDao> orderLogsAggregatedResponseList =
//                mongoOperations.aggregate(newAggregation(matchOperation,
//                        DESCENDING_EXTORDTIM_SORT_OPERATION,
//                        ORDER_LOGS_RESPONSE_GROUP_OPERATION_V1),
//                        collName,
//                        com.edel.mw.order.edelmwReports.v1.dao.OrderLogsResponseDao.class).getMappedResults();
//
//
//
//        if(orderLogsAggregatedResponseList != null && !orderLogsAggregatedResponseList.isEmpty()){
//            return orderLogsAggregatedResponseList.get(0);
//        }else{
//            return null;
//        }
//    }

    @Override
    public OrderLogsResponseDao getOrderLogs(String userId, String oid){
        String collName = userId+"_EQ_ORDERS";

        Criteria joinedCriteria = new Criteria();
        joinedCriteria.andOperator(ORDER_TYPE_CRITERIA, getOrderNumberCriteria(oid));

        AggregationOperation matchOperation = match(joinedCriteria);

        System.out.println(
                newAggregation(matchOperation,
                        DESCENDING_EXTORDTIM_SORT_OPERATION,
                        ORDER_LOGS_RESPONSE_GROUP_OPERATION).toString());

        List<OrderLogsResponseDao> orderLogsAggregatedResponseList =
                mongoOperations.aggregate(
                        newAggregation(matchOperation,
                                DESCENDING_EXTORDTIM_SORT_OPERATION,
                                ORDER_LOGS_RESPONSE_GROUP_OPERATION),
                        collName, OrderLogsResponseDao.class).getMappedResults();



        if(orderLogsAggregatedResponseList != null && !orderLogsAggregatedResponseList.isEmpty()){
            return orderLogsAggregatedResponseList.get(0);
        }else{
            return null;
        }
    }


}
