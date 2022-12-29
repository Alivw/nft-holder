package io.debc.nft.handler;

import io.debc.nft.annotation.Event;
import io.debc.nft.contract.Erc1155Contract;
import io.debc.nft.entity.NFTBalance;
import io.debc.nft.utils.SysUtils;
import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.core.methods.response.Log;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-28 11:10
 **/
@Event
@Slf4j
public class TransferBatchEventHandler implements EventHandler {

    private Erc1155Contract erc1155Contract = new Erc1155Contract();
    public static final String ID = "0x4a39dc06d4c0dbc64b70af90fd698a233a518aa5d07e595d983b8c0526c8f7fb";

    @Override
    public List<NFTBalance> handle(List<Log> logs) {
        List<Log> logList = logs.stream().flatMap(t -> {
            BigInteger[] ids = SysUtils.decodeTransferBatchData(t.getData().substring(2));
            List<Log> ans = new ArrayList<>(ids.length);
            Log l;
            for (BigInteger id : ids) {
                l = new Log();
                l.setData(id.toString());
                l.setTopics(t.getTopics());
                l.setAddress(t.getAddress());
                ans.add(l);
            }
            return ans.stream();
        }).collect(Collectors.toList());
        List<NFTBalance> ans = new ArrayList<>(logList.size());
        Map<String, Set<String>> nft1155Map = logList.stream().collect(Collectors.groupingBy(t -> t.getAddress() + "-" + t.getData(), Collectors.mapping(e -> e.getTopics().get(2) + "-" + e.getTopics().get(3), Collectors.toSet())));
        for (Map.Entry<String, Set<String>> entry : nft1155Map.entrySet()) {
            String[] keys = entry.getKey().split("-");
            String contractAddress = SysUtils.convertTooLongAddress(keys[0]);
            Integer is1155 = contract1155Cache.get(contractAddress);
            if (is1155 == 1) {
                for (String fromAndToAddress : entry.getValue()) {
                    String[] userAddress = fromAndToAddress.split("-");
                    for (String address : userAddress) {
                        address = SysUtils.convertTooLongAddress(address);
                        Boolean nftHasHandled = nftHasHandleCache.getIfPresent(address + contractAddress + keys[1]);
                        if (nftHasHandled == null && !ETH_NULL_ADDRESS.equals(address)) {
                            addNFTBalance(ans, address, contractAddress, keys[1]);
                            nftHasHandleCache.put(address + entry.getKey(), true);
                        }
                    }

                }
            }
        }
        log.info("transfer single handle successfully {}", ans.size());
        return ans;
    }

    public List<NFTBalance> addNFTBalance(List<NFTBalance> ans, String userId, String contractAddress, String tokenId) {
        NFTBalance nftBalance;
        BigInteger balance;
        try {
            balance = erc1155Contract.balanceOf(userId, contractAddress, new BigInteger(tokenId));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (balance != null) {
            nftBalance = new NFTBalance();
            nftBalance.setAddress(userId);
            nftBalance.setAmount(balance.toString());
            nftBalance.setTokenId(tokenId);
            nftBalance.setContract(contractAddress);
            nftBalance.setStd(1);
            ans.add(nftBalance);
        }
        return ans;
    }

    @Override
    public String getEventId() {
        return ID;
    }

}
