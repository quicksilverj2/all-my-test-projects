package com.edel.messagebroker.crons;

import com.edel.messagebroker.objects.BaseType;
import com.edel.messagebroker.objects.Message;
import com.edel.messagebroker.objects.TaskAssign;
import com.edel.messagebroker.util.MBAppConstants;
import com.edel.messagebroker.worker.WorkerConfig;
import com.edel.messagebroker.worker.WorkerDBConnection;
import com.edel.messagebroker.worker.WorkerDataCache;
import com.edel.notification.NotificationData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.msf.log.Logger;
import com.msf.utils.helper.Helper;
import javafx.concurrent.Task;

import javax.xml.transform.Result;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class WorkerEngine {

    private Socket socket = null;

    private static Logger log = Logger.getLogger(WorkerEngine.class);

    // Gets calls
    public Message<TaskAssign> startprocess(){
        //
        try{
            socket = new Socket(WorkerConfig.getInstance().getConIp(),WorkerConfig.getInstance().getConPort());
            log.debug("socket to string "+socket.toString());
            log.debug("Started client socket at "+socket.getLocalSocketAddress());
            BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String command =null;
            if((command=socketReader.readLine())!=null){
                Gson gson = new Gson();
                BaseType baseType = gson.fromJson(command,BaseType.class);
                if(baseType.getTyp().equals(MBAppConstants.TaskTypes.INITIATE)){
                    Type collectionType = new TypeToken<Message<TaskAssign>>(){}.getType();
                    Message<TaskAssign> taskAssignMessage = gson.fromJson(command,collectionType);
                    getUsers(taskAssignMessage);
//                    WorkerDataCache.getInstance().se
                } else if (baseType.getTyp().equalsIgnoreCase(MBAppConstants.TaskTypes.CLOSE)) {
                    log.debug("No Work to Do");
                }
                WorkerDataCache.getInstance().setUserDataFlag(true);
//                if(WorkerDataCache.getInstance().getSignalMap()){
//
//                }
            }else{
                log.debug("No Response from Server");
            }
        }catch (Exception e){
            //TODO Server Socket Not Present Error Handling
            log.error("Exception ",e);
        }
        return null;
    }

    private void closeSocket(){
        try{
            socket.close();
        }catch (Exception e){
            log.error("Exception in closing socket ",e);
        }
    }

    private void getUsers(Message<TaskAssign> taskAssignMessage){
        String query = new StringBuilder("Select * from tbl_user_notification_setting where ntile = ").append(taskAssignMessage.getData().getTskNo()).toString();
        log.debug("Executing Query "+query);
        long startTime = System.currentTimeMillis();
        try(Connection conn = WorkerDBConnection.getInstance().getEmtConnetion();
            PreparedStatement stmt = Helper.createPreparedStatementWithTimeOut(conn,query,100);
            ResultSet rs = stmt.executeQuery();
        ){
            log.debug("Query Time taken "+(System.currentTimeMillis() - startTime));
            if(rs==null || !rs.isBeforeFirst()){
                log.debug("No User Notification Data");
            }else{
                while (rs.next()){
                    //TODO accode change
                    String accode = rs.getString("eqaccode");
                    String accTyp = "EQ";
                    String userName = rs.getString("username");
                    String accname = rs.getString("accname");
                    NotificationData notificationData = new NotificationData();
                    notificationData.setCompltTrade(true);
                    notificationData.setPartialTrade(true);
                    notificationData.setAccName(accname);
                    notificationData.setAccode(accode);
                    notificationData.setActyp(accTyp);
                    notificationData.setUsrID(userName);
                    WorkerDataCache.getInstance().addUserNotiData(accode,accTyp,notificationData);
                }
            }
        }catch (Exception e){
            log.error("Exception ",e);
        }finally {

        }
    }
}
