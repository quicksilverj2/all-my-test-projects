package com.edel.messagebroker.test;

import com.edel.messagebroker.util.MBProducerConfig;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Test {

    public static void main(String[] args){
        try{
//            Socket socket = new Socket("10.250.26.126",9307);
//            System.out.println("Started client socket at "+socket.getLocalSocketAddress());
//            System.out.println("Inet Address "+socket.getInetAddress());
//            System.out.println("Port "+socket.getPort());
//            System.out.println("Local Port "+socket.getLocalPort());
//            System.out.println("REmote Socket "+socket.getRemoteSocketAddress());
            ArrayList<Integer> list = new ArrayList<>();
            list.add(1);
            Integer intege = list.remove(0);
            System.out.println(intege);

            Socket socket = new Socket("192.168.123.21",50123);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            int i=0;
            while(true){
//                out.write(("i "+i + "\n").getBytes());
//                System.out.println("i "+i);
                Thread.sleep(2000);
//                i++;
            }

        }catch (Exception e){
            System.out.println("Exception "+e.getMessage());
        }
    }
}
