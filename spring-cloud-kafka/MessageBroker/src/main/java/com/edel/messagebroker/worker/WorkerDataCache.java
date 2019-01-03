package com.edel.messagebroker.worker;

import com.edel.notification.NotificationData;
import com.edel.services.constants.AppConstants;
import com.msf.log.Logger;
import com.msf.utils.constants.CacheConstants;

import java.util.HashMap;

public class WorkerDataCache {

    private static volatile WorkerDataCache instance;

    private static final Object lock = new Object();

    private static Logger log = Logger.getLogger(WorkerDataCache.class);

    private boolean userDataFlag;

    private final Object userDataFlagLock = new Object();

    private HashMap<String,Boolean> signalMap = new HashMap<>();

    private final Object signalMapLock = new Object();

    private HashMap<String, NotificationData> userNotiData = new HashMap<>();

    private String exc;

    public static WorkerDataCache getInstance(){
        WorkerDataCache r = instance;
        if(r==null){
            synchronized (lock){
                r = instance;
                if(r==null){
                    instance = r= new WorkerDataCache();
                }
            }
        }
        return r;
    }

    public void setExc(String exc){
        this.exc = exc;
    }

    private WorkerDataCache(){
        log.debug("Worker Data cache Initialized");
        signalMap.put(AppConstants.EXCHANGE_NSE,false);
        signalMap.put(AppConstants.EXCHANGE_CDS,false);
    }

    public void setSignalMapForExc(String exc,Boolean flag){
        synchronized (signalMapLock){
            signalMap.put(exc,flag);
        }
    }

    public Boolean getSignalMap(String exc){
        Boolean flag = false;
        synchronized (signalMapLock){
            flag = signalMap.get(exc);
        }
        return flag;
    }

    public void setUserDataFlag(boolean userDataFlag){
        synchronized (userDataFlagLock){
            this.userDataFlag = userDataFlag;
        }
    }

    public boolean getUserDataFlag(){
        boolean usrDataFlg  =false;
        synchronized (userDataFlagLock){
            usrDataFlg = userDataFlag;
        }
        return usrDataFlg;
    }

    public void addUserNotiData(String accId,String accTyp,NotificationData notificationData){
        String key = accId+ CacheConstants.REDIS_KEY_SEPERATOR+accTyp;
        userNotiData.put(key,notificationData);
    }
}
