package com.edel.mw.order.edelmwReports.v1.controller;

import com.edel.mw.order.edelmwReports.common.EdelMWReportHelper;
import com.edel.mw.order.edelmwReports.v1.dao.OrderBookResponseObject;
import com.edel.mw.order.edelmwReports.v1.dao.OrderLogsResponseDao;
import com.edel.mw.order.edelmwReports.v1.dao.PositionBookResponseObject;
import com.edel.mw.order.edelmwReports.v1.dao.TradeBookResponseObject;
import com.edel.mw.order.edelmwReports.v1.mongo.OrderBookRepositoryV1Impl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by jitheshrajan on 7/2/18.
 */
@RestController("LiveOrdersControllerV1")
@RequestMapping("v1/report")
public class LiveOrdersControllerV1 {


    /*
     * Creating a new version of the same controller in order to handle multiple response types.
     * This controller will send response which is based on our new edelmw's response style.
     * */

    private OrderBookRepositoryV1Impl orderBookRepositoryImpl;

    public LiveOrdersControllerV1(OrderBookRepositoryV1Impl orderBookRepositoryImpl) {
        this.orderBookRepositoryImpl = orderBookRepositoryImpl;
    }

    @GetMapping("/order/history")
    public List<OrderBookResponseObject> getOrderHistory(
            @RequestParam(required = true, value = "usr") String userId,
            @RequestParam(required = true, value = "stDt") String stDt,
            @RequestParam(required = true, value = "endDt") String endDt
    ) throws Exception {
        List<OrderBookResponseObject> ordersDocuments = this.orderBookRepositoryImpl.getOrderHistory(userId,stDt,endDt);
        return ordersDocuments;
    }

    @GetMapping("/trade/history")
    public List<TradeBookResponseObject> getTradeHistory(
            @RequestParam(required = true, value = "usr") String userId,
            @RequestParam(required = true, value = "oid") String ordId
    ){
        List<TradeBookResponseObject> ordersDocuments = this.orderBookRepositoryImpl.getTradeHistory(userId, ordId);
        return ordersDocuments;
    }

    @GetMapping("/order/today")
    public List<OrderBookResponseObject> getOpenOrders(
            @RequestParam(required = false, value = "usr") String userId
    ){
        List<OrderBookResponseObject> ordersDocuments = this.orderBookRepositoryImpl.getCurrentDayOrderBook(userId);
        return ordersDocuments;
    }

    @GetMapping("/trade/today")
    public List<TradeBookResponseObject> getOpenTrades(
            @RequestParam(required = false, value = "usr") String userId
    ){
        List<TradeBookResponseObject> ordersDocuments = this.orderBookRepositoryImpl.getCurrentDayTrades(userId);
        return ordersDocuments;
    }

    @GetMapping("/order/logs")
    public OrderLogsResponseDao getOrderLogs(
            @RequestParam(required = true, value = "usr") String userId,
            @RequestParam(required = true, value = "oid") String ordId
    ){
        OrderLogsResponseDao orderLogs = this.orderBookRepositoryImpl.getOrderLogs(userId, ordId);
        return orderLogs;
    }


    @GetMapping("positions")
    public List<PositionBookResponseObject> getPositions(
            @RequestParam(required = true, value = "usr") String userId
    ){
        try{
            List<TradeBookResponseObject> ordersDocuments = this.orderBookRepositoryImpl.getCurrentDayTrades(userId);

            return EdelMWReportHelper.calculateAveragePriceForPositions(ordersDocuments);
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }
}
