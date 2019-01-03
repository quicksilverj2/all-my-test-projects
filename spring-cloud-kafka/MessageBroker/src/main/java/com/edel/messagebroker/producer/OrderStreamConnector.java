package com.edel.messagebroker.producer;

import com.edel.messagebroker.util.MBHelper;
import com.edel.messagebroker.util.MBProducerConfig;
import com.msf.log.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

//TODO implement reconnecting of socket

public class OrderStreamConnector {

    private static Logger log = Logger.getLogger(OrderStreamConnector.class);

    public void startListening(){

        Socket socket = null;
        BufferedReader socketReader = null;
        try{
            ProducerEngine.getInstance();
            socket = new Socket(MBProducerConfig.getOrderStreamerIp(),
                    MBProducerConfig.getOrderStreamerPort());

            log.debug("socket to string "+socket.toString());
            log.debug("Started client socket at "+socket.getLocalSocketAddress());
            log.debug("");

            socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String s = "";

            while ((s= socketReader.readLine())!=null) {
                log.debug("message "+s);
                ProducerEngine.publish(s);
            }
            log.debug("Got null from socket ");
        }catch (Exception e){
            log.error("Exception while reading data from order streamer "+e);
        }finally {
            MBHelper.closeBufferReader(socketReader);
            MBHelper.closeSocket(socket);
            ProducerEngine.close();
        }
    }
}
