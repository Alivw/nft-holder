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

    public static final String TRANSFER_ID = "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef";
    public static final String TRANSFER_SINGLE_ID = "0xc3d58168c5ae7397731d063d5bbf3d657854427343f4c083240f7aacaa2d0f62";
    private Produce produce;

    public Producer(Produce produce) {
        this.produce = produce;
    }

    private static Predicate<Log> distinct() {
        return log -> {
            Map<String, Log> map = new ConcurrentHashMap<>();
            List<String> topics = log.getTopics();


            if (log.getTopics() == null) {
                return false;
            }

            if (topics.size() != 4) {
                return false;
            }

            //if (topics.get(0).equals(TRANSFER_ID)) {
            //    return map.put(log.getAddress() + topics.get(3), log) == null;
            //}

            if (TRANSFER_SINGLE_ID.equals(topics.get(0))) {
                return map.put(log.getAddress() + topics.get(2) + topics.get(3) + log.getData().substring(2, 66), log) == null;
            }

            return true;
        };
    }

    public List<Log> obtainTopics(Long blockNumber) {
        return produce.getEthLogs(blockNumber).stream()
                .filter(Producer.distinct())
                .collect(Collectors.toList());
    }

}
