package com.edel.stream.quoteprocessor.component;

import com.edel.rulelib.java8.dao.BaseExpression;
import com.edel.rulelib.java8.util.ExpressionHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by jitheshrajan on 10/22/18.
 */
@Component
public class QuoteProcessorStartRunner implements ApplicationRunner {

    @Value("${rule.store.rest.endpoint.all}")
    private String uri;

    private static final Logger log =
            LoggerFactory.getLogger(QuoteProcessorStartRunner.class);
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        log.info("Quote processor start runner is initializing stuff for startup");
        log.info(uri);
        loadRules(uri);

    }

    private static void loadRules(String ruleLoadUri)
    {
//        final String uri = "http://10.250.26.94:9010/rulestore-uat/exp/all";

        Type listType = new TypeToken<ArrayList<BaseExpression>>() {
        }.getType();

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(ruleLoadUri, String.class);

        ArrayList<BaseExpression> baseExpressions = new Gson().fromJson(result, listType);

        log.info(result);
        log.info(ExpressionHelper.loadRules(baseExpressions)+"");


    }
}
