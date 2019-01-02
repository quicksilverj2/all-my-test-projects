package com.edel.mw.order.edelmwReports.v1.mongo;

import com.edel.mw.order.edelmwReports.v1.dao.OrderBookResponseObject;
import com.edel.mw.order.edelmwReports.v1.dao.OrderLogsResponseDao;
import com.edel.mw.order.edelmwReports.v1.dao.TradeBookResponseObject;

import java.util.List;

public interface OrderBookRepositoryV1Custom {

    List<OrderBookResponseObject> getOrderHistory(String userId, String stDt, String endDt) throws Exception;

    List<TradeBookResponseObject> getTradeHistory(String userId, String ordId);

    List<OrderBookResponseObject> getCurrentDayOrderBook(String userId);

    List<TradeBookResponseObject> getCurrentDayTrades(String userId);

    OrderLogsResponseDao getOrderLogs(String userId, String ordId);
}
