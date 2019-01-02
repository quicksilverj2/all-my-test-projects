package com.edel.mw.order.edelmwReports.v1.mongo;

import com.edel.mw.order.edelmwReports.common.dao.BaseOrdersDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by jitheshrajan on 7/2/18.
 */
public interface OrderBookRepositoryV1 extends MongoRepository<BaseOrdersDocument, String>, OrderBookRepositoryV1Custom {

}
