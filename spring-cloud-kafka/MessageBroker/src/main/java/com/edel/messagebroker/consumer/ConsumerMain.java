package com.edel.messagebroker.consumer;

import com.edel.messagebroker.util.MBConsExecutorServiceManager;
import com.edel.messagebroker.worker.SubscriberWorker;
import com.msf.log.Logger;
import com.msf.network.CacheService;
import com.msf.network.QuoteSentinelCache;
import com.msf.network.StandaloneCacheService;
import com.msf.utils.constants.CacheConstants;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

public class ConsumerMain {

    private static Logger log = Logger.getLogger(ConsumerMain.class);

    public static void main(String args[]){
        System.out.println("consumer");

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
        ConsumerMain.log = Logger.getLogger(ConsumerMain.class);

        try{
            MBConsumerConfig.getInstance().loadFile(configFile);
        }catch (Exception e){
            log.error(" App Config Exception ",e);
            System.out.println("Exception in loading App Config "+e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }

        MBConsExecutorServiceManager.getInstance();

        ArrayList<String> redisChannels = new ArrayList<>();
        redisChannels.add(MBConsumerConfig.getRedisBaseString()+ CacheConstants.REDIS_CHANNEL_GET_NOTIFICATION);
        redisChannels.add(MBConsumerConfig.getRedisBaseString()+ CacheConstants.REDIS_CHANNEL_SET_NOTIFICATION);
        redisChannels.add(MBConsumerConfig.getRedisBaseString()+ CacheConstants.REDID_CHANNEL_MSG_PROCESS_STOP);
        MBConsumerSubscriber subscriber = new MBConsumerSubscriber(MBConsumerConfig.getRedisBaseString());
        SubscriberWorker subscriberWorker = new SubscriberWorker(redisChannels,subscriber,MBConsumerConfig.getReconnectSubscriber());
        Thread subscriberThread = new Thread(subscriberWorker);
        subscriberThread.setName("SThread:");
        subscriberThread.start();

        try{
            for(int i=0;i<3;i++){
                ConsumerEngine consumerEngine = new ConsumerEngine();
                Thread newThread = new Thread(consumerEngine);
                newThread.setName("newThread");
                newThread.start();
                newThread.join();
            }
        }catch (Exception e){
            log.error("Exception in config load "+e);
        }

        subscriberThread.interrupt();

        try{
            Thread.sleep(1000);
            subscriber.unsubscribe();
        }catch (Exception e){
            log.error("Exception ",e);
        }

        MBConsExecutorServiceManager.getInstance().shutDownAsnycESAndAwaitTermination();
        QuoteSentinelCache.getInstance().closeConnection();
        StandaloneCacheService.getInstance().closeConnection();
        CacheService.getInstance().closeConnection();

        log.debug("Safe Exit Done");

        // Not Exiting Main as monit starts automatically
        while(true){
            try{
                Thread.sleep(1000000);
            }catch (Exception e){
                log.error("Exception ",e);
            }
        }
    }
}
