package com.edel.messagebroker.util;

import com.edel.messagebroker.Tasks.BaseTask;
import com.edel.messagebroker.Tasks.ConNotiSettingTask;
import com.edel.messagebroker.conductor.ConductorDataCache;
import com.edel.messagebroker.consumer.MBConsumerConfig;
import com.edel.messagebroker.modules.TargetUserAttributes;
import com.edel.messagebroker.objects.BasicMessage;
import com.edel.messagebroker.objects.Message;
import com.edel.notification.NotificationData;
import com.edel.services.constants.AppConstants;
import com.edel.tradeconfirmation.objects.UserIdentifier;
import com.google.gson.Gson;
import com.msf.log.Logger;
import com.msf.network.CacheService;
import com.msf.network.StandaloneCacheService;
import com.msf.utils.constants.CacheConstants;
import com.msf.utils.helper.Helper;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.BufferedReader;
import java.net.Socket;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class MBHelper {
    private static Logger log = Logger.getLogger(MBHelper.class);

    public static void closeBufferReader(BufferedReader bufferedReader){
        try{
            if(bufferedReader!=null){
                bufferedReader.close();
            }
        }catch (Exception e){
            log.error("Exception while closing buffer reader ",e);
        }
    }

    public static void closeSocket(Socket socket){
        try{
            if(socket!=null){
                socket.close();
            }
        }catch (Exception e){
            log.error("Exception while closing socket ",e);
        }
    }

    public static String getAccType(String userId){
        String accType = CacheInstance.getPrefAccType(userId);
        if(accType==null){
            log.debug("Pref Acc Not Present in Cache");
            String redisKey = new StringBuilder(MBConsumerConfig.getRedisSpecificPattern()).append(CacheConstants.REDIS_LOGIN_USERID).toString();
            try{
                accType = CacheService.getInstance().hashGet(redisKey,userId);
                log.debug("Pref acctype in redis "+accType + " redis Key "+redisKey);
                if(accType==null) {
                    accType = MBAppConstants.NO_PRESENT;
                }
                CacheInstance.setPrefAccType(userId,accType);
                return accType;
            }catch (Exception e){
                log.error("Exception  ",e);
                return MBAppConstants.EXCEPTION;
            }
        }else{
            log.debug("Pref Acc  Present in Cache "+accType);

            return accType;
        }
    }

    public static void handleNotification(NotificationData notificationObject){
        if(notificationObject.isValid()){
            CacheInstance.setNotificationObject(notificationObject.getUsrID(),notificationObject.getActyp(),notificationObject);
        }else{
            log.error("Invalid Notification");
        }
    }

    public static void handleLoginRequest(UserIdentifier userIdentifier) throws  Exception{
        log.debug("Handle Login Request");
        if(userIdentifier.isValid()){
            String accType = CacheInstance.getPrefAccType(userIdentifier.getUserId());
            NotificationData notificationData = null;
            if(accType==null || accType.equalsIgnoreCase(MBAppConstants.EXCEPTION)){
                log.debug("Pref Acctype for userid is null ");
                // start
            }else if(accType.equalsIgnoreCase(MBAppConstants.NO_PRESENT)){
                log.debug("No  Userid present "+userIdentifier.getUserId());
                // return
                return;
            }else if (accType.equalsIgnoreCase(CacheConstants.ACCOUNT_BOTH)){
                // if both check for both account type of notifications
                notificationData = CacheInstance.getNotificationSettings(userIdentifier.getUserId(),CacheConstants.ACCOUNT_EQUITY);
                if(notificationData!=null){
                    notificationData = CacheInstance.getNotificationSettings(userIdentifier.getUserId(),CacheConstants.ACCOUNT_COMM);
                }
            }else{
                notificationData = CacheInstance.getNotificationSettings(userIdentifier.getUserId(),accType);
            }

            if(notificationData==null){
                ConNotiSettingTask conNotiSettingTask = new ConNotiSettingTask(
                        userIdentifier.getUserId());

                Future<String> obj = MBHelper.submitTask(MBConsExecutorServiceManager.getAsyncExecutorService(),
                        conNotiSettingTask);
                if(obj==null){
                    conNotiSettingTask.process();
                }
//                MBHelper.runTask(obj,conNotiSettingTask);
            }
        }else{
            log.debug("Invalid Userid ");
        }
    }

//    public static void handleLoginRequest(UserIdentifier userIdentifier) throws Exception{
//        if(userIdentifier.isValid()){
//            String accType = CacheInstance.getPrefAccType(userIdentifier.getUserId());
//            if(accType==null || accType.equalsIgnoreCase(MBAppConstants.EXCEPTION)){
//                accType = userIdentifier.getAccTyp();
//                CacheInstance.setPrefAccType(userIdentifier.getUserId(),accType);
//            } else if(accType.equalsIgnoreCase(CacheConstants.ACCOUNT_BOTH)||accType.equalsIgnoreCase(userIdentifier.getAccTyp())){
//
//            } else{
//                log.debug("Acctype in cache is not matching to logged in one "+userIdentifier.getUserId() +" actual acctype "+userIdentifier.getAccTyp()+" logged in acctype ");
//                return;
//            }
//
//            NotificationData notificationObject = CacheInstance.getNotificationSettings(userIdentifier.getUserId(),userIdentifier.getAccTyp());
//            if(notificationObject==null){
//
//                ConNotiSettingTask conNotiSettingTask = new ConNotiSettingTask(
//                        userIdentifier.getUserId(),
//                        userIdentifier.getAccTyp());
//
//                Future<String> obj = MBHelper.submitTask(MBConsExecutorServiceManager.getAsyncExecutorService(),
//                        conNotiSettingTask);
//                MBHelper.runTask(obj,conNotiSettingTask);
////                getNotificationObject(userIdentifier.getUserId(),userIdentifier.getAccTyp());
//            }
//        }else{
//            log.error("Invalid User Identifier ");
//        }
//    }

    public static TargetUserAttributes getUserAttributes(String accid){
        TargetUserAttributes targetUserAttributes  = new TargetUserAttributes();
        targetUserAttributes.setAttribute("USER_ATTRIBUTE_UNIQUE_ID");
        targetUserAttributes.setComparisonParameter("is");
        targetUserAttributes.setAttributeValue(accid);
        return targetUserAttributes;
    }

    public static long getTradeClosingTime(String symbol){
        if(symbol.contains(AppConstants.EXCHANGE_NSE) || symbol.contains(AppConstants.EXCHANGE_BSE) || symbol.contains(AppConstants.EXCHANGE_NFO)){
            return MBConsumerConfig.getNseLstTm();
        }else if(symbol.contains(AppConstants.EXCHANGE_CDS)){
            return MBConsumerConfig.getCdsLstTm();
        }else{
            return 0;
        }
    }

    public static String getRedisField(String symbol){
        if(symbol.contains(AppConstants.EXCHANGE_NSE) || symbol.contains(AppConstants.EXCHANGE_BSE) || symbol.contains(AppConstants.EXCHANGE_NFO)){
            return CacheConstants.REDIS_OPENORDERS_NSE;
        }else if(symbol.contains(AppConstants.EXCHANGE_CDS)){
            return CacheConstants.REDIS_OPENORDERS_CDS;
        }else{
            return null;
        }
    }

    public static String getTradeMessage(TradeDataPacket tradeDataPacket,NotificationData notificationData){
        String placeHolderString = MBConsumerConfig.getTradeMessage();
        placeHolderString = placeHolderString.replace(MBAppConstants.NAME_PLACEHOLDER,notificationData.getAccName());
        placeHolderString = placeHolderString.replace(MBAppConstants.ORDER_TYPE_PLACEHOLDER,Helper.toCamelCase(tradeDataPacket.getData().getTTyp()));
        placeHolderString = placeHolderString.replace(MBAppConstants.SYMBOL_PLACEHOLDER,tradeDataPacket.getData().getDpName());
        placeHolderString = placeHolderString.replace(MBAppConstants.RATIO_PLACEHOLDER,tradeDataPacket.getData().getFQty()+"/"+tradeDataPacket.getData().getQty());
        placeHolderString = placeHolderString.replace(MBAppConstants.PRICE_PLACEHOLDER,tradeDataPacket.getData().getFPrc());

        placeHolderString = placeHolderString.replace(MBAppConstants.TIME_PLACEHOLDER,tradeDataPacket.getData().getRcvTim());
        return placeHolderString;
    }

    public static String getTradePndMessage(OrderDataPacket orderDataPacket){
        String phString = MBConsumerConfig.getTrdPendMsg();
        phString = phString.replace(MBAppConstants.NAME_PLACEHOLDER,"");
        phString = phString.replace(MBAppConstants.ORDER_TYPE_PLACEHOLDER,Helper.toCamelCase(orderDataPacket.getData().getTTyp()));
        phString = phString.replace(MBAppConstants.SYMBOL_PLACEHOLDER,orderDataPacket.getData().getDpName());
        phString = phString.replace(MBAppConstants.RATIO_PLACEHOLDER,orderDataPacket.getData().getRQty()+"/"+orderDataPacket.getData().getTQty());
        phString = phString.replace(MBAppConstants.PRICE_PLACEHOLDER,orderDataPacket.getData().getPrc());
        return phString;
    }

    public static NotificationData shouldSendNotification(TradeDataPacket tradeDataPacket){

        String accType = getAccType(tradeDataPacket.getUsrId());
        NotificationData notificationObject = null;
        if(accType.equalsIgnoreCase(CacheConstants.ACCOUNT_EQUITY) || accType.equalsIgnoreCase(CacheConstants.ACCOUNT_COMM)){
            notificationObject = getNotificationObject(tradeDataPacket.getUsrId(),accType);
        }else if(accType.equalsIgnoreCase(CacheConstants.ACCOUNT_BOTH)){
            notificationObject = getNotificationObject(tradeDataPacket.getUsrId(),tradeDataPacket.getAccTyp());
        }else{
            log.debug("Ignorning to send notification as userid is not present in redis "+tradeDataPacket.getUsrId());
            return null;
        }
        if(tradeDataPacket.getData().getFQty().equalsIgnoreCase(tradeDataPacket.getData().getQty())){

            if(notificationObject.isCompltTrade()||notificationObject.isPartialTrade()){
                return notificationObject;
            }else{
                log.debug("No Notification for user "+tradeDataPacket.getUsrId());
                notificationObject = null;
            }
        }else{
            if(notificationObject.isPartialTrade()){
                return notificationObject;
            }else{
                log.debug("No Partial Trade for user "+tradeDataPacket.getUsrId());
                return null;
            }
        }
        return notificationObject;
    }

    public static String getCloseMessage(){
        BasicMessage basicMessage = new BasicMessage(MBAppConstants.TaskTypes.CLOSE);

        Message<BasicMessage> message = new Message<BasicMessage>();
        message.setExc(ConductorDataCache.getInstance().getExc());
        message.setTyp(MBAppConstants.TaskTypes.CLOSE);
        message.setData(basicMessage);
        Gson gson = new Gson();
        String json = gson.toJson(message,Message.class);
        return json;
    }

    public static Long setExpiryTillEOD(String time,String redisQuery){
        try{
            String[] timeArray = time.split(":");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 1);
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
            calendar.set(Calendar.SECOND, Integer.parseInt(timeArray[2]));

            long expiry = calendar.getTimeInMillis() - System.currentTimeMillis();
            log.debug("expiry " +expiry + " redisKey " +redisQuery);
            Long response = StandaloneCacheService.getInstance().setExpiry(redisQuery, expiry);
            return response;
        }catch (Exception e){
            log.error("Exception ",e);
            return -1L;
        }
    }

    public static NotificationData getNotificationObject(String userId,String accTyp){
        NotificationData notificationObject = CacheInstance.getNotificationSettings(userId,accTyp);
        if(notificationObject==null){
            log.debug("No Notification Object in cache for "+userId+":"+accTyp);
            String hashkey = new StringBuilder(userId).append(CacheConstants.REDIS_KEY_SEPERATOR).append(accTyp).toString();
            String redisKey = new StringBuilder(MBConsumerConfig.getRedisSpecificPattern()).append(CacheConstants.REDIS_NOTIFICATION).toString();
            try{
                String response =  CacheService.getInstance().hashGet(redisKey,hashkey);
                if(response==null){
                    log.debug("No Notification Settings for userid  "+userId + " accTyp "+accTyp);
                    notificationObject = NotificationData.getDefaultObject();
                }else{
                    log.debug("Notification Setting in redis "+response);
                    Gson gson = new Gson();
                    notificationObject = gson.fromJson(response,NotificationData.class);
                }
                CacheInstance.setNotificationObject(userId,accTyp,notificationObject);
            }catch (Exception e){
                log.error("Exception ",e);
                notificationObject = NotificationData.getDefaultObject();
            }
        }
        return notificationObject;
    }

    public static Future<String> submitTask(ExecutorService executorService, BaseTask task){
        Future<String> objectFuture = null;
        try{
            objectFuture = executorService.submit(task);
        }catch (Exception e){
            log.error("Exception while submitting task to executor service ",e);
        }
        return objectFuture;
    }

    public static String runTask(Future<String> objectFuture, BaseTask task) throws Exception{
        String taskObject = null;
        if(objectFuture == null){
            taskObject = task.process();
        }else{
            taskObject = objectFuture.get();
        }
        return taskObject;
    }

    public static void loadEmtConnections(PropertiesConfiguration config,DBConnectionParam emtConnParam){
        emtConnParam.setDbIP(config.getString("config.postgresql.emt.ip"));
        emtConnParam.setDbUser(config.getString("config.postgresql.emt.user"));
        emtConnParam.setDbPassword(config.getString("config.postgresql.emt.password"));
        emtConnParam.setDbMaxIdle(config.getInt("config.postgresql.emt.maxIdle"));
        emtConnParam.setDbMinIdle(config.getInt("config.postgresql.emt.minIdle"));
        emtConnParam.setDbMaxTotal(config.getInt("config.postgresql.emt.maxTotal"));
        emtConnParam.setDbInitialSize(config.getInt("config.postgresql.emt.initialSize"));
        emtConnParam.setDbMinEvictableIdleTimeMillis(config.getLong("config.postgresql.emt.minEvictableIdleTimeMillis"));
        emtConnParam.setDbTimeBetweenEvictionRunsMillis(config.getLong("config.postgresql.emt.timeBetweenEvictionRunsMillis"));
        emtConnParam.setDbMaxWaitMills(config.getLong("config.postgresql.emt.maximumwait"));
        emtConnParam.setRemoveAbandonedTimeout(config.getInt("config.postgresql.emt.removeAbandonedTimeout"));
    }
}