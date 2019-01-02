package com.edel.mw.order.edelmwReports.v0.mongo;

import com.edel.mw.order.edelmwReports.common.dao.BaseOrdersDocument;
import com.edel.mw.order.edelmwReports.v0.dao.OrderBookResponseDao;
import com.edel.mw.order.edelmwReports.v0.dao.OrderLogsResponseDao;
import com.edel.mw.order.edelmwReports.v0.dao.TradeBookResponseDao;

import java.util.List;

/**
 * Created by jitheshrajan on 7/2/18.
 */
public interface OrderBookRepositoryCustom {

    long getOrdersCount(String userId);

    List<? extends BaseOrdersDocument> getOrders(String userId);

    List<? extends BaseOrdersDocument> getOrderHistory(String userId, String startDate, String endDate) throws Exception;

    List<BaseOrdersDocument> getTradeHistory(String userId, String sNestOrdNum);

    List<OrderBookResponseDao> getCurrentDayOrderBook(String userId);

    List<TradeBookResponseDao> getCurrentDayTrades(String userId);

    OrderLogsResponseDao getOrderLogs(String userId, String oid);

}
