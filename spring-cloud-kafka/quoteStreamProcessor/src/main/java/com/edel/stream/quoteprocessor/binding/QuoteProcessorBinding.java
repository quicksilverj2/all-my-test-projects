package com.edel.stream.quoteprocessor.binding;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

/**
 * Created by jitheshrajan on 10/22/18.
 */
public interface QuoteProcessorBinding {

    String QUOTE_STREAM_IN = "streamtoqueue-quote-in";
    String EVENT_QUOTE_STREAM_OUT = "eventquote-quote-out";

    @Input(QUOTE_STREAM_IN)
    KStream<String, String> quotePacketStreamIn();

    @Output(EVENT_QUOTE_STREAM_OUT)
    KStream<String, String> processedEventsWithSymbol();
}
