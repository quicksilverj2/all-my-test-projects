package com.edel.messagebroker.producer;

import com.edel.messagebroker.util.MBProducerConfig;
import com.msf.log.Logger;

import java.io.FileInputStream;
import java.util.Properties;

public class ProducerMain {
    private static Logger log = Logger.getLogger(ProducerMain.class);


    public static void main(String args[]){
        System.out.println("producer");

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

        ProducerMain.log = Logger.getLogger(ProducerMain.class);

        try{
            MBProducerConfig.getInstance().loadFile(configFile);
        }catch (Exception e){
            log.error(" App Config Exception ",e);
            System.out.println("Exception in loading App Config "+e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }

        OrderStreamConnector orderStreamConnector = new OrderStreamConnector();
        orderStreamConnector.startListening();
    }
}
