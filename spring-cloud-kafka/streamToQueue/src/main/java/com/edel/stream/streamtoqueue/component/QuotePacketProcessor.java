package com.edel.stream.streamtoqueue.component;

import com.edel.stream.streamtoqueue.binding.QuoteStreamBinding;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import static com.edel.stream.streamtoqueue.binding.QuoteStreamBinding.QUOTE_STREAM_IN;

/**
 * Created by jitheshrajan on 10/22/18.
 */
@Component
public class QuotePacketProcessor {

    private static final Logger log =
            LoggerFactory.getLogger(QuotePacketProcessor.class);


    @StreamListener
    public void process(
            @Input(QUOTE_STREAM_IN) KStream<String, String> eventKstream) {


        eventKstream
//                .filter(new Predicate<String, String>() {
//                    @Override
//                    public boolean test(String s, String s2) {
//                        log.info("Input s: "  +s);
//                        log.info("Input s2: "+s2);
//                        return !s2.isEmpty();
//                    }
//                })
                .filter((key, value) -> !value.isEmpty())
                .map((key, value) -> new KeyValue<>(parseMessageIntoJsonAndRetrieveKey(value),"0"))
                .groupByKey()
                .windowedBy(TimeWindows.of(1000 * 60))
                .count(Materialized.as(QuoteStreamBinding.QUOTE_PACKET_WINDOWED_MV));


        eventKstream
                .filter((key, value) -> !value.isEmpty())
                .map((key, value) -> new KeyValue<>(parseMessageIntoJsonAndRetrieveKey(value),"0"))
                .groupByKey()
                .count(Materialized.as(QuoteStreamBinding.QUOTE_PACKET_MV));
//                .toStream();


    }

    private String parseMessageIntoJsonAndRetrieveKey(String value) {
        JsonParser parser = new JsonParser();
        JsonObject o = parser.parse(value).getAsJsonObject();
        log.info("SYMBOL : "+o.get("sym"));
        return o.get("sym").getAsString();
    }
}
