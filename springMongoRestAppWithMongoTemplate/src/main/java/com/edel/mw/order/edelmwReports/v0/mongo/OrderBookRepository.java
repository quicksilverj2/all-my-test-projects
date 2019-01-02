package com.edel.mw.order.edelmwReports.v0.mongo;

import com.edel.mw.order.edelmwReports.common.dao.BaseOrdersDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by jitheshrajan on 7/2/18.
 */
public interface OrderBookRepository extends MongoRepository<BaseOrdersDocument, String>, OrderBookRepositoryCustom {

}
