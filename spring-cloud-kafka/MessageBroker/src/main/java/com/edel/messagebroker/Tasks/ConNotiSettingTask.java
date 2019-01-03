package com.edel.messagebroker.Tasks;

import com.edel.messagebroker.util.MBAppConstants;
import com.edel.messagebroker.util.MBHelper;
import com.msf.log.Logger;
import com.msf.utils.constants.CacheConstants;

public class ConNotiSettingTask extends BaseTask {

    private static Logger log = Logger.getLogger(ConNotiSettingTask.class);

    private String userId;

    public ConNotiSettingTask(String userId){
        this.userId = userId;
    }

    public String process(){
        log.debug("Process of connot ");
        String accType = MBHelper.getAccType(userId);
        if(accType==null||accType.equalsIgnoreCase(MBAppConstants.NO_PRESENT)||accType.equalsIgnoreCase(MBAppConstants.EXCEPTION)){
            return "";
        }else if(accType.equalsIgnoreCase(CacheConstants.ACCOUNT_BOTH)){
            MBHelper.getNotificationObject(userId,CacheConstants.ACCOUNT_EQUITY);
            MBHelper.getNotificationObject(userId,CacheConstants.ACCOUNT_COMM);
        }else{
            MBHelper.getNotificationObject(userId,accType);
        }
        return "";
    }

    @Override
    public String call(){
        try{
            super.startProcess();
            return process();
        }catch (Exception e){
            log.error("Exception ",e);
            return "";
        }
    }
}
