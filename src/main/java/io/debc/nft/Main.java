package io.debc.nft;

import io.debc.nft.annotation.Event;
import io.debc.nft.config.ConfigurableConstants;
import io.debc.nft.handler.EventHandler;
import io.debc.nft.inter.Produce;
import io.debc.nft.product.ES;
import io.debc.nft.product.Producer;
import io.debc.nft.product.Web3;
import io.debc.nft.thread.Pool;
import io.debc.nft.thread.TConsumer;
import io.debc.nft.utils.ESUtils;
import io.debc.nft.utils.SysUtils;
import io.debc.nft.utils.Web3Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
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
        // es min  5806610   RUN_ENV=http://192.168.31.55:8545;DATASOUCE=geth
        // 2022-12-29 14:14:57 [INFO ] [pool-produce-thread-2] i.d.n.t.TConsumer - handle 937821 time :15

        Set<EventHandler> eventHandlers = SysUtils.componentScan("io.debc.nft.handler", Event.class);

        Producer producer = getProducer();
        Pool pool = new Pool(PRODUCE_CORE_SIZE, PRODUCE_QUEUE_SIZE, "produce");
        long lastExecNumber = Long.parseLong(SysUtils.getSystemEnv("LAST_EXEC_BLOCK", "" + Math.max(ESUtils.getMaxBlock() - REPEAT, 0)));
        log.info("get lastExec block :{}", lastExecNumber);
        //pool.execute(new TConsumer(producer, 5844053, eventHandlers));
        while (true) {
            EventHandler.nftHasHandleCache.cleanUp();
            EventHandler.contract721Cache.cleanUp();
            long ethBlockNumber = Web3Utils.getEthBlockNumber();
            if (lastExecNumber < ethBlockNumber) {
                for (long i = lastExecNumber + 1; i <= ethBlockNumber; i++) {
                    pool.execute(new TConsumer(producer, i, eventHandlers));
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