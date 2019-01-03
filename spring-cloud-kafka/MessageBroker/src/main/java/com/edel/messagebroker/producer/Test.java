package com.edel.messagebroker.producer;

import com.edel.messagebroker.objects.Message;
import com.edel.messagebroker.objects.TaskAssign;
import com.edel.messagebroker.util.MBAppConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class Test {

    public static void main(String args[]){
//        DefaultAction defaultAction = new DefaultAction();
////        defaultAction.setType("type1");
////        defaultAction.setValue("value1");
////        HashMap<String,String> kvPairs = new HashMap<>();
////        kvPairs.put("one","oasda");
////        defaultAction.setKvPairs(kvPairs);
////
////        Gson gson = new Gson();
////        String ads = gson.toJson(defaultAction, DefaultAction.class);
////
////        System.out.println(ads);

        TaskAssign taskAssign = new TaskAssign(1);
        Message<TaskAssign> message = new Message<>();
        message.setData(taskAssign);
        message.setTyp(MBAppConstants.TaskTypes.INITIATE);
        Type collectionType = new TypeToken<Message<TaskAssign>>(){}.getType();
        Gson gson = new Gson();
        String json = gson.toJson(message,collectionType);
        System.out.println(json);
    }
}
