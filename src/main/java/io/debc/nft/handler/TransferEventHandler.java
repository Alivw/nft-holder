package io.debc.nft.handler;

import io.debc.nft.annotation.Event;
import io.debc.nft.contract.Erc721Contract;
import io.debc.nft.entity.NFTBalance;
import io.debc.nft.utils.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.core.methods.response.Log;

import javax.swing.*;
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
 * @create: 2022-12-28 11:06
 **/
@Event
@Slf4j
public class TransferEventHandler implements EventHandler {
    public static final String ID = "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef";

    private static Erc721Contract erc721Contract = new Erc721Contract();

    @Override
    public String getEventId() {
        return ID;
    }

    private void addNFTBalance(List<NFTBalance> ans, String contractAddress, String tokenId) throws IOException {
        NFTBalance nftBalance;
        String userAddress = erc721Contract.ownerOf(contractAddress, tokenId.substring(2));

        nftBalance = new NFTBalance();
        nftBalance.setAddress(userAddress);
        nftBalance.setAmount("1");
        nftBalance.setTokenId(new BigInteger(tokenId.substring(2), 16).toString());
        nftBalance.setContract(contractAddress);
        nftBalance.setStd("721");
        ans.add(nftBalance);
    }


    @Override
    public List<NFTBalance> handle(List<Log> logs) {
        List<NFTBalance> ans = new ArrayList<>(logs.size());
        Map<String, Set<String>> nft721Map = logs.stream().collect(Collectors.groupingBy(Log::getAddress, Collectors.mapping(e -> e.getTopics().get(3), Collectors.toSet())));
        for (Map.Entry<String, Set<String>> entry : nft721Map.entrySet()) {
            String contractAddress = entry.getKey();
            Integer is721 = contract721Cache.get(contractAddress);
            if (is721 == 1) {
                for (String tokenId : entry.getValue()) {
                    String cacheKey = MD5Utils.encrypt(contractAddress + tokenId);
                    nftHasHandleCache.get(cacheKey, key -> {
                        addNFTBalance(ans, contractAddress, tokenId, 10);
                        return true;
                    });
                }
            }
        }
        return ans;
    }

    private void addNFTBalance(List<NFTBalance> ans, String contractAddress, String tokenId, int retryTimes) {
        if (retryTimes > 0) {
            try {
                addNFTBalance(ans, contractAddress, tokenId);
            } catch (IOException e) {
                addNFTBalance(ans, contractAddress, tokenId, --retryTimes);
            } catch (RuntimeException e) {
                // do nothing
                log.debug("{}", e);
            }
        } else {
            throw new RuntimeException("error");
        }
    }
}
