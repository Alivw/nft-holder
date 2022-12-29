package io.debc.nft.product;

import io.debc.nft.inter.Produce;
import org.web3j.protocol.core.methods.response.Log;

import java.util.List;
import java.util.Map;
import java.util.TooManyListenersException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @description: productor
 * @author: Jalivv
 * @create: 2022-12-08 13:55
 **/
public class Producer {

    private Produce produce;

    public Producer(Produce produce) {
        this.produce = produce;
    }

    private static Predicate<Log> distinct() {
        return log -> {
            List<String> topics = log.getTopics();
            if (log.getTopics() == null) {
                return false;
            }
            return topics.size() == 4;
        };
    }

    public List<Log> obtainTopics(Long blockNumber) {
        return produce.getEthLogs(blockNumber).stream()
                .filter(Producer.distinct())
                .collect(Collectors.toList());
    }

}
