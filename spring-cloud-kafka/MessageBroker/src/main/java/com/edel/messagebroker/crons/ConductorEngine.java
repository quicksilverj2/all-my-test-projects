package com.edel.messagebroker.crons;

import com.edel.messagebroker.modules.ConnectionClient;
import com.edel.messagebroker.conductor.ConductorDBConnection;
import com.edel.messagebroker.conductor.ConductorDataCache;
import com.msf.log.Logger;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ConductorEngine {

    private static Logger log = Logger.getLogger(ConductorEngine.class);

    private ServerSocket serverSocket;

    public ConductorEngine(String ipAddress,int port,int queueLength){
        try{
            log.debug("conductor Engine");
            serverSocket = new ServerSocket(port,queueLength, InetAddress.getByName(ipAddress));
        }catch (Exception e){
            log.error("Exception in creating Server Socket ",e);
            System.exit(-1);
        }
    }

    public void startProcess(int maxClient){

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

        Thread thread = new Thread(() -> {
            while(true){
                try{
                    Socket newSocket = serverSocket.accept();
                    log.info(newSocket.getInetAddress()+" has connected!");
                    log.info("HostAddress : "+newSocket.getInetAddress().getHostAddress());
                    ConnectionClient connectionClient = new ConnectionClient(newSocket);
                    ConductorDataCache.getInstance().handleNewWorker(connectionClient);
                } catch(Exception e){
                    log.error("IO Error while staring socket server for orders!",e);
                    if(ConductorDataCache.getInstance().isCompleted()){
                        log.debug("Job Completed");
                        System.exit(-1);
                    }

                }
            }
        });
        thread.setName("ServerSocketThread:");
        thread.start();
    }

    public void closeSocket(){
        try{
            serverSocket.close();
        }catch (Exception e){
            log.error("Exception in closing server socket ",e);
        }
    }
}