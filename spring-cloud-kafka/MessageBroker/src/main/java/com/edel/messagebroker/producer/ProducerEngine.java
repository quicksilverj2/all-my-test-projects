package com.edel.messagebroker.producer;

import com.edel.messagebroker.util.CustomProducerInterceptor;
import com.edel.messagebroker.util.MBProducerConfig;
import com.msf.log.Logger;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class ProducerEngine  {

    private static Logger log = Logger.getLogger(ProducerEngine.class);

    private static volatile ProducerEngine instance;

    private static final Object lock = new Object();

    private Properties props;

    private Producer<String,String> producer;

    public static ProducerEngine getInstance(){
        ProducerEngine r = instance;
        if(r==null){
            synchronized (lock){
                r = instance;
                if(r==null){
                    instance = r = new ProducerEngine();
                }
            }
        }
        return r;
    }

    private ProducerEngine(){
        log.debug("Producer Engine Initialized ");
        props = new Properties();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, MBProducerConfig.getBrokers());

        props.put(ProducerConfig.ACKS_CONFIG,"1");

        props.put(ProducerConfig.RETRIES_CONFIG,0);

        //Specify buffer size in config
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);

        //Reduce the no of requests less than 0
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);

        //The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");

        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, CustomProducerInterceptor.class.getName());

        producer = new KafkaProducer<>(props);
    }

    public static void publish(String message){
        ProducerRecord<String,String> record = new ProducerRecord<>(MBProducerConfig.getTopic(),message);
        long time = System.currentTimeMillis();
        getInstance().producer.send(record, (metadata, exception) -> {
            long elapsedTime = System.currentTimeMillis() - time;
            if (metadata != null) {
                log.debug("Received Record (key = "+record.key()+" value = "+record.value()+") meta (partition = "+metadata.partition()+", offset= "+metadata.offset()+") time= "+elapsedTime+" curr Time "+System.currentTimeMillis());
            } else {
                exception.printStackTrace();
            }
        });
    }

    public static void close(){
        try{
            if(getInstance().producer!=null){
                getInstance().producer.close();
            }
        }catch (Exception e){
            log.error("Exception ",e);
        }
    }
}
