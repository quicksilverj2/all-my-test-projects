package com.edel.stream.streamtoqueue.component;

import com.edel.stream.streamtoqueue.binding.QuoteStreamBinding;
import com.edel.stream.streamtoqueue.source.QuoteStreamClient;
import com.edel.streamparse.store.TCPInstanceRegister;
import com.edel.streamparse.store.TCPStreamConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;

import static com.edel.stream.streamtoqueue.binding.QuoteStreamBinding.QUOTE_STREAM_OUT;

/**
 * Created by jitheshrajan on 9/28/18.
 */
@Component
public class ProcessorStartSetupTaskRunner implements ApplicationRunner {
    private static final Logger log =
            LoggerFactory.getLogger(ProcessorStartSetupTaskRunner.class);

    public static int counter;

    private QuoteStreamBinding binding;

    @Value("${quote.stream.connector.ip}")
    private String hostIP;

    @Value("${quote.stream.connector.port}")
    private int port;

    @Value("${spring.cloud.stream.bindings."+QUOTE_STREAM_OUT+".producer.partitionCount}")
    private int partitionCount;

    public ProcessorStartSetupTaskRunner(QuoteStreamBinding binding) {
        this.binding = binding;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Application started with option names : {}",
                args.getOptionNames());
        log.info("Increment counter "+counter++);


//        OrderStreamConnector orderStreamConnector = new OrderStreamConnector(binding);
//        Runnable runnable = () -> {
//            orderStreamConnector.startListening(args.getSourceArgs()[0],Integer.parseInt(args.getSourceArgs()[1]));
//        };
//
//        Executors.newSingleThreadExecutor().submit(runnable);



        log.info("Connecting to socket : "+hostIP+":"+port);
        TCPStreamConnector tcpStreamConnector = new TCPStreamConnector(hostIP, port);
        TCPInstanceRegister.getInstance().registerStreamerClient(new QuoteStreamClient(binding.quotePacketStreamOut(), partitionCount));

//        StreamingRequest streamingRequest = ESHelper.createQuoteStreamingRequest();

//        String streamingRequestString = "{\"request\":{\"data\":{\"symbols\":[{\"symbol\":\"-29\"},{\"symbol\":\"-101\"}]},\"response_format\":\"json\",\"appID\":\"cb87fa34c48b8957170f912df197d0bc\",\"streaming_type\":\"quote\",\"request_type\":\"subscribe\"},\"echo\":{\"appID\":\"cb87fa34c48b8957170f912df197d0bc\"}}";
        Runnable quoteStreamRunnable = () -> {
            tcpStreamConnector.start();
            tcpStreamConnector.doSubscribe("");
        };

        Executors.newSingleThreadExecutor().submit(quoteStreamRunnable);

    }
}
