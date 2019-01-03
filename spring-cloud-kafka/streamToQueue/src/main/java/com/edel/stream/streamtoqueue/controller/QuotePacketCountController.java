package com.edel.stream.streamtoqueue.controller;

import com.edel.stream.streamtoqueue.binding.QuoteStreamBinding;
import com.edel.stream.streamtoqueue.util.ESHelper;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.*;
import org.springframework.cloud.stream.binder.kafka.streams.QueryableStoreRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by jitheshrajan on 10/1/18.
 */
@RestController("QuotePackets")
@RequestMapping("qp")
public class QuotePacketCountController {

    private final QueryableStoreRegistry registry;


    public QuotePacketCountController(QueryableStoreRegistry registry) {
        this.registry = registry;
    }

    @GetMapping("/count")
    public Map<String, Long> getQuotePacketCounts(){

        Map<String, Long> counts = new HashMap<String, Long>();

        ReadOnlyKeyValueStore<String, Long> queryableKeyValueStore =
                this.registry.getQueryableStoreType(QuoteStreamBinding.QUOTE_PACKET_MV,
                        QueryableStoreTypes.keyValueStore());

        KeyValueIterator<String, Long> all = queryableKeyValueStore.all();

        while(all.hasNext()){
            KeyValue<String, Long> item = all.next();
            counts.put(item.key, item.value);
        }

        return counts;
    }

    @GetMapping("/windowed/count")
    public Map<String, Map<String, Long>> getQuotePacketWindowedCounts(
            @RequestParam(required = false, value = "sym") String symbol
    ){


        if(symbol == null || symbol.isEmpty()){
            symbol = "11536_NSE";
        }

        Map<String, Map<String, Long>> counts = new HashMap<>();

        final ReadOnlyWindowStore<String, Long> queryableKeyValueStore =
                this.registry.getQueryableStoreType(QuoteStreamBinding.QUOTE_PACKET_WINDOWED_MV,
                QueryableStoreTypes.<String, Long>windowStore());



        WindowStoreIterator<Long> allSymbolUpdates = queryableKeyValueStore.fetch(symbol, 0, System.currentTimeMillis());

        Map<String, Long> windowsMap =  new TreeMap<>();
        while(allSymbolUpdates.hasNext()){
            KeyValue<Long, Long> item = allSymbolUpdates.next();

            if (counts.get(symbol) != null){
                windowsMap = counts.get(symbol);
            }

            windowsMap.put(ESHelper.convertEpochToDate(item.key, ESHelper.TIME_WINDOW_FORMAT), item.value);
            counts.put(symbol, windowsMap);

        }


        return counts;
    }

}
