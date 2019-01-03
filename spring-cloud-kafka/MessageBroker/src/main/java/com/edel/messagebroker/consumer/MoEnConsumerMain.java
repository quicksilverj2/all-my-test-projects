package com.edel.messagebroker.consumer;

import com.msf.log.Logger;

import java.io.FileInputStream;
import java.util.Properties;

public class MoEnConsumerMain {

    private static Logger log = Logger.getLogger(ConsumerMain.class);

    public static void main(String args[]) {
        System.out.println("Moengage Consumer Main");

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
        MoEnConsumerMain.log = Logger.getLogger(MoEnConsumerMain.class);

        try{
            MoEnConsumerConfig.getInstance().loadFile(configFile);
        }catch (Exception e){
            log.error(" App Config Exception ",e);
            System.out.println("Exception in loading App Config "+e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }

        try{
            for(int i=0;i<1;i++){
                MoEnConsumerEngine consumerEngine = new MoEnConsumerEngine();
                Thread newThread = new Thread(consumerEngine);
                newThread.setName("newThread");
                newThread.start();
                newThread.join();
            }
        }catch (Exception e){
            log.error("Exception in config load "+e);
        }
    }
}
