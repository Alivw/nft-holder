package io.debc.nft.consumer;

import io.debc.nft.contract.Contract;
import io.debc.nft.contract.Erc20Contract;
import io.debc.nft.entity.EsBalance;
import io.debc.nft.entity.EsContract;
import io.debc.nft.thread.Pool;
import io.debc.nft.utils.Assert;
import io.debc.nft.utils.CollectionUtils;
import io.debc.nft.utils.EsQueryUtils;
import io.debc.nft.utils.SysUtils;
import org.web3j.protocol.core.methods.response.Log;

import java.util.*;
import java.util.stream.Collectors;

import static io.debc.nft.config.ConfigurableConstants.CONTRACT_CORE_SIZE;
import static io.debc.nft.config.ConfigurableConstants.CONTRACT_QUEUE_SIZE;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-13 09:31
 **/
public class EventHandler {
    static Set<Contract> CONTRACTS = SysUtils.componentScan("io.debc.balance.contract", io.debc.nft.annotation.Contract.class);
    static Pool pool = new Pool(CONTRACT_CORE_SIZE, CONTRACT_QUEUE_SIZE, "contract");

    public static Set<String> handleIds = new HashSet<>(Arrays.asList("0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef", "0xe1fffcc4923d04b559f4d29a8bfc6cda04eb5b0d3c460751c2402c5c5cc9109c", "0x7fcf532c15f0a6db0bd6d0e038bea71d30d808c7d98cb3bf7268a95bf5081b65"));
    static Erc20Contract erc20Contract = new Erc20Contract();


    public final void handle(List<Log> ethLogs) {
        // collect contractAddress Find its contract, if it is erc20, and get the balance.
        Set<String> contractAddresses = ethLogs.stream().map(Log::getAddress).collect(Collectors.toSet());
        Assert.notEmpty(contractAddresses);
        List<EsContract> contracts = EsQueryUtils.getContractByAddresses(contractAddresses);
        if (!CollectionUtils.isEmpty(contracts)) {
            Set<String> set = contracts.stream().map(EsContract::getAddress).collect(Collectors.toSet());
            contractAddresses.removeAll(set);
        }
        // contractAddress is unknownAddress
        if (!CollectionUtils.isEmpty(contractAddresses)) {
            List<EsContract> saveList = new ArrayList<>(contractAddresses.size());
            for (String contractAddress : contractAddresses) {
                EsContract es = callAllContractMethods(contractAddress);
                if (es != null) {
                    saveList.add(es);
                }
            }
            if (!saveList.isEmpty()) {
                doSaveBatch(saveList);
                contracts.addAll(saveList);
            }else {
                EsQueryUtils.putCache(contractAddresses);
            }
        }
        Set<String> erc20 = contracts.stream().filter(e -> e.getStandard().contains("erc20")).map(EsContract::getAddress).collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(erc20)) {
            List<Log> logs = ethLogs.stream().filter(e -> erc20.contains(e.getAddress())).collect(Collectors.toList());
            if (!logs.isEmpty()) {
                List<EsBalance> esBalances = erc20Contract.calculateBalance(logs);
                if (!esBalances.isEmpty()) {
                    pool.execute(() -> EsQueryUtils.saveBalanceBatch(esBalances));
                }
            }
        }
    }


    private void doSaveBatch(List<EsContract> saveList) {
        EsQueryUtils.saveContractBatch(saveList);
    }

    private EsContract callAllContractMethods(String contractAddress) {
        List<String> standards = new ArrayList<>(3);
        EsContract ans = null;
        for (Contract contract : CONTRACTS) {
            EsContract esContract = contract.callContractMethods(contractAddress);
            if (esContract != null) {
                ans = esContract;
                standards.add(esContract.getStd());
            }
        }
        if (ans == null) {
            return null;
        }
        ans.setStandard(standards);
        return ans;
    }
}
