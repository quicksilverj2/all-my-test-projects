package com.edel.rulelib.java8;

/**
 * Created by jitheshrajan on 10/11/18.
 */

import com.edel.rulelib.java8.rule.BaseRule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Singleton class which will act as a repository to collect all rules from the database and store it
 * in memory. If there is an update on the rules from the db side, we will expect a refresh of the rule store.
 */
public class RuleStore {

    private static volatile RuleStore instance;
    private static final Object lock = new Object();

    private static final Object mapLock = new Object();

    private Map<String, List<BaseRule>> ruleMap  = null;

    private RuleStore() {
        ruleMap = new HashMap<>();
    }

    public static RuleStore getInstance(){
        RuleStore r = instance;
        if(r==null){
            synchronized (lock) {
                r = instance;
                if(r == null){
                    System.out.println("Rule Store Initialized");
                    instance = r = new RuleStore();
                }
            }
        }
        return r;
    }

    public List<BaseRule> getRulesForCategory(String category){
        return ruleMap.get(category);
    }


    /**
     * This method is to be used by any application which consumes this Library for loading rules into
     * memory.
     *
     * @param rules
     * @return
     */
    public boolean loadRules(List<BaseRule> rules){

        try{


            for(BaseRule baseRule : rules){
                if(loadRule(baseRule)){
                    System.out.println("Rule added successully");
                }else{
                    System.err.println(baseRule.toString() + " wasnt loaded");
                }
            }
        }catch (Exception e){
            System.err.println("Error while loading rules!");
            return false;
        }

        return true;
    }

    public boolean reloadRules(List<BaseRule> rules){

        HashMap<String, List<BaseRule>> newRuleMap = new HashMap<>();

        try{

            for(BaseRule baseRule : rules){
                if(loadRule(baseRule, newRuleMap)){
                    System.out.println("Rule added successully");
                }else{
                    System.err.println(baseRule.toString() + " wasnt loaded");
                }
            }
        }catch (Exception e){
            System.err.println("Error while loading rules!");
            return false;
        }

        synchronized (mapLock){
            ruleMap = newRuleMap;
        }

        return true;
    }


    /**
     * This method to be used while reloading rules from RuleStore
     * @param baseRule
     * @param newMap
     * @return
     */
    public boolean loadRule(BaseRule baseRule, Map<String, List<BaseRule>> newMap){
        try{
            if(newMap.get(baseRule.getRuleCategory())!=null){
                newMap.get(baseRule.getRuleCategory()).add(baseRule);
            }else{
                List<BaseRule> categoryRuleList =  new ArrayList<>();
                categoryRuleList.add(baseRule);
                newMap.put(baseRule.getRuleCategory(), categoryRuleList);
            }
        }catch (Exception e){
            System.err.println("Error while loading rule!");
            return false;
        }

        return true;
    }

    /**
     * This is to be used while loading rules only for the first time!
      * @param baseRule
     * @return
     */
    public boolean loadRule(BaseRule baseRule){
        try{

            synchronized (mapLock){
                if(ruleMap.get(baseRule.getRuleCategory())!=null){
                    ruleMap.get(baseRule.getRuleCategory()).add(baseRule);
                }else{
                    List<BaseRule> categoryRuleList =  new ArrayList<>();
                    categoryRuleList.add(baseRule);
                    ruleMap.put(baseRule.getRuleCategory(), categoryRuleList);
                }
            }

        }catch (Exception e){
            System.err.println("Error while loading rule!");
            return false;
        }

        return true;
    }


}
