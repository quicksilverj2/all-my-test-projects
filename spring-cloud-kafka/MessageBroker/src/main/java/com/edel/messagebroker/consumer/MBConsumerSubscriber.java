package com.edel.messagebroker.consumer;

import com.edel.messagebroker.util.CacheInstance;
import com.edel.messagebroker.util.MBHelper;
import com.edel.notification.NotificationData;
import com.edel.tradeconfirmation.objects.UserIdentifier;
import com.google.gson.Gson;
import com.msf.log.Logger;
import com.msf.network.BaseSubscriber;
import com.msf.utils.constants.CacheConstants;

public class MBConsumerSubscriber extends BaseSubscriber {

    private static com.msf.log.Logger log = Logger.getLogger(MBConsumerSubscriber.class);

    private String baseChannel ;

    public MBConsumerSubscriber(String baseChannel){
        this.baseChannel = baseChannel;
    }

    @Override
    public void onMessage(String channel,String message){
        try{
            super.onMessage(channel,message);
            Gson gson  = new Gson();
            String actChannel = channel.replace(baseChannel,"");
            switch (actChannel){
                case CacheConstants.REDIS_CHANNEL_GET_NOTIFICATION:
                    UserIdentifier identifier = gson.fromJson(message,UserIdentifier.class);
                    MBHelper.handleLoginRequest(identifier);
                    break;
                case CacheConstants.REDIS_CHANNEL_SET_NOTIFICATION:
                    NotificationData notificationObject = gson.fromJson(message,NotificationData.class);
                    MBHelper.handleNotification(notificationObject);
                    break;
                case CacheConstants.REDIS_MB_CONSUMER_PROCESS:
                    if(message.equalsIgnoreCase(CacheConstants.REDID_CHANNEL_MSG_PROCESS_STOP)){
                        CacheInstance.setStopProcess(true);
                    }
                    break;
                default:
                    break;
            }
        }catch (Exception e){
            log.error("Exception ",e);
        }
    }
}
