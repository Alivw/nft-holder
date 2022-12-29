package io.debc.nft.handler;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.LoadingCache;
import io.debc.nft.config.CacheConfig;
import io.debc.nft.entity.NFTBalance;
import org.web3j.protocol.core.methods.response.Log;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-13 09:31
 **/
public interface EventHandler {

    LoadingCache<String, Integer> contract721Cache = CacheConfig.get721ContractCache();
    LoadingCache<String, Integer> contract1155Cache = CacheConfig.get721ContractCache();

    Cache<String, Boolean> nftHasHandleCache = CacheConfig.nftHasHandledCache();
    /**
     * see https://www.4byte.directory/event-signatures/?page=126&sort=id <br/>
     * Transfer: 0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef
     * TransferSingle:0xc3d58168c5ae7397731d063d5bbf3d657854427343f4c083240f7aacaa2d0f62
     * TransferBatch: 0x4a39dc06d4c0dbc64b70af90fd698a233a518aa5d07e595d983b8c0526c8f7fb
     */
     Set<String> handleIds = new HashSet<>(Arrays.asList("0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef", "0xc3d58168c5ae7397731d063d5bbf3d657854427343f4c083240f7aacaa2d0f62", "0x4a39dc06d4c0dbc64b70af90fd698a233a518aa5d07e595d983b8c0526c8f7fb"));


    default boolean canHandle(String eventId){
        return getEventId().equals(eventId);
    }
    List<NFTBalance> handle(List<Log> logs);

    String getEventId();
}
