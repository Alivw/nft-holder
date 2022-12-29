package io.debc.nft.handler;

import io.debc.nft.annotation.Event;
import io.debc.nft.contract.Erc721Contract;
import io.debc.nft.entity.NFTBalance;
import org.web3j.protocol.core.methods.response.Log;

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
public class TransferEventHandler implements EventHandler {
    public static final String ID = "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef";
    private static Erc721Contract erc721Contract = new Erc721Contract();

    //0x7c40c393dc0f283f318791d746d894ddd3693572  0x00000000000000000000000000000000000000000000000000000000000022a6
    @Override
    public List<NFTBalance> handle(List<Log> logs) {
        List<NFTBalance> ans = new ArrayList<>(logs.size());
        Map<String, Set<String>> nft721Map = logs.stream().collect(Collectors.groupingBy(Log::getAddress, Collectors.mapping(e -> e.getTopics().get(3), Collectors.toSet())));
        for (Map.Entry<String, Set<String>> entry : nft721Map.entrySet()) {
            String contractAddress = entry.getKey();
            Integer is721 = contract721Cache.get(contractAddress);
            if (is721 == 1) {
                for (String tokenId : entry.getValue()) {
                    Boolean nftHasHandled = nftHasHandleCache.getIfPresent(contractAddress + tokenId);
                    if (nftHasHandled == null) {
                        addNFTBalance(ans, contractAddress, tokenId);
                        nftHasHandleCache.put(contractAddress + tokenId, true);
                    }
                }
            }
        }
        return ans;
    }

    private void addNFTBalance(List<NFTBalance> ans, String contractAddress, String tokenId) {
        NFTBalance nftBalance;
        String userAddress = null;
        try {
            userAddress = erc721Contract.ownerOf(contractAddress, tokenId.substring(2));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (userAddress != null) {
            nftBalance = new NFTBalance();
            nftBalance.setAddress(userAddress);
            nftBalance.setAmount("1");
            nftBalance.setTokenId(tokenId);
            nftBalance.setContract(contractAddress);
            nftBalance.setStd(0);
            ans.add(nftBalance);
        }
    }

    private void addNFTBalance(List<NFTBalance> ans, Map.Entry<String, Set<String>> entry, String contractAddress) throws Exception {
        NFTBalance nftBalance;
        for (String tokenId : entry.getValue()) {
            String userAddress = erc721Contract.ownerOf(contractAddress, tokenId.substring(2));
            if (userAddress != null) {
                nftBalance = new NFTBalance();
                nftBalance.setAddress(userAddress);
                nftBalance.setAmount("1");
                nftBalance.setTokenId(tokenId);
                nftBalance.setContract(contractAddress);
                ans.add(nftBalance);
            }
        }
    }

    @Override
    public String getEventId() {
        return ID;
    }
}
