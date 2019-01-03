package com.edel.stream.quoteprocessor.component;

import com.edel.rulelib.java8.execution.ExecutionEvent;
import com.edel.rulelib.java8.execution.RulesProcessor;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.edel.stream.quoteprocessor.binding.QuoteProcessorBinding.EVENT_QUOTE_STREAM_OUT;
import static com.edel.stream.quoteprocessor.binding.QuoteProcessorBinding.QUOTE_STREAM_IN;

/**
 * Created by jitheshrajan on 10/22/18.
 */
@Component
public class QuoteCategorizedRuleProcessor {

    private String category = "SCREENER";
    // todo the above field should be configured via a config file or any other source!

    private static final Logger log =
            LoggerFactory.getLogger(QuoteCategorizedRuleProcessor.class);
    JsonParser parser = new JsonParser();

    @SendTo(EVENT_QUOTE_STREAM_OUT)
    @StreamListener
    public KStream<String, String> process(
            @Input(QUOTE_STREAM_IN) KStream<String, String> eventKstream) {

        log.info("processor method instantiated!");

        KStream<String, String> preFilteredStream =
                eventKstream
//                        .filter(new Predicate<String, String>() {
//                            @Override
//                            public boolean test(String s, String s2) {
//                                log.info("Input s: "  +s);
//                                log.info("Input s2: "+s2);
//                                return !s2.isEmpty();
//                            }
//                        })
                        .filter((key, value) -> !value.isEmpty())
                        .map((key, value) -> {
                            ExecutionEventWrapper executionEventWrapper = processAndGetExecutionEvents(key, value, category);
                            return new KeyValue<>(executionEventWrapper.getKey(),executionEventWrapper.getExecEventListString());
                        });

//                .filter((s, s2) -> !s2.isEmpty());

//        preFilteredStream.groupByKey()
//                .aggregate(() -> 0, // initialzr : this is set to 0 since we are starting fresh everyday!
//                                     // so if you are restarting this in between, then we need to fetch
//                                     // this value from a state store.
//                (aggKey, newValue, aggValue) ->  )

        return preFilteredStream
                .filter((s, s2) -> !s2.isEmpty());
    }


    private ExecutionEventWrapper processAndGetExecutionEvents(String key, String quotePacketString, String category) {

        log.info(key);

        JsonObject jsonObject = parser.parse(quotePacketString).getAsJsonObject();
        String symbol = jsonObject.get("sym") != null  ? jsonObject.get("sym").getAsString() : null;



        List<ExecutionEvent> executionEvents = RulesProcessor.processMatches(jsonObject, category);

        log.info(executionEvents.size()+"");

        return new ExecutionEventWrapper(symbol, executionEvents != null && !executionEvents.isEmpty() ? executionEvents.toString() : "");
    }


    class ExecutionEventWrapper {
        @Getter
        @Setter
        private String key;
        @Getter
        @Setter
        private String execEventListString;

        public ExecutionEventWrapper(String key, String execEventListString) {
            this.key = key;
            this.execEventListString = execEventListString;
        }
    }

}





