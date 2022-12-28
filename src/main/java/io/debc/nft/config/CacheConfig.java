package io.debc.nft.config;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import io.debc.nft.contract.Erc721Contract;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-28 14:26
 **/

public class CacheConfig {

    private static Erc721Contract erc721Contract = new Erc721Contract();

    public static LoadingCache<String, Integer> getContractCache() {
        return Caffeine.newBuilder().maximumSize(1_000_000L)
                .initialCapacity(4096)
                .build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String key) throws Exception {
                        return erc721Contract.supportInterface(key) ? 1 : 0;
                    }
                });
    }


}
