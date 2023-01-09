package io.debc.nft.thread;

import io.debc.nft.entity.NFTBalance;
import io.debc.nft.handler.EventHandler;
import io.debc.nft.product.Producer;
import io.debc.nft.utils.CollectionUtils;
import io.debc.nft.utils.ESUtils;
import io.debc.nft.utils.SysUtils;
import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.core.methods.response.Log;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-08 16:57
 **/
@Slf4j
public class TConsumer extends Thread {

    private Producer producer;
    private long blockNumber;

    private Set<EventHandler> eventHandlers;

    public static final int retryTimes = Integer.parseInt(SysUtils.getSystemEnv("retry_times", "10"));

    public TConsumer(Producer producer, long blockNumber, Set<EventHandler> eventHandler) {
        this.eventHandlers = eventHandler;
        this.producer = producer;
        this.blockNumber = blockNumber;
    }

    @Override
    public void run() {
        List<Log> logs = producer.obtainTopics(blockNumber);
        if (!logs.isEmpty()) {
            doRun(logs, retryTimes);
        }
    }

    private void doRun(List<Log> logs, int retryTimes) {
        if (retryTimes > 0) {
            try {
                doRun(logs);
            } catch (Exception e) {
                log.error("handle {} throws exception,retry times = {}", blockNumber, retryTimes, e);
                doRun(logs, --retryTimes);
            }
        } else {
            log.error("handle {} throws exception", blockNumber);
            System.exit(500);
        }

    }

    private void doRun(List<Log> logs) {
        long s = System.currentTimeMillis();
        Map<String, List<Log>> logMap = logs.stream().collect(Collectors.groupingBy(e -> e.getTopics().get(0), Collectors.mapping(Function.identity(), Collectors.toList())));
        int size = logs.size();
        List<NFTBalance> nftBalances = new ArrayList<>(size + (size >> 1));
        for (Map.Entry<String, List<Log>> entry : logMap.entrySet()) {
            for (EventHandler eventHandler : eventHandlers) {
                if (eventHandler.canHandle(entry.getKey())) {

                    List<NFTBalance> handleBalances = eventHandler.handle(entry.getValue());
                    if (!CollectionUtils.isEmpty(handleBalances)) {
                        nftBalances.addAll(handleBalances);
                    }


                }
            }
        }
        if (!nftBalances.isEmpty()) {
            ESUtils.saveNFTBalanceBatch(nftBalances, blockNumber);
            log.info("write block info into es {}", blockNumber);
        }
        log.info("handle {} time :{}", blockNumber, System.currentTimeMillis() - s);
    }

}
