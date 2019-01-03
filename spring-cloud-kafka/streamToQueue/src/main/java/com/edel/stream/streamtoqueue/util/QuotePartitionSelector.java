package com.edel.stream.streamtoqueue.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.binder.PartitionSelectorStrategy;

/**
 * Created by jitheshrajan on 10/22/18.
 */
public class QuotePartitionSelector implements PartitionSelectorStrategy {

    private static final Logger log =
            LoggerFactory.getLogger(QuotePartitionSelector.class);

    @Override
    public int selectPartition(Object o, int i) {

        log.info(o.toString());
        log.info(i+"");
        return 0;
    }
}
