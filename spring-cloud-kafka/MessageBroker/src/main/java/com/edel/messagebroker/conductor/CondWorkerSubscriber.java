package com.edel.messagebroker.conductor;

import com.msf.log.Logger;
import com.msf.network.BaseSubscriber;

public class CondWorkerSubscriber extends BaseSubscriber {

    private static Logger log = Logger.getLogger(CondWorkerSubscriber.class);

    @Override
    public void onMessage(String channel,String message){
        super.onMessage(channel,message);
        try{
            if(message!=null){

            }else{
                log.error("message null for channel "+channel);
            }
        }catch (Exception e){
            log.error("Exception in subscriber Thread ",e);
        }
    }

}
