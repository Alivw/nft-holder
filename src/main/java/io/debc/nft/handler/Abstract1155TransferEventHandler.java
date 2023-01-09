package io.debc.nft.handler;

import io.debc.nft.contract.Erc1155Contract;
import io.debc.nft.entity.NFTBalance;
import io.debc.nft.utils.MD5Utils;
import io.debc.nft.utils.SysUtils;
import org.web3j.protocol.core.methods.response.Log;
import org.yaml.snakeyaml.events.Event;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-29 16:12
 **/
public abstract class Abstract1155TransferEventHandler implements EventHandler {
    private Erc1155Contract erc1155Contract = new Erc1155Contract();

    @Override
    public List<NFTBalance> handle(List<Log> logs) {
        List<NFTBalance> ans = new ArrayList<>(logs.size());
        Map<String, Set<String>> nft1155Map = logs.stream().collect(Collectors.groupingBy(t -> t.getAddress() + "-" + t.getData(), Collectors.mapping(e -> e.getTopics().get(2) + "-" + e.getTopics().get(3), Collectors.toSet())));
        for (Map.Entry<String, Set<String>> entry : nft1155Map.entrySet()) {
            String[] keys = entry.getKey().split("-");
            String contractAddress = SysUtils.convertTooLongAddress(keys[0]);
            Integer is1155 = contract1155Cache.get(contractAddress);
            if (is1155 == 1) {
                for (String fromAndToAddress : entry.getValue()) {
                    for (String longUserId : fromAndToAddress.split("-")) {
                        String userId = SysUtils.convertTooLongAddress(longUserId);
                        String cacheKey = MD5Utils.encrypt(userId + contractAddress + keys[1]);
                        if (!ETH_NULL_ADDRESS.equals(userId)) {
                            nftHasHandleCache.get(cacheKey, key -> {
                                addNFTBalance(ans, userId, contractAddress, keys[1]);
                                return true;
                            });
                        }
                    }
                }
            }
        }
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
            nftBalance.setStd("1155");
            ans.add(nftBalance);
        }
        return ans;
    }
}
