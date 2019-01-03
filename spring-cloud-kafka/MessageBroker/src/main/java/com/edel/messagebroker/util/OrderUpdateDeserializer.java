package com.edel.messagebroker.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class OrderUpdateDeserializer implements Deserializer {
    @Override
    public void configure(Map map, boolean b) {

    }

    @Override
    public Object deserialize(String s, byte[] bytes) {
        ObjectMapper mapper = new ObjectMapper();
        SampleObject sampleObject = null;
        try{
            String roundTrip = new String(bytes, "UTF8");
            System.out.println(roundTrip+" toundt");
            sampleObject = mapper.readValue(bytes,SampleObject.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return sampleObject;
    }

    @Override
    public void close() {

    }
}
