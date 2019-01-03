package com.edel.messagebroker.util;

import com.msf.log.Logger;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

public class CustomProducerInterceptor implements ProducerInterceptor {

    private static Logger log = Logger.getLogger(CustomProducerInterceptor.class);

    @Override
    public ProducerRecord onSend(ProducerRecord record) {
        log.debug(String.format("onSend topic=%s key=%s value=%s %d \n",
                record.topic(), record.key(), record.value().toString(),
                record.partition()
        ));        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception e) {
        log.debug(String.format("onAck topic=%s, part=%d, offset=%d\n",
                metadata.topic(), metadata.partition(), metadata.offset()
        ));
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
