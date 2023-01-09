package io.debc.nft.handler;

import io.debc.nft.contract.Erc1155Contract;
import io.debc.nft.entity.NFTBalance;
import io.debc.nft.utils.MD5Utils;
import io.debc.nft.utils.SysUtils;
import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.core.methods.response.Log;
import org.yaml.snakeyaml.events.Event;

import java.io.IOException;
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
@Slf4j
public abstract class Abstract1155TransferEventHandler implements EventHandler {
    private Erc1155Contract erc1155Contract = new Erc1155Contract();

    @Override
    public List<NFTBalance> handle(List<Log> logs) {
        int size = logs.size();
        List<NFTBalance> ans = new ArrayList<>(size + (size >> 1));
        Map<String, Set<String>> nft1155Map = logs.stream().collect(Collectors.groupingBy(t -> t.getAddress() + "-" + t.getData(), Collectors.mapping(e -> e.getTopics().get(2) + "-" + e.getTopics().get(3), Collectors.toSet())));
        String cacheKey;
        for (Map.Entry<String, Set<String>> entry : nft1155Map.entrySet()) {
            String[] keys = entry.getKey().split("-");
            String contractAddress = SysUtils.convertTooLongAddress(keys[0]);
            Integer is1155 = contract1155Cache.get(contractAddress);
            if (is1155 == 1) {
                for (String fromAndToAddress : entry.getValue()) {

                    for (String longUserId : fromAndToAddress.split("-")) {
                        String userId = SysUtils.convertTooLongAddress(longUserId);
                        cacheKey = MD5Utils.encrypt(userId + contractAddress + keys[1]);
                        if (!ETH_NULL_ADDRESS.equals(userId)) {
                            nftHasHandleCache.get(cacheKey, key -> {
                                addNFTBalance(ans, userId, contractAddress, keys[1], 10);
                                return true;
                            });
                        }
                    }
                }
            }
        }
        return ans;
    }

    private void addNFTBalance(List<NFTBalance> ans, String userId, String contractAddress, String key, int retryTimes) {
        if (retryTimes > 0) {
            try {
                addNFTBalance(ans, userId, contractAddress, key);
            } catch (IOException e) {
                addNFTBalance(ans, userId, contractAddress, key, --retryTimes);
            } catch (RuntimeException e) {
                // do nothing
                log.debug("{}", e);
            }
        } else {
            throw new RuntimeException("error");
        }
    }

    /**
     * @param ans
     * @param userId
     * @param contractAddress
     * @param tokenId         BigInteger.toString()
     */
    private List<NFTBalance> addNFTBalance(List<NFTBalance> ans, String userId, String contractAddress, String tokenId) throws IOException {
        BigInteger balance = erc1155Contract.balanceOf(userId, contractAddress, new BigInteger(tokenId));
        NFTBalance nftBalance = new NFTBalance();
        nftBalance.setAddress(userId);
        nftBalance.setAmount(balance.toString());
        nftBalance.setTokenId(tokenId);
        nftBalance.setContract(contractAddress);
        nftBalance.setStd("1155");
        ans.add(nftBalance);
        return ans;
    }
}
