package io.debc.nft.thread.entity.product;

import com.fasterxml.jackson.core.type.TypeReference;
import io.debc.nft.thread.entity.consumer.EventHandler;
import io.debc.nft.thread.entity.entity.RespData;
import io.debc.nft.thread.entity.inter.Produce;
import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.ipc.UnixIpcService;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

import static io.debc.nft.config.ConfigurableConstants.ENV;
import static io.debc.nft.utils.SysUtils.objectMapper;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-08 13:06
 **/
@Slf4j
public class Web3 implements Produce {
    private Web3jService web3jService;

    private Web3j web3;


    public Web3() {
        if ("DOCKER".equals(ENV)) {
            this.web3jService = new UnixIpcService("/java/geth.ipc", true);
        } else if ("INFURA".equals(ENV)) {
            this.web3jService = new HttpService(" https://mainnet.infura.io/v3/9aa3d95b3bc440fa88ea12eaa4456161", true);
        } else if ("LOCAL".equals(ENV)) {
            this.web3jService = new UnixIpcService("/home/docker/eth/data/.ethereum/geth.ipc", true);

        } else {
            this.web3jService = new HttpService(ENV, true);
        }
        web3 = Web3j.build(this.web3jService);
    }

    public List<Log> getEthLogs(Long blockNumber) {
        long s = System.currentTimeMillis();
        List<Log> ethLog;
        try {
            Map<String, Object> paraMap = new HashMap<>(3);
            List<String> logFilter = new ArrayList<>(EventHandler.handleIds);
            String b = "0x" + BigInteger.valueOf(blockNumber).toString(16);
            paraMap.put("fromBlock", b);
            paraMap.put("toBlock", b);
            paraMap.put("topics", Collections.singletonList(logFilter));

            String json = new Request<>("eth_getLogs", Collections.singletonList(paraMap), web3jService, EthLog.class).send().getRawResponse();
            ethLog = objectMapper.readValue(json, new TypeReference<RespData<List<Log>>>() {
            }).getResult();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.debug("get eth logs which block number is {} spending time :{}", blockNumber, System.currentTimeMillis() - s);
        return ethLog;
    }

    public Web3j get() {
        return this.web3;
    }


}
