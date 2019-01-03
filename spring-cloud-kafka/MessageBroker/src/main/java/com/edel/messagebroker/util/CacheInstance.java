package com.edel.messagebroker.util;

import com.edel.notification.NotificationData;
import com.msf.log.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CacheInstance {

    private static Logger log = Logger.getLogger(CacheInstance.class);

    private static volatile CacheInstance instance;

    private static final Object lock = new Object();

    private static final ReadWriteLock stopProcessLock = new ReentrantReadWriteLock();

    private boolean shouldStop = false;

    public static boolean shouldStop(){
        boolean stop ;
        stopProcessLock.readLock().lock();
        stop = getInstance().shouldStop;
        stopProcessLock.readLock().unlock();
        return stop;
    }

    public static void setStopProcess(boolean stopProcess){
        stopProcessLock.writeLock().lock();
        getInstance().shouldStop = stopProcess;
        stopProcessLock.writeLock().unlock();
    }

    public static CacheInstance getInstance(){
        CacheInstance r = instance;
        if(r==null){
            synchronized (lock){
                r = instance;
                if(r==null){
                    instance = r = new CacheInstance();
                }
            }
        }
        return r;
    }

    private CacheInstance(){
        log.debug("Cache Instance Initialized ");
    }

    private ConcurrentHashMap<String,String> preferredAccountMap = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, NotificationData> notiSettings = new ConcurrentHashMap<>();

    public static NotificationData getNotificationSettings(String userId,String accTyp){
        return getInstance().notiSettings.get(userId+MBAppConstants.SEPERATOR+accTyp);
    }

    public static void setNotificationObject(String userId,String accTyp,NotificationData notificationObject){
        getInstance().notiSettings.put(userId+MBAppConstants.SEPERATOR+accTyp,notificationObject);
    }

    public static String getPrefAccType(String userid){
        return getInstance().preferredAccountMap.get(userid);
    }

    public static void setPrefAccType(String userid,String accType){
        getInstance().preferredAccountMap.put(userid,accType);
    }
}
