package com.edel.rulestore.mongo;

import com.edel.rulestore.dao.ExpressionDao;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by jitheshrajan on 10/12/18.
 */
public interface ExpressionRepository extends MongoRepository<ExpressionDao, String>, ExpressionRepositoryCustom{
}
