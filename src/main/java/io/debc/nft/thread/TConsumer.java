package io.debc.nft.thread;

import io.debc.nft.entity.NFTBalance;
import io.debc.nft.handler.EventHandler;
import io.debc.nft.product.Producer;
import io.debc.nft.utils.CollectionUtils;
import io.debc.nft.utils.EsQueryUtils;
import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.core.methods.response.Log;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.debc.nft.config.ConfigurableConstants.BALANCE_CORE_SIZE;
import static io.debc.nft.config.ConfigurableConstants.BALANCE_QUEUE_SIZE;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-08 16:57
 **/
@Slf4j
public class TConsumer extends Thread {

    static Pool pool = new Pool(BALANCE_CORE_SIZE, BALANCE_QUEUE_SIZE, "putBlockNumber");
    private Producer producer;
    private long blockNumber;

    private Set<EventHandler> eventHandlers;


    public TConsumer(Producer producer, long blockNumber, Set<EventHandler> eventHandler) {
        this.eventHandlers = eventHandler;
        this.producer = producer;
        this.blockNumber = blockNumber;
    }

    @Override
    public void run() {
        List<Log> logs = producer.obtainTopics(blockNumber);
        if (!logs.isEmpty()) {
            long s = System.currentTimeMillis();
            Map<String, List<Log>> logMap = logs.stream().collect(Collectors.groupingBy(e -> e.getTopics().get(0), Collectors.mapping(Function.identity(), Collectors.toList())));
            List<NFTBalance> nftBalances = new ArrayList<>(logs.size());
            for (Map.Entry<String, List<Log>> entry : logMap.entrySet()) {
                for (EventHandler eventHandler : eventHandlers) {
                    if (eventHandler.canHandle(entry.getKey())) {
                        try {
                            List<NFTBalance> handleBalances = eventHandler.handle(entry.getValue());
                            if (!CollectionUtils.isEmpty(handleBalances)) {
                                nftBalances.addAll(handleBalances);
                            }
                        } catch (Exception e) {
                            log.error("exec block error: {}",blockNumber,e);
                        }

                    }
                }
            }
            log.info("handle {} time :{}", blockNumber, System.currentTimeMillis() - s);
        }
        if (blockNumber == 13079443) {
            log.info("end time : {}", System.currentTimeMillis());
        }
    }

}
