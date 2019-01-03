package com.edel.messagebroker.modules;

import com.edel.messagebroker.objects.BaseType;
import com.edel.messagebroker.objects.Message;
import com.edel.messagebroker.objects.TaskAssign;
import com.edel.messagebroker.conductor.ConductorDataCache;
import com.edel.messagebroker.util.MBAppConstants;
import com.edel.messagebroker.util.MBHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.msf.log.Logger;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.Socket;

public class ConnectionClient {

    private static Logger log = Logger.getLogger(ConnectionClient.class);
    private  DataOutputStream out;
    private  Socket socket;
    private final Object writeLock = new Object();
    private BufferedReader socketReader = null;

    public ConnectionClient(Socket socket) throws Exception{
        log.info("New client socket creating : "+socket.getInetAddress() +" port "+socket.getPort());
        this.socket = socket;
        log.info("Socket done" );
        socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new DataOutputStream(socket.getOutputStream());
        log.info("out done" );
        log.info("New client created!");
    }

    public boolean handleThread(int task){
        boolean ifSent = sendInitiateMessage(task);
        if(ifSent){
            Thread thread = new Thread(() -> {
                try{
                    String message1 = "";
                    boolean completed = false;
                    while(!completed){
                        message1 = socketReader.readLine();
                        if(message1 ==null){
                            throw new Exception("Null from Socket");
                        }
                        Gson gson = new Gson();
                        BaseType baseType = gson.fromJson(message1, BaseType.class);
                        String type = baseType.getTyp();
                        switch (type){
                            case MBAppConstants.TaskTypes.DONE:
                                ConductorDataCache.getInstance().completeTask(task);
                                completed = true;
                            break;
                        }
                    }
                    String json = MBHelper.getCloseMessage();
                    sendMessage(json);
                    log.debug("Listening to client completed");
                }catch(Exception e){
                    log.debug("Exception in reading socket ",e);
                    ConductorDataCache.getInstance().removeWorker(task);
                }finally {
                    MBHelper.closeBufferReader(socketReader);
                    this.close();
                    if(ConductorDataCache.getInstance().isCompleted()){
                        log.debug("Work is Completed ,last task is "+task);
                        System.exit(-1);
                    }
                }
            });
            thread.setName("Client:"+task);
            thread.start();
        }
        return ifSent;
    }

    private boolean sendInitiateMessage(int task){
        TaskAssign taskAssign = new TaskAssign(task);
        Message<TaskAssign> message = new Message<>();
        message.setData(taskAssign);
        message.setExc(ConductorDataCache.getInstance().getExc());
        message.setTyp(MBAppConstants.TaskTypes.INITIATE);
        Type collectionType = new TypeToken<Message<TaskAssign>>(){}.getType();
        Gson gson = new Gson();
        String json = gson.toJson(message,collectionType);
        return sendMessage(json);
    }

    public void close(){
        try{
            synchronized (writeLock){
                out.close();
                socket.close();
            }
        }catch (Exception e){
            log.error("Exception while closing socket ",e);
        }
    }

    public boolean sendMessage(Object obj ){
        try{
            synchronized (writeLock){
                out.write((obj + "\n").getBytes());
            }
            return true;
        }catch (Exception e){
            log.error("Exception in sending message ",e);
            return false;
        }
    }
}