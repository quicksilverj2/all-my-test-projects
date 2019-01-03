package com.edel.rulestore.dao;

import com.edel.rulelib.java8.dao.BaseExpression;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 * Created by jitheshrajan on 10/12/18.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpressionDao<T> extends BaseExpression {
    @Id
    private String id;

    @Builder
    public ExpressionDao(String category, String oper, String leftParam, boolean cnst, String rightParam, Object obj, String id) {
        super(category, oper, leftParam, cnst, rightParam, obj);
        this.id = id;
    }
}
