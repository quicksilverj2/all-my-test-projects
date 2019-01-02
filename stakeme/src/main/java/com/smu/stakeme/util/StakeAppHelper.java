package com.smu.stakeme.util;

import com.smu.stakeme.model.StakePlayer;
import com.smu.stakeme.store.UserStore;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by jitheshrajan on 11/29/18.
 */
public class StakeAppHelper {

    public static String base64Encoding(byte[] data) {
        byte[] encodedBytes = Base64.encodeBase64(data);
        return new String(encodedBytes);
    }

    public static void addStakeUserFromFileLine(String line){

        // default delimiter is ',' so it cant be a part of any values of the member!

        String splitStrings[] = line.split(",");

        if(splitStrings.length < 6){
            System.err.println("Un supported line found! ignoring : "+line);
            return;
        }

        try {
            UserStore.getInstance().addUser(new StakePlayer(
                    generateUniqueId(),
                    splitStrings[0],
                    splitStrings[1],
                    splitStrings[2],
                    splitStrings[3],
                    splitStrings[4],
                    splitStrings[5]
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static List<String> readEntireFile(String fileName){
        try {
            File file = new File(fileName);
            String line = null;

            FileInputStream fin = null;
            try {
                fin = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                System.err.println("File not found!");
                return null;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(fin));

            List<String> fileStrings = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                fileStrings.add(line);
            }

            return fileStrings;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String generateUniqueId() {
        return UUID.randomUUID() + "";
    }

    public static void addCountryDetailsFromLine(String line) {
        String splitStrings[] = line.split(",");

        if(splitStrings.length < 5){
            System.err.println("Un supported line found! ignoring : "+line);
            return;
        }

        try {
            CountryDetailEnum.INSTANCE.registerCountryDetail(
                    CountryDetail.builder().countryNameShort(splitStrings[0])
                            .countryName(splitStrings[4]).countryCode(splitStrings[3])
                    .build());

            System.out.println("Inserting : "+splitStrings[4]) ;


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
