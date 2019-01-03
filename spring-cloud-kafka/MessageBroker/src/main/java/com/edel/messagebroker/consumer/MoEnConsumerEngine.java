package com.edel.messagebroker.consumer;

import com.edel.services.constants.AppConstants;
import com.msf.log.Logger;
import com.msf.utils.helper.Helper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

public class MoEnConsumerEngine extends BaseConsumerEngine {

    private KafkaConsumer<String, String> consumer;

    private static Logger log = Logger.getLogger(MoEnConsumerEngine.class);

    public MoEnConsumerEngine(){
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, MoEnConsumerConfig.getBrokers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG,MoEnConsumerConfig.getGroupID());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"false");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,"30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
//                "com.edel.messagebroker.util.OrderUpdateDeserializer");
        consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList(MoEnConsumerConfig.getTopic()));
    }


    @Override
    public void run(){
        try{
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap.put(AppConstants.HTTPHeadersKey.CONTENT_TYPE, AppConstants.HTTPHeadersKey.CONTENT_TYPE_JSON);

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(10000);
                if(records.isEmpty()){
                    log.debug("No Records");
                }
                for (ConsumerRecord<String, String> record : records) {
                    try{
                        String message = record.value();
                        String responseString = Helper.getHTTPClientResponseWithOutAuditObjectNoRes("http://10.250.26.14/notify-dev/push/send/moengage",
                                message, headerMap,
                                25000,
                                25000,
                                AppConstants.HTTPHeadersKey.METHOD_TYPE_POST);
                        log.debug("Response String "+responseString);
                        consumer.commitSync();
                    }catch (Exception e){
                        log.error("Exception ",e);
                    }
                }
            }
        }catch (Exception e){
            log.error("UnCaught Exception ",e);
        }finally {
            consumer.close();
        }
    }
}
