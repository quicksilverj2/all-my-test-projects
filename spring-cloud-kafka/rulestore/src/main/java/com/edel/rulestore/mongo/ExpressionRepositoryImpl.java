package com.edel.rulestore.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;

/**
 * Created by jitheshrajan on 10/12/18.
 */
public class ExpressionRepositoryImpl {

    private final MongoOperations mongoOperations;

    @Autowired
    public ExpressionRepositoryImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }
}
