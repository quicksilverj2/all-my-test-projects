package com.edel.stream.streamtoqueue.source;

import com.edel.streamparse.model.MarketDepthQuotePacket;
import com.edel.streamparse.model.StreamQuotePacket;
import com.edel.streamparse.store.StreamerClient;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import java.util.Random;

public class QuoteStreamClient implements StreamerClient {

    private final MessageChannel quoteStreamOut;
    private final int partitionCount;
    private Gson gson;
    private static final Logger log =
            LoggerFactory.getLogger(QuoteStreamClient.class);

    private final Random RANDOM_NUMBER_GENERATOR = new Random(System.currentTimeMillis());


    public QuoteStreamClient(MessageChannel quoteStreamOut, int partitionCount) {
        this.quoteStreamOut = quoteStreamOut;
        this.partitionCount = partitionCount;
        gson = new Gson();
    }

    @Override
    public void receiveQuotePacket(StreamQuotePacket quotePacket) {
        log.info("Received Quote Stream Packet!");

        Message<String> orderUpdateEventMessage = MessageBuilder
                .withPayload(gson.toJson(quotePacket))
                .setHeader("partitionKey", RANDOM_NUMBER_GENERATOR.nextInt(partitionCount))
                .setHeader("sym",quotePacket.getSym())
                .build();

        this.quoteStreamOut.send(orderUpdateEventMessage);
    }

    @Override
    public void receiveMarketDepthPacket(MarketDepthQuotePacket marketDepthQuotePacket) {

        //TODO
        log.info("have to check this!");
    }
}
