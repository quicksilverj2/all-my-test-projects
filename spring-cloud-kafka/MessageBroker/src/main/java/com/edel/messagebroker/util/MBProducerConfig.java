package com.edel.messagebroker.util;


import com.msf.log.Logger;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Created by jitheshrajan on 5/21/18.
 */
public class MBProducerConfig  {

    private static final Object lock = new Object();

    private static Logger log = Logger.getLogger(MBProducerConfig.class);

    private static volatile MBProducerConfig instance;

    private String orderStreamerIp ;

    private int orderStreamerPort;

    private String topic;

    private String brokers;

    public static String getTopic(){
        return getInstance().topic;
    }

    public static String getBrokers(){
        return getInstance().brokers;
    }

    public static String getOrderStreamerIp(){
        return getInstance().orderStreamerIp;
    }

    public static int getOrderStreamerPort(){
        return getInstance().orderStreamerPort;
    }

    public static MBProducerConfig getInstance() {
        MBProducerConfig r = instance;
        if (null == r) {
            synchronized (lock) {
                r = instance;
                if (r == null) {
                    instance = r = new MBProducerConfig();
                }
            }
        }
        return r;
    }

    public void loadFile(String fileName) throws Exception {

        PropertiesConfiguration config = new PropertiesConfiguration(fileName);

        loadOrderStreamerConfig(config);

        loadKafkaParams(config);
    }

    private void loadOrderStreamerConfig(PropertiesConfiguration config) throws Exception {
        orderStreamerIp = config.getString("config.orderstreamer.ip");
        orderStreamerPort = config.getInt("config.orderstreamer.port");
    }

    private void loadKafkaParams(PropertiesConfiguration config) throws  Exception{
        topic = config.getString("config.topic.name");
        brokers = config.getString("config.kafka.brokers");
    }

}
