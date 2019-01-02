package com.edel.mw.order.edelmwReports.v1.mongo;

import com.edel.mw.order.edelmwReports.common.EdelMWReportConstants;
import com.edel.mw.order.edelmwReports.common.EdelMWReportHelper;
import com.edel.mw.order.edelmwReports.v1.dao.OrderBookResponseObject;
import com.edel.mw.order.edelmwReports.v1.dao.OrderLogsResponseDao;
import com.edel.mw.order.edelmwReports.v1.dao.TradeBookResponseObject;
import com.edel.mw.order.edelmwReports.v1.dao.TradeFillItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.edel.mw.order.edelmwReports.mongo.aggregation.MongoDBAggregationQueryHelper.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

public class OrderBookRepositoryV1Impl implements OrderBookRepositoryV1Custom {


    private static final long ALLOWED_DATE_RANGE_GAP = 90;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public OrderBookRepositoryV1Impl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public List<OrderBookResponseObject> getOrderHistory(String userId, String stDt, String endDt) throws Exception {

        String collName = userId+"_EQ_ORDERS";

        if(EdelMWReportHelper.checkIfDateFormat(stDt, EdelMWReportConstants.DATE_FILTER_FORMAT) &&
                EdelMWReportHelper.checkIfDateFormat(endDt, EdelMWReportConstants.DATE_FILTER_FORMAT)){

            if (EdelMWReportHelper.getDateDiff(stDt, endDt, EdelMWReportConstants.DATE_FILTER_FORMAT, TimeUnit.DAYS) > ALLOWED_DATE_RANGE_GAP) {
                // invalid date range!
                throw new Exception();
            }
        }else{
            throw new Exception();
        }

        AggregationOperation matchOperation = match(getDateinBetweenCriteria(stDt, endDt));

        System.out.println(
                newAggregation(matchOperation,
                        DESCENDING_DATA_EXTORDTIM_SORT_OPERATION,
                        ORDER_BOOK_RESPONSE_GROUP_OPERATION_V1).toString());

        return mongoTemplate.aggregate(
                newAggregation(matchOperation,
                        DESCENDING_DATA_EXTORDTIM_SORT_OPERATION,
                        ORDER_BOOK_RESPONSE_GROUP_OPERATION_V1),
                collName, OrderBookResponseObject.class).getMappedResults();
    }

    @Override
    public List<TradeBookResponseObject> getTradeHistory(String userId, String ordId) {
        String collName = userId+"_EQ_ORDERS";

        Criteria criteria = new Criteria();
        criteria.andOperator(TRADE_TYPE_CRITERIA,getOrderNumberCriteria(ordId));

        MatchOperation matchOperation = match(criteria);

        return mongoTemplate.aggregate(newAggregation(matchOperation, DESCENDING_DATA_EXTORDTIM_SORT_OPERATION,
                TRADE_BOOK_RESPONSE_GROUP_OPERATION_V1), collName, TradeBookResponseObject.class).getMappedResults();
    }


    @Override
    public List<OrderBookResponseObject> getCurrentDayOrderBook(String userId) {
        String collName = userId+"_EQ_ORDERS";

        System.out.println("Query on collection : "+collName);

//        Criteria joinedCriteria  = ORDER_TYPE_CRITERIA;

        Criteria joinedCriteria = new Criteria();
        joinedCriteria.andOperator(ORDER_TYPE_CRITERIA, getCurrentDateCriteria());

//        joinedCriteria.orOperator(getCurrentDateCriteria(), getAMOStatusesCriteria());

//        joinedCriteria.andOperator(getCurrentDateCriteria(), ORDER_TYPE_CRITERIA);

        AggregationOperation matchOperationStage1 = match(joinedCriteria);

        System.out.println(newAggregation(matchOperationStage1,
                DESCENDING_DATA_EXTORDTIM_SORT_OPERATION,
                ORDER_BOOK_RESPONSE_GROUP_OPERATION_V1,
//                matchOperationStage4,
                DESCENDING_EXTORDTIM_SORT_OPERATION).toString());

        List<OrderBookResponseObject> orderBookResponseObjects = mongoTemplate.aggregate(newAggregation(
                matchOperationStage1,
                DESCENDING_DATA_EXTORDTIM_SORT_OPERATION,
                ORDER_BOOK_RESPONSE_GROUP_OPERATION_V1,
//                matchOperationStage4,
                DESCENDING_EXTORDTIM_SORT_OPERATION), collName, OrderBookResponseObject.class).getMappedResults();

        processAveragePricesForOrderList(orderBookResponseObjects);

        return orderBookResponseObjects;
    }





    @Override
    public List<TradeBookResponseObject> getCurrentDayTrades(String userId) {
        String collName = userId+"_EQ_ORDERS";

        Criteria joinedCriteria = new Criteria();
        joinedCriteria.andOperator(TRADE_TYPE_CRITERIA, getCurrentDateCriteria());

        AggregationOperation matchOperation = match(joinedCriteria);

        System.out.println(
                newAggregation(matchOperation,
                        DESCENDING_DATA_EXTORDTIM_SORT_OPERATION,
                        TRADE_BOOK_RESPONSE_GROUP_OPERATION_V1).toString());

        List<TradeBookResponseObject> tradeBookResponseObjectList = mongoTemplate.aggregate(
                newAggregation(
                        matchOperation,
                        DESCENDING_DATA_EXTORDTIM_SORT_OPERATION,
                        TRADE_BOOK_RESPONSE_GROUP_OPERATION_V1),
                collName,
                TradeBookResponseObject.class).getMappedResults();

        processAveragePricesForTradeList(tradeBookResponseObjectList);

        return tradeBookResponseObjectList;
    }

    // currently we have trade book objects which store all data incrementally. that is,
    // if there are multiple fills, each leg will contain average price based on previous fills as well.
    // we will correct the same and send the data as single fills.
    private void processAveragePricesForTradeList(List<TradeBookResponseObject> tradeBookResponseObjectList) {

        for(TradeBookResponseObject tradeBookResponseObject : tradeBookResponseObjectList){

            if(tradeBookResponseObject.getFlDtlLst().size() <= 1){
                // no need to process the record if there is only one trade fill leg!
                continue;
            }

            TreeMap<String, TradeFillItem> tradeLegs = new TreeMap<>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    try{
                        return Integer.parseInt(o2) - Integer.parseInt(o1);
                    }catch (Exception e){
                        return 0;
                    }
                }
            });

            System.out.println("Trade Fill Items Before Processing");
            for(TradeFillItem tradeFillItem : tradeBookResponseObject.getFlDtlLst()){
                System.out.println(tradeFillItem.getFlID() + " " + tradeFillItem.getFlQty()  + " " +tradeFillItem.getFlPrc());
                tradeLegs.put(tradeFillItem.getFlID(),tradeFillItem);
            }

            String previousAveragePrice = "-0.00";
            String previousQuantity = "-0";
            TradeFillItem oldTradeFillItem = null;


            // we are iterating the map in reverse order here. Hence the last item in the list is
            // untouched, which would be the first leg of the trade whose values are correct as such!
            for(Map.Entry<String, TradeFillItem> entry : tradeLegs.entrySet()){

                if(oldTradeFillItem == null){
                    oldTradeFillItem = entry.getValue();
                    previousAveragePrice = entry.getValue().getFlPrc();
                    previousQuantity = entry.getValue().getFlQty();
                    continue;
                }else{

                    String currentLegAveragePrice = EdelMWReportHelper.calculateIndividualAveragePriceFromWeightedAverage(
                            previousAveragePrice,
                            previousQuantity,
                            entry.getValue().getFlPrc(),
                            entry.getValue().getFlQty());

                    oldTradeFillItem.setFlPrc(currentLegAveragePrice);
                    oldTradeFillItem.setFlQty((Double.parseDouble(previousQuantity) - Double.parseDouble(entry.getValue().getFlQty())) + "");

                    previousAveragePrice = entry.getValue().getFlPrc();
                    previousQuantity = entry.getValue().getFlQty();

                    oldTradeFillItem = entry.getValue();
                }
            }
            tradeBookResponseObject.setFlDtlLst(new ArrayList<>(tradeLegs.values()));
            // TODO remove this log!
            System.out.println("Trade Fill Items After Processing");
            for(TradeFillItem tradeFillItem : tradeBookResponseObject.getFlDtlLst()){
                System.out.println(tradeFillItem.getFlID() + " " + tradeFillItem.getFlQty()  + " " +tradeFillItem.getFlPrc());
            }
        }

    }


    // currently we have trade book objects which store all data incrementally. that is,
    // if there are multiple fills, each leg will contain average price based on previous fills as well.
    // we will correct the same and send the data as single fills.
    private void processAveragePricesForOrderList(List<OrderBookResponseObject> orderBookResponseObjects) {

        for (OrderBookResponseObject orderBookResponseObject : orderBookResponseObjects) {

            if (orderBookResponseObject.getFlDtlLst().size() <= 1) {
                // no need to process the record if there is only one trade fill leg!
                continue;
            }

            TreeMap<String, TradeFillItem> tradeLegs = new TreeMap<>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    try {
                        return Integer.parseInt(o2) - Integer.parseInt(o1);
                    } catch (Exception e) {
                        return 0;
                    }
                }
            });

            System.out.println("Trade Fill Items Before Processing");
            for (TradeFillItem tradeFillItem : orderBookResponseObject.getFlDtlLst()) {

                if (tradeFillItem.getFlID() == null || tradeFillItem.getFlID().isEmpty()) {
                    continue;
                }

                System.out.println(tradeFillItem.getFlID() + " " + tradeFillItem.getFlQty() + " " + tradeFillItem.getFlPrc());
                tradeLegs.put(tradeFillItem.getFlID(), tradeFillItem);
            }

            String previousAveragePrice = "-0.00";
            String previousQuantity = "-0";
            TradeFillItem oldTradeFillItem = null;


            // we are iterating the map in reverse order here. Hence the last item in the list is
            // untouched, which would be the first leg of the trade whose values are correct as such!
            for (Map.Entry<String, TradeFillItem> entry : tradeLegs.entrySet()) {

                if (oldTradeFillItem == null) {
                    oldTradeFillItem = entry.getValue();
                    previousAveragePrice = entry.getValue().getFlPrc();
                    previousQuantity = entry.getValue().getFlQty();
                    continue;
                } else {

                    String currentLegAveragePrice = EdelMWReportHelper.calculateIndividualAveragePriceFromWeightedAverage(
                            previousAveragePrice,
                            previousQuantity,
                            entry.getValue().getFlPrc(),
                            entry.getValue().getFlQty());


                    oldTradeFillItem.setFlPrc(currentLegAveragePrice);
                    oldTradeFillItem.setFlQty((Double.parseDouble(previousQuantity) - Double.parseDouble(entry.getValue().getFlQty())) + "");

                    previousAveragePrice = entry.getValue().getFlPrc();
                    previousQuantity = entry.getValue().getFlQty();

                    oldTradeFillItem = entry.getValue();

                }
            }

            orderBookResponseObject.setFlDtlLst(new ArrayList<>(tradeLegs.values()));
            // TODO remove this log!
            System.out.println("Trade Fill Items After Processing");
            for (TradeFillItem tradeFillItem : orderBookResponseObject.getFlDtlLst()) {
                System.out.println(tradeFillItem.getFlID() + " " + tradeFillItem.getFlQty() + " " + tradeFillItem.getFlPrc());
            }
        }

    }




    @Override
    public OrderLogsResponseDao getOrderLogs(String userId, String ordId) {
        String collName = userId+"_EQ_ORDERS";

        Criteria joinedCriteria = new Criteria();
        joinedCriteria.andOperator(ORDER_TYPE_CRITERIA, getOrderNumberCriteria(ordId));

        AggregationOperation matchOperation = match(joinedCriteria);

        System.out.println(
                newAggregation(matchOperation,
                        DESCENDING_DATA_EXTORDTIM_SORT_OPERATION,
                        ORDER_LOGS_RESPONSE_GROUP_OPERATION_V1).toString());

        List<OrderLogsResponseDao> orderLogsAggregatedResponseList =
                mongoTemplate.aggregate(newAggregation(matchOperation,
                        DESCENDING_DATA_EXTORDTIM_SORT_OPERATION,
                        ORDER_LOGS_RESPONSE_GROUP_OPERATION_V1),
                        collName,
                        OrderLogsResponseDao.class).getMappedResults();



        if(orderLogsAggregatedResponseList != null && !orderLogsAggregatedResponseList.isEmpty()){
            return orderLogsAggregatedResponseList.get(0);
        }else{
            return null;
        }
    }
}
