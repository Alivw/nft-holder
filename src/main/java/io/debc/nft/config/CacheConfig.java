package io.debc.nft.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.debc.nft.contract.Erc721Contract;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-28 14:26
 **/

public class CacheConfig {

    private static Erc721Contract erc721Contract = new Erc721Contract();

    public static LoadingCache<String, Integer> getContractCache() {
        return CacheBuilder.newBuilder().maximumSize(1000000L)
                .initialCapacity(1000000)
                .build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String key) throws Exception {
                        return erc721Contract.supportInterface(key) ? 1 : 0;
                    }

                });
    }
}
