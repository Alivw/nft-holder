package io.debc.nft.thread.entity.thread.product;

import io.debc.nft.thread.entity.thread.inter.Produce;
import org.web3j.protocol.core.methods.response.Log;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @description: productor
 * @author: Jalivv
 * @create: 2022-12-08 13:55
 **/
public class Producer {

    public static final String TRANSFER_ID = "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef";
    private Produce produce;

    public Producer(Produce produce) {
        this.produce = produce;
    }

    private static Predicate<Log> distinct() {
        final Map<String, Log> map = new ConcurrentHashMap<>();
        return log -> {
            List<String> topics = log.getTopics();
            if (topics.size() <= 1) {
                return false;
            }
            String eventId = topics.get(0);
            if (TRANSFER_ID.equalsIgnoreCase(eventId) && topics.size() > 2) {
                return map.put(topics.get(1) + topics.get(2) + log.getAddress(), log) == null;
            } else {
                return map.put(log.getAddress() + topics.get(1), log) == null;
            }
        };
    }

    public List<Log> obtainTopics(Long blockNumber) {
        return produce.getEthLogs(blockNumber).stream()
                .filter(Producer.distinct())
                .collect(Collectors.toList());
    }

}
