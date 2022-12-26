package io.debc.nft.thread.entity.thread.thread;

import io.debc.nft.thread.entity.thread.consumer.EventHandler;
import io.debc.nft.thread.entity.thread.product.Producer;
import io.debc.nft.thread.entity.thread.utils.EsQueryUtils;
import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.core.methods.response.Log;

import java.util.List;

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

    private EventHandler eventHandler;
    public TConsumer(Producer producer, long blockNumber, EventHandler eventHandler) {
        this.eventHandler = eventHandler;
        this.producer = producer;
        this.blockNumber = blockNumber;
    }

    @Override
    public void run() {
        List<Log> logs = producer.obtainTopics(blockNumber);
        if (!logs.isEmpty()) {
            long s = System.currentTimeMillis();
            eventHandler.handle(logs);
            pool.execute(() -> EsQueryUtils.put(blockNumber));
            log.info("handle {} time :{}", blockNumber, System.currentTimeMillis() - s);
        }
    }

}
