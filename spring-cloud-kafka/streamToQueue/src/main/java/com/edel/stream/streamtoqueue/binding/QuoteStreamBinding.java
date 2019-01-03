package com.edel.stream.streamtoqueue.binding;


import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface QuoteStreamBinding {
    String QUOTE_STREAM_IN = "streamtoqueue-quote-in";
    String QUOTE_STREAM_OUT = "streamtoqueue-quote-out";

    String QUOTE_PACKET_MV = "streamtoqueue-quote-mv";
    String QUOTE_PACKET_WINDOWED_MV = "streamtoqueue-quote-windowed-mv";


    @Input(QUOTE_STREAM_IN)
    KStream<String, String> quotePacketStreamIn();

    @Output(QUOTE_STREAM_OUT)
    MessageChannel quotePacketStreamOut();

}
