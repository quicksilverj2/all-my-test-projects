package com.edel.messagebroker.consumer;

import com.msf.log.Logger;
import org.apache.commons.configuration.PropertiesConfiguration;

public class MoEnConsumerConfig {

    private static Logger log = Logger.getLogger(MoEnConsumerConfig.class);

    private static volatile MoEnConsumerConfig instance;

    private static final Object lock = new Object();

    private String topic;

    private String brokers;

    private String groupID;

    public static String getTopic(){
        return getInstance().topic;
    }

    public static String getBrokers(){
        return getInstance().brokers;
    }

    public static String getGroupID(){
        return getInstance().groupID;
    }

    public static MoEnConsumerConfig getInstance() {
        MoEnConsumerConfig r = instance;
        if(r==null){
            synchronized (lock){
                r = instance;
                if(r==null){
                    instance = r = new MoEnConsumerConfig();
                }
            }
        }
        return r;
    }

    private MoEnConsumerConfig(){
        log.debug("MB Consumer Config Initialized ");
    }

    public void loadFile(String fileName) throws Exception {
        PropertiesConfiguration config = new PropertiesConfiguration(fileName);
        loadKafkaParams(config);
    }

    private void loadKafkaParams(PropertiesConfiguration config) throws  Exception{
        topic = config.getString("config.topic.name");
        brokers = config.getString("config.kafka.brokers");
        groupID = config.getString("config.kakfka.groupid");
    }
}
