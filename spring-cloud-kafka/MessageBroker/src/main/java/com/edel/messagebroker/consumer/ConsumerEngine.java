package com.edel.messagebroker.consumer;

import com.edel.messagebroker.modules.DefaultAction;
import com.edel.messagebroker.modules.TargetUserAttributes;
import com.edel.messagebroker.util.*;
import com.edel.notification.NotificationData;
import com.edel.services.constants.AppConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.msf.log.Logger;
import com.msf.network.QuoteSentinelCache;
import com.msf.utils.constants.CacheConstants;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.lang.reflect.Type;
import java.util.*;


public class ConsumerEngine extends BaseConsumerEngine{

    private KafkaConsumer<String, String> consumer;

    private Producer<String,String> producer;

    private static Logger log = Logger.getLogger(ConsumerEngine.class);

    public ConsumerEngine(){
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, MBConsumerConfig.getBrokers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG,MBConsumerConfig.getGroupID());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"false");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,"30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
//                "com.edel.messagebroker.util.OrderUpdateDeserializer");

        log.info("Consumer thread initializing");

        consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList(MBConsumerConfig.getTopic()));

        Properties producerPros = new Properties();
        producerPros.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, MBConsumerConfig.getMonegageBrokers());

        producerPros.put(ProducerConfig.ACKS_CONFIG,"1");

        producerPros.put(ProducerConfig.RETRIES_CONFIG,0);

        //Specify buffer size in config
        producerPros.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);

        //Reduce the no of requests less than 0
        producerPros.put(ProducerConfig.LINGER_MS_CONFIG, 1);

        //The buffer.memory controls the total amount of memory available to the producer for buffering.
        producerPros.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);

        producerPros.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");

        producerPros.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        producerPros.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, CustomProducerInterceptor.class.getName());

        producer = new KafkaProducer<>(producerPros);

    }

    @Override
    public void run(){
        try{
            Gson gson = new Gson();
            boolean shouldStop = false;
            while (!shouldStop){
                try{
                    ConsumerRecords<String, String> records = consumer.poll(10000);
                    if(records.isEmpty()){
                        log.debug("No Records");
                    }
                    for (ConsumerRecord<String, String> record : records) {
                        // print the offset,key and value for the consumer records.
//                System.out.print(" received  "+System.currentTimeMillis());
                        log.debug("Thread Name "+Thread.currentThread().getName()+"offset = "+record.offset()+", key = "+ record.key()+", value = "+ record.value());
                        String value = record.value();
                        GeneralPacket generalPacket = gson.fromJson(value,GeneralPacket.class);
                        if(generalPacket.getType().equalsIgnoreCase(AppConstants.OrderFilerConstants.TRADE_UPDATE_TYPE)){
                            log.debug("Trade Update");

                            TradeDataPacket tradeDataPacket = gson.fromJson(value,TradeDataPacket.class);
                            //TODO confirm the logic with
                            if(tradeDataPacket.getData().getFQty()==null || tradeDataPacket.getData().getFQty().isEmpty()){
                                log.debug("Position Conversion so not doing anything ");
                                continue;
                            }
                            NotificationData notificationObject = MBHelper.shouldSendNotification(tradeDataPacket);
//                        MBHelper.handleNotification(notificationObject);
                            if(notificationObject!=null){
                                String notiMessage = MBHelper.getTradeMessage(tradeDataPacket,notificationObject);
                                TargetUserAttributes targetUserAttributes = MBHelper.getUserAttributes(tradeDataPacket.getAccId());
                                NewMoengageRequest androidRequest = new NewMoengageRequest();
                                androidRequest.setPlatform(MBAppConstants.PLATFORM_NAME_ANDROID);
                                androidRequest.setTargetAudience(MBAppConstants.TARGET_AUDIENCE_USER);
                                androidRequest.setCustomSegmentName(null);
                                DefaultAction defaultAction = new DefaultAction();
                                defaultAction.setType("navigation");
                                defaultAction.setValue("com.msf.emt.mobile.app.MainActivity");
                                HashMap<String,String> kvPairs = new HashMap<>();
                                kvPairs.put("FROM_PUSHNOTIFICATION","Yes");
                                kvPairs.put("SCREEN_ID","TRADE_BOOK");
                                defaultAction.setKvPairs(kvPairs);
                                androidRequest.setDefaultAction(defaultAction);
                                androidRequest.setTargetUserAttributes(targetUserAttributes);
                                androidRequest.setMessage(notiMessage);
                                androidRequest.setTitle(MBConsumerConfig.getTrdConfTitle());
                                androidRequest.setSubtitle(null);
                                androidRequest.setSummary(null);

                                NewMoengageRequest iosRequest = new NewMoengageRequest();
                                iosRequest.setPlatform(MBAppConstants.PLATFORM_NAME_IOS);
                                iosRequest.setTargetAudience(MBAppConstants.TARGET_AUDIENCE_USER);
                                iosRequest.setCustomSegmentName(null);
                                DefaultAction defaultAction1 = new DefaultAction();
                                defaultAction1.setKvPairs(kvPairs);
                                defaultAction1.setType(null);
                                defaultAction1.setValue(null);
                                iosRequest.setDefaultAction(defaultAction1);
                                iosRequest.setTitle(MBConsumerConfig.getTrdConfTitle());
                                iosRequest.setSubtitle(null);
                                iosRequest.setSummary(null);
                                iosRequest.setTargetUserAttributes(targetUserAttributes);

                                iosRequest.setMessage(notiMessage);
                                iosRequest.setCampaignName("");

                                ArrayList<NewMoengageRequest> requests = new ArrayList<>();
                                requests.add(androidRequest);
                                requests.add(iosRequest);

                                Type listType = new TypeToken<List<NewMoengageRequest>>() {}.getType();
                                String message = gson.toJson(requests,listType);
                                log.debug("Moengage request "+message);


                                ProducerRecord<String,String> producerRecord = new ProducerRecord<>(MBConsumerConfig.getMoengageTopic(),message);
                                long time = System.currentTimeMillis();
                                producer.send(producerRecord, (metadata, exception) -> {
                                    long elapsedTime = System.currentTimeMillis() - time;
                                    if (metadata != null) {
                                        log.debug("Received Record (key = "+record.key()+" value = "+record.value()+") meta (partition = "+metadata.partition()+", offset= "+metadata.offset()+") time= "+elapsedTime+" curr Time "+System.currentTimeMillis());
                                    } else {
                                        exception.printStackTrace();
                                        log.error("Exception ",exception);
                                    }
                                });
                                consumer.commitSync();
                            }
                        }else{
                            OrderDataPacket orderDataPacket = gson.fromJson(value,OrderDataPacket.class);
                            if(orderDataPacket.getData().getSts().equalsIgnoreCase(MBAppConstants.OrderStatus.OPEN)
                                    ||orderDataPacket.getData().getSts().equalsIgnoreCase(MBAppConstants.OrderStatus.TRIGGER_PENDING)){
                                String ordNo = orderDataPacket.getData().getOID();
                                String excField = MBHelper.getRedisField(orderDataPacket.getData().getSym());
                                long tradeEndTime ;
                                // Push Into Redis
                                if(excField!=null){
                                    long time = Long.parseLong(orderDataPacket.getData().getExtOrdTim());
                                    tradeEndTime = MBHelper.getTradeClosingTime(orderDataPacket.getData().getSym());
                                    log.debug("time  "+time +" tradeEndTime "+tradeEndTime);
                                    if(time<tradeEndTime){
                                        try{

                                            String rediskey = new StringBuilder(MBConsumerConfig.getRedisSpecificPattern())
                                                    .append(excField).append(orderDataPacket.getAccId())
                                                    .append(CacheConstants.REDIS_KEY_SEPERATOR)
                                                    .append(orderDataPacket.getAccTyp()).toString();
                                            Long hsetRespnse = QuoteSentinelCache.getInstance().hset(rediskey,ordNo,value);
                                            log.debug("hset Response "+hsetRespnse + " key "+rediskey + " ordNo "+ordNo);
                                        }catch (Exception e){
                                            log.error("Exception ",e);
                                        }
                                    }else{
                                        log.debug("order time crossed for NSE");
                                    }
                                }else{
                                    log.debug("Exc Fields  nUll");
                                }
                            }else if(orderDataPacket.getData().getSts().equalsIgnoreCase(MBAppConstants.OrderStatus.CANCELLED)
                                    || orderDataPacket.getData().getSts().equalsIgnoreCase(MBAppConstants.OrderStatus.REJECTED)
                                    || orderDataPacket.getData().getSts().equalsIgnoreCase(MBAppConstants.OrderStatus.REJECTED)){
                                // delete data for the user
                                String ordNo = orderDataPacket.getData().getOID();
                                String excField = MBHelper.getRedisField(orderDataPacket.getData().getSym());
                                if(excField!=null){
                                    try{
                                        String rediskey = new StringBuilder(MBConsumerConfig.getRedisSpecificPattern())
                                                .append(excField).append(orderDataPacket.getAccId())
                                                .append(CacheConstants.REDIS_KEY_SEPERATOR)
                                                .append(orderDataPacket.getAccTyp()).toString();
                                        Long hdelResponse = QuoteSentinelCache.getInstance().hdel(rediskey,ordNo);
                                        log.debug("hdel Response "+hdelResponse+" redis Key "+rediskey +" hashKey "+ordNo);
                                    }catch (Exception e){
                                        log.error("Exception ",e);
                                    }
                                }else{
                                    log.debug("Exc Fields  nUll");
                                }
                            }else{
                                log.debug("ignoring other status");
                            }
                            log.debug("Order update");
                        }
                    }
                    if(CacheInstance.shouldStop()){
                        shouldStop = true;
                        log.debug("Recevied stop command ");
                    }
                }catch (Exception e){
                    log.error("Exception inside while loop ",e);
                }
            }
        }catch (Exception e){
            log.error("Exception ",e);
        }finally {
            try{
                if(producer!=null){
                    producer.close();
                }

                if(consumer!=null){
                    consumer.close();
                }
            }catch (Exception e){
                log.error("Exception ",e);
            }
        }
    }
}
