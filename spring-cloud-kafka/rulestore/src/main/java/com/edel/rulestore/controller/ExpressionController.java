package com.edel.rulestore.controller;

import com.edel.rulelib.java8.RuleStore;
import com.edel.rulelib.java8.execution.ExecutionEvent;
import com.edel.rulelib.java8.execution.RulesProcessor;
import com.edel.rulelib.java8.impl.AllowedExpressionOperators;
import com.edel.rulelib.java8.rule.BaseRule;
import com.edel.rulestore.dao.ExpressionDao;
import com.edel.rulestore.dao.StreamingQuotePacket;
import com.edel.rulestore.mongo.ExpressionRepository;
import com.edel.rulestore.mongo.ExpressionRepositoryImpl;
import com.google.gson.Gson;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by jitheshrajan on 10/12/18.
 */
@RestController("ExpressionController")
@RequestMapping("/exp")
public class ExpressionController {


    private ExpressionRepositoryImpl expressionRepositoryImpl;

    @Autowired
    private ExpressionRepository expressionRepository;

    public ExpressionController(ExpressionRepositoryImpl expressionRepositoryImpl) {
        this.expressionRepositoryImpl = expressionRepositoryImpl;
    }


    @GetMapping("/all")
    @ApiResponse(code = 200, message = "Some success Message", response = ExpressionDao.class)
    public List<ExpressionDao> getAll(@RequestParam(required = false, value = "usr") String userId) {
        List<ExpressionDao> expressionDaos = this.expressionRepository.findAll();
        return expressionDaos;
    }

    @GetMapping("/count")
    public long getCount(){
        return this.expressionRepository.count();
    }

    @GetMapping("/load")
    public boolean loadRules(){

        // todo move this into an application start based setup!

        List<ExpressionDao> expressionDaos = this.expressionRepository.findAll();

        // using primitive type while loading!
        // todo check with napender or shekar!
        List<BaseRule> rules = new ArrayList<>();

        for(ExpressionDao expressionDao : expressionDaos) {

            BaseRule baseRule = null;
            baseRule = AllowedExpressionOperators.valueOf(expressionDao.getOper())
                    .getRule(expressionDao.getCategory(),
                            expressionDao.getLeftParam(),
                            expressionDao.getRightParam(),
                            expressionDao.isCnst());

            rules.add(baseRule);
        }

        return RuleStore.getInstance().reloadRules(rules);
    }

    @PutMapping
    public ExpressionDao insert(@RequestBody ExpressionDao expression) throws Exception {




        ExpressionDao newExpression;
        if(AllowedExpressionOperators.valueOf(expression.getOper())!= null){
            newExpression = this.expressionRepository.insert(expression);
        }else{
            throw new Exception("Unsupported Operation alert! Please contact backend team");
        }

        // Calling load rules again since a new rule was added
        loadRules();

        return newExpression;


    }

    @PostMapping
    public ExpressionDao update(@RequestBody ExpressionDao newEvent){



        if(newEvent.getId() != null){
            Optional<ExpressionDao> opEvent =  this.expressionRepository.findById(newEvent.getId());
            if(opEvent != null && opEvent.isPresent()){
                ExpressionDao existingEvent = opEvent.get();

                if(AllowedExpressionOperators.valueOf(existingEvent.getOper())!= null){
                    System.out.println("Rule can be updated!");
                }
                // else case not necessary since this will throw an exception!
            }

        }
        ExpressionDao save = this.expressionRepository.save(newEvent);
        // Calling load rules again since a new rule was added
        loadRules();

        return save;
    }

    // this is a test method to check how the rule executes!
    // ideally the rule will be tested against packets in a streamtoqueue module separately!
    @PostMapping("/process/{category}")
    public List<ExecutionEvent> compareValuesWithRule(
            @RequestBody String jsonString,
            @PathVariable("category") String category){

        RulesProcessor rulesProcessor = new RulesProcessor();

        System.out.println(jsonString);
        StreamingQuotePacket streamingQuotePacket = new Gson().fromJson(jsonString, StreamingQuotePacket.class);

        System.out.println(streamingQuotePacket.toString());

        List<ExecutionEvent> executionEvents = rulesProcessor.processMatches(streamingQuotePacket, category);

        for(ExecutionEvent executionEvent : executionEvents){
            System.out.println(executionEvent.toString());
        }

        return executionEvents;
    }


    @GetMapping("/operators")
    public List<AllowedExpressionOperators> getAllowedOperators() {
        return Arrays.asList(AllowedExpressionOperators.values());
    }



}
