package com.edel.mw.order.edelmwReports.v0.controller;

import com.edel.mw.order.edelmwReports.common.dao.BaseOrdersDocument;
import com.edel.mw.order.edelmwReports.v0.dao.OrderBookResponseDao;
import com.edel.mw.order.edelmwReports.v0.dao.OrderLogsResponseDao;
import com.edel.mw.order.edelmwReports.v0.dao.TradeBookResponseDao;
import com.edel.mw.order.edelmwReports.v0.mongo.OrderBookRepositoryImpl;
import com.edel.order.EdelOrderUpdateModel;
import io.swagger.annotations.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by jitheshrajan on 7/2/18.
 */
@RestController("LiveOrdersController")
@RequestMapping("/report")
public class LiveOrdersController {

    private OrderBookRepositoryImpl orderBookRepositoryImpl;

    public LiveOrdersController(OrderBookRepositoryImpl orderBookRepositoryImpl) {
        this.orderBookRepositoryImpl = orderBookRepositoryImpl;
    }

    private class SwaggerResponseClass extends BaseOrdersDocument<EdelOrderUpdateModel>{

    }

    @GetMapping("/all")
    @ApiResponse(code = 200, message = "Some success Message", response = SwaggerResponseClass.class)
    public List<? extends BaseOrdersDocument> getAll(@RequestParam(required = false, value = "usr") String userId) {
        List<? extends BaseOrdersDocument> ordersDocuments = this.orderBookRepositoryImpl.getOrders(userId);

        return ordersDocuments;
    }

    @GetMapping("/count")
    public long getCount(@RequestParam(required = false, value = "usr") String userId){
        return this.orderBookRepositoryImpl.getOrdersCount(userId);
    }

    @GetMapping("/order/history")
    public List<? extends BaseOrdersDocument> getOrderHistory(
            @RequestParam(required = true, value = "usr") String userId,
            @RequestParam(required = true, value = "stDt") String stDt,
            @RequestParam(required = true, value = "endDt") String endDt
    ) throws Exception {
        List<? extends BaseOrdersDocument> ordersDocuments = this.orderBookRepositoryImpl.getOrderHistory(userId,stDt,endDt);
        return ordersDocuments;
    }

    @GetMapping("/trade/history")
    public List<BaseOrdersDocument> getTradeHistory(
            @RequestParam(required = true, value = "usr") String userId,
            @RequestParam(required = true, value = "oid") String ordId
    ){
        List<BaseOrdersDocument> ordersDocuments = this.orderBookRepositoryImpl.getTradeHistory(userId, ordId);
        return ordersDocuments;
    }

    @GetMapping("/order/today")
    public List<OrderBookResponseDao> getOpenOrders(
            @RequestParam(required = false, value = "usr") String userId
    ){
        List<OrderBookResponseDao> ordersDocuments = this.orderBookRepositoryImpl.getCurrentDayOrderBook(userId);
        return ordersDocuments;
    }

    @GetMapping("/trade/today")
    public List<TradeBookResponseDao> getOpenTrades(
            @RequestParam(required = false, value = "usr") String userId
    ){
        List<TradeBookResponseDao> ordersDocuments = this.orderBookRepositoryImpl.getCurrentDayTrades(userId);
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

}
