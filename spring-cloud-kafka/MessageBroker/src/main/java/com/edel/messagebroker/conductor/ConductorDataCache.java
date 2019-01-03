package com.edel.messagebroker.conductor;

import com.edel.messagebroker.modules.ConnectionClient;
import com.edel.messagebroker.util.MBAppConstants;
import com.edel.messagebroker.util.MBHelper;
import com.edel.messagebroker.util.WorkerStatus;
import com.msf.log.Logger;
import com.msf.network.StandaloneCacheService;
import com.msf.utils.constants.CacheConstants;
import lombok.Getter;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ConductorDataCache {

    private static Logger log = Logger.getLogger(ConductorDataCache.class);

    private static volatile ConductorDataCache instance;

    private static final Object lock = new Object();

    private static final Object workerLock  = new Object();

    private HashMap<Integer, WorkerStatus> workersMap = new HashMap<>();

    private ArrayList<ConnectionClient> pendingConnList = new ArrayList<>();

    private ArrayList<Integer> pendingTaks = new ArrayList<>();

    private Set<Integer> completedTasks = new HashSet<>();

    private int maxCount;

    private boolean isCompleted = false;

    private ServerSocket serverSocket;

    @Getter
    private String exc;

    public static ConductorDataCache getInstance(){
        ConductorDataCache r = instance;
        if(r==null){
            synchronized (lock){
                r = instance;
                if(r==null){
                    instance = r = new ConductorDataCache();
                }
            }
        }
        return r;
    }

    public void handleNewWorker(ConnectionClient connectionClient){
        synchronized (workerLock){
            if(pendingTaks.size()>0){
                // Tasks are Available
                Integer taskCount = pendingTaks.get(0);
                //TODO Handle send the message to
                boolean isSent = connectionClient.handleThread(taskCount);
                if(isSent){
                    log.debug("Task "+taskCount +" accepted by client");
                    pendingTaks.remove(0);
                }
            }else{
                if(!isCompleted){
                    pendingConnList.add(connectionClient);
                }else{
                    log.debug("No Adding as is already completed");
                }
            }
        }
    }

    public void removeWorker(int task){
        synchronized (workerLock){
            log.debug("Adding task "+task +" back to pending");
            pendingTaks.add(task);
            workersMap.put(task,WorkerStatus.EXCEPTION);

            if(pendingConnList.size()>0){
                ConnectionClient client = pendingConnList.remove(0);
                int taskCount = pendingTaks.get(0);
                boolean isSent = client.handleThread(taskCount);
                if(isSent){
                    log.debug("Task "+taskCount +" accepted by client");
                    pendingTaks.remove(0);
                }else{
                    log.debug("Adding client back to pending conn list");
                    pendingConnList.add(client);
                }
            }
        }
    }

    public void completeTask(int tsk){
        synchronized (workerLock){
            workersMap.put(tsk,WorkerStatus.COMPLETED);
            completedTasks.add(tsk);

            if(completedTasks.size()==maxCount){
                this.isCompleted = true;
                log.debug("All Tasks Completed");

                if(pendingConnList.size()>0){
                    log.debug("Pending Conn List "+pendingConnList.size());
                    String json  = MBHelper.getCloseMessage();
                    for(ConnectionClient connectionClient : pendingConnList){
                        connectionClient.sendMessage(json);
                        connectionClient.close();
                    }
                    pendingConnList.clear();
                }else{
                    log.debug("No Pending Conn List to delete");
                }

                String key = ConductorConfig.getInstance().getRedisChannelBaseString() +CacheConstants.REDIS_CHANNEL_PENDING_ORDERS;
                String value = MBAppConstants.PendingTasks.COMPLETED;
                log.debug("key "+key+ " value "+value);

                try{
                    String reponse = StandaloneCacheService.getInstance().setValue(key,value);
                    log.debug("Redis set response "+reponse);
                }catch (Exception e){
                    log.error("Exception in setting ",e);
                }
                MBHelper.setExpiryTillEOD(MBAppConstants.PendingTasks.EXPIRY_TIME,key);

                StandaloneCacheService.getInstance().closeConnection();
                ConductorDBConnection.getInstance().closeConnections();
                try{
                    serverSocket.close();
                }catch (Exception e){
                    log.error("Exception in closing server socket ",e);
                }
            }
        }
    }

    public void executeDBCommand(int maxClient){
        String query = new StringBuilder(" select * from public.ufn_insert_push_notifications_data(").append(maxClient).append(");").toString();

        long startTime = System.currentTimeMillis();

        try(Connection conn = ConductorDBConnection.getInstance().getEmtConnetion();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
        ){
            log.debug("time Taken "+(System.currentTimeMillis() - startTime)+" Query "+query);

        }catch (Exception e){
            log.error("Exception in loading notification data into table ",e);
        }
    }

    public void initialize(int maxCount, int port, int queueLength, String ipAddress, String exc){
        synchronized (workerLock){
            for(int i=1;i<=maxCount;i++){
                pendingTaks.add(i);
                workersMap.put(i,WorkerStatus.DIDNOTSTART);
            }
            this.maxCount = maxCount;
        }
        this.exc = exc;
        executeDBCommand(maxCount);

        try{
            serverSocket = new ServerSocket(port,queueLength, InetAddress.getByName(ipAddress));
        }catch (Exception e){
            log.error("Exception ",e);
            System.exit(-1);
        }

        Thread thread = new Thread(() -> {
            while(true){
                try{
                    log.debug("Waiting for Thread");
                    Socket newSocket = serverSocket.accept();
                    log.info(newSocket.getInetAddress()+" has connected!");
                    log.info("HostAddress : "+newSocket.getInetAddress().getHostAddress());
                    ConnectionClient connectionClient = new ConnectionClient(newSocket);
                    ConductorDataCache.getInstance().handleNewWorker(connectionClient);
                } catch(Exception e){
                    log.error("IO Error while staring socket server for orders!",e);
                }
            }
        });
        thread.setDaemon(true);
        thread.setName("ServerSocketThread:");
        thread.start();

        // Set in Redis and publish
        String key = ConductorConfig.getInstance().getRedisChannelBaseString() +CacheConstants.REDIS_CHANNEL_PENDING_ORDERS;
        String value = MBAppConstants.PendingTasks.STARTED;
        log.debug("key "+key+ " value "+value);

        try{
            String reponse = StandaloneCacheService.getInstance().setValue(key,value);
            log.debug("Redis set response "+reponse);
        }catch (Exception e){
            log.error("Exception in setting ",e);
        }

        try{
            Long pubRes = StandaloneCacheService.getInstance().publish(key,value);
            log.debug("Pub response "+pubRes);
        }catch (Exception e){
            log.error("Exception in publishing",e);
        }
        MBHelper.setExpiryTillEOD(MBAppConstants.PendingTasks.EXPIRY_TIME,key);

    }

    public boolean isCompleted(){
        boolean isCom = false;
        synchronized (workerLock){
            isCom = isCompleted;
        }
        return isCom;
    }

    private ConductorDataCache(){
        log.debug("conductor Data Cache Initialized");
    }

}
