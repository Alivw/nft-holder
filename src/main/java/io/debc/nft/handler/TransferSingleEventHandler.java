package io.debc.nft.handler;

import io.debc.nft.annotation.Event;
import io.debc.nft.contract.Erc1155Contract;
import io.debc.nft.entity.NFTBalance;
import io.debc.nft.utils.SysUtils;
import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.core.methods.response.Log;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-28 11:07
 **/
@Event
@Slf4j
public class TransferSingleEventHandler implements EventHandler {
    public static final String ID = "0xc3d58168c5ae7397731d063d5bbf3d657854427343f4c083240f7aacaa2d0f62";

    @Override
    public String getEventId() {
        return ID;
    }

    private Erc1155Contract erc1155Contract = new Erc1155Contract();

    @Override
    public List<NFTBalance> handle(List<Log> logs) {
        log.info("transfer single handle ....");
        logs = logs.stream().peek(t -> t.setData(SysUtils.decodeTransferSingleData(t.getData().substring(2)))).collect(Collectors.toList());
        List<NFTBalance> ans = new ArrayList<>(logs.size());
        Map<String, Set<String>> nft1155Map = logs.stream().collect(Collectors.groupingBy(t -> t.getAddress() + "-" + t.getData(), Collectors.mapping(e -> e.getTopics().get(2), Collectors.toSet())));
        for (Map.Entry<String, Set<String>> entry : nft1155Map.entrySet()) {
            String[] keys = entry.getKey().split("-");
            String contractAddress = SysUtils.convertTooLongAddress(keys[0]);
            Integer is1155 = contract1155Cache.get(contractAddress);
            if (is1155 == 1) {
                for (String userId : entry.getValue()) {
                    Boolean nftHasHandled = nftHasHandleCache.getIfPresent(userId + contractAddress + keys[1]);
                    if (nftHasHandled == null) {
                        addNFTBalance(ans, userId, contractAddress, keys[1]);
                        nftHasHandleCache.put(userId + entry.getKey(), true);
                    }
                }
            }
        }
        log.info("transfer single handle successfully {}", ans.size());
        return ans;
    }

    /**
     * @param ans
     * @param userId
     * @param contractAddress
     * @param tokenId         BigInteger.toString()
     */
    private List<NFTBalance> addNFTBalance(List<NFTBalance> ans, String userId, String contractAddress, String tokenId) {
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
}
