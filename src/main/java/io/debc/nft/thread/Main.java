package io.debc.nft.thread;

import io.debc.nft.thread.config.ConfigurableConstants;
import io.debc.nft.thread.consumer.EventHandler;
import io.debc.nft.thread.inter.Produce;
import io.debc.nft.thread.product.ES;
import io.debc.nft.thread.product.Producer;
import io.debc.nft.thread.product.Web3;
import io.debc.nft.thread.thread.Pool;
import io.debc.nft.thread.thread.TConsumer;
import io.debc.nft.thread.utils.EsQueryUtils;
import io.debc.nft.thread.utils.Web3Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

import static io.debc.nft.config.ConfigurableConstants.*;

/**
 * @description: ${description}
 * @author: Jalivv
 * @create: ${YEAR}-${MONTH}-${DAY} ${HOUR}:${MINUTE}
 **/
@Slf4j
public class Main {

    public static void main(String[] args) throws InterruptedException {


        //for (int i = 0; i < CONSUMER_NUMBER; i++) {
        //    // 启动消费线程
        //    new TConsumer(TASK_QUEUE, eventHandlers).start();
        //}

        Producer producer = getProducer();
        Pool pool = new Pool(PRODUCE_CORE_SIZE, PRODUCE_QUEUE_SIZE, "produce");
        long t = EsQueryUtils.getMaxBlock() - REPEAT;
        long lastExecNumber = t > 0 ? t : 0;
        log.info("get lastExec block :{}", lastExecNumber);
        EventHandler eventHandler = new EventHandler();
        //pool.execute(new TConsumer(producer, 12660509L, eventHandler));
        while (true) {
            long ethBlockNumber = Web3Utils.getEthBlockNumber();
            if (lastExecNumber < ethBlockNumber) {
                for (long i = lastExecNumber + 1; i <= ethBlockNumber; i++) {
                    pool.execute(new TConsumer(producer, i, eventHandler));
                }
            }
            lastExecNumber = ethBlockNumber;
            TimeUnit.SECONDS.sleep(MAIN_SLEEP_SECONDS);
        }


    }

    private static Producer getProducer() {
        Produce produce = null;
        if (ConfigurableConstants.DATASOURCE.equalsIgnoreCase("geth")) {
            produce = new Web3();
        }
        if (ConfigurableConstants.DATASOURCE.equalsIgnoreCase("es")) {
            produce = new ES();
        }
        return new Producer(produce);
    }


}