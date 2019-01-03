package com.edel.messagebroker.crons;

import com.edel.messagebroker.conductor.CondWorkerSubscriber;
import com.edel.messagebroker.conductor.ConductorConfig;
import com.edel.messagebroker.conductor.ConductorDataCache;
import com.edel.messagebroker.conductor.ConductorWorkerESManger;
import com.edel.messagebroker.objects.Message;
import com.edel.messagebroker.objects.TaskAssign;
import com.edel.messagebroker.util.*;
import com.edel.messagebroker.worker.WorkerConfig;
import com.edel.messagebroker.worker.WorkerDBConnection;
import com.edel.messagebroker.worker.WorkerDataCache;
import com.msf.log.Logger;
import com.msf.network.CacheService;
import com.msf.network.StandaloneCacheService;
import com.msf.utils.constants.CacheConstants;
import redis.clients.jedis.exceptions.JedisException;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

public class WorkerMain {

    private static Logger log = Logger.getLogger(WorkerMain.class);

    public static void main(String args[]){
        System.out.println("Worker");

        String configFile = "config/config.properties";
        Properties jsLogProperties = new Properties();
        String jslogFile = "config/jslog.properties";
        try {
            jsLogProperties.load(new FileInputStream(jslogFile));
            Logger.setLogger(jsLogProperties,"[%d{yyyy-MM-dd HH:mm:ss.SSS}] [%t] %5p - %m %n");
        } catch (Exception e) {
            System.out.println("cannot load jsLog: " + jslogFile);
            System.exit(-1);
        }
        WorkerMain.log = Logger.getLogger(WorkerMain.class);

        try{
            WorkerConfig.getInstance().loadFile(configFile);
        }catch (Exception  e){
            log.error("Exception in loading app config ",e);
            System.exit(-1);
        }

        ConductorWorkerESManger.getInstance();
        WorkerDBConnection.getInstance().loadEmtSettings(WorkerConfig.getInstance().getEmtConnParam());
        WorkerDataCache.getInstance();


        final ArrayList<String> channels = new ArrayList<>();
        String rediskey = ConductorConfig.getInstance().getRedisChannelBaseString()+ CacheConstants.REDIS_CHANNEL_PENDING_ORDERS;
        channels.add(rediskey);
        channels.add(ConductorConfig.getInstance().getRedisChannelBaseString()+CacheConstants.REDIS_CHANNEL_PND_ORDERS_TIMER);

        CondWorkerSubscriber subscriber = new CondWorkerSubscriber();

        Thread subscriberThread = new Thread(() -> {
            boolean isCompleted = false;
            while(!isCompleted){
                try {
                    log.debug("Subscribing to " + channels);
                    StandaloneCacheService.getInstance().subscribe(subscriber,channels);
                    log.debug("Subscribing ended ");
                    isCompleted = true;
                }catch (JedisException e){
                    isCompleted = false;
                    log.error("Jedis Main Thread Exception ",e);

                    if(ConductorDataCache.getInstance().isCompleted()){
                        isCompleted = true;
                        log.debug("is Completed is true so subcriber thread is sleeping");
                    }else{
                        try{
                            Thread.sleep(ConductorConfig.getInstance().getReconnectSubscriber());
                        }catch (Exception es){
                            log.error("Exception ",es);
                        }
                    }
                }catch (Exception e){
                    isCompleted = true;
                    log.debug("Subscribing failed."+e);
                }
            }
        });
        subscriberThread.setName("SubscriberThread:-");
        subscriberThread.start();

        try{
            String response = StandaloneCacheService.getInstance().getValueWithException(rediskey);
            if(response==null){
                // Need to Wait from subscriber
            }else if(response.equalsIgnoreCase(MBAppConstants.PendingTasks.STARTED)){
                // No need to wait for Subscriber
            }else if(response.equalsIgnoreCase(MBAppConstants.PendingTasks.COMPLETED)){
                log.debug("Already Task is completed so Exiting");
            }
        }catch (Exception e){
            log.error("Exception ",e);
        }

        WorkerEngine engine = new WorkerEngine();
        Message<TaskAssign> taskAssignMessage = engine.startprocess();

        try{
            subscriber.unsubscribe();
        }catch (Exception e){
            log.error("Exception in unsubscription ",e);
        }
        ConductorWorkerESManger.getInstance().shutDownAsnycESAndAwaitTermination();
        CacheService.getInstance().closeConnection();
        StandaloneCacheService.getInstance().closeConnection();
        WorkerDBConnection.getInstance().closeConnections();
        log.debug("complete load finish");
    }


}
