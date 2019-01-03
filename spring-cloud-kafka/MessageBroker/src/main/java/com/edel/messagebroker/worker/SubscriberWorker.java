package com.edel.messagebroker.worker;

import com.edel.messagebroker.consumer.MBConsumerSubscriber;
import com.msf.log.Logger;
import com.msf.network.StandaloneCacheService;
import redis.clients.jedis.exceptions.JedisException;

import java.util.ArrayList;

public class SubscriberWorker implements Runnable {

    private static Logger log = Logger.getLogger(SubscriberWorker.class);

    private ArrayList<String> redisChannels;

    private int reConnectTime;

    private MBConsumerSubscriber subscriber;

    public SubscriberWorker(ArrayList<String> redisChannels,MBConsumerSubscriber subscriber,int reConnectTime){
        this.redisChannels = redisChannels;
        this.subscriber = subscriber;
        this.reConnectTime = reConnectTime;
    }

    @Override
    public void run(){
        boolean isCompleted = false;
        while(!isCompleted){
            try {
                log.debug("Subscribing to " + redisChannels);
                StandaloneCacheService.getInstance().subscribe(subscriber,redisChannels);
                log.debug("Subscribing ended ");
                isCompleted = true;
            }catch (JedisException e){
                isCompleted = false;
                log.error("Jedis Main Thread Exception ",e);
                try{
                    Thread.sleep(reConnectTime);
                }catch (Exception es){
                    log.error("Exception ",es);
                }
            }catch (Exception e){
                isCompleted = true;
                log.debug("Subscribing failed."+e);
            }
        }

    }
}
