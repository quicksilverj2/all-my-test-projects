package com.edel.messagebroker.crons;

import com.edel.messagebroker.conductor.ConductorConfig;
import com.edel.messagebroker.conductor.ConductorDBConnection;
import com.edel.messagebroker.conductor.ConductorDataCache;
import com.edel.services.constants.AppConstants;
import com.msf.log.Logger;

import java.io.FileInputStream;
import java.util.Properties;

public class ConductorMain {

    private static Logger log = Logger.getLogger(ConductorMain.class);

    public static void main(String[] args){

        System.out.println("conductor");

        String exc = args[0];

        int maxSize = 0;
        if(exc.equalsIgnoreCase(AppConstants.EXCHANGE_NSE)){
            maxSize = ConductorConfig.getNseMaxSize();
        }else if( exc.equalsIgnoreCase(AppConstants.EXCHANGE_CDS)){
            maxSize = ConductorConfig.getCdsMaxSize();
        } else{
            System.out.println("Invalid exc "+exc);
            System.exit(-1);
        }

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

        ConductorMain.log = Logger.getLogger(ConductorMain.class);

        try{
            ConductorConfig.getInstance().loadFile(configFile);
        }catch (Exception e){
            log.error("Exception in config load ",e);
            System.exit(-1);
        }

        try{
            ConductorDBConnection.getInstance().loadEmtSettings(ConductorConfig.getEmtConnParam());
        }catch (Exception e){
            log.error("Exception in DB Load ",e);
            System.exit(-1);
        }

        ConductorDataCache.getInstance().initialize(maxSize,
                ConductorConfig.getPortBind(),
                ConductorConfig.getQueueLength(),
                ConductorConfig.getIpBind(),exc);

        while (true){
            try{
                log.debug("Going to Sleep");
                Thread.sleep(10000000);
            }catch (Exception e){
                log.error("Exception ",e);
            }
        }
    }
}
