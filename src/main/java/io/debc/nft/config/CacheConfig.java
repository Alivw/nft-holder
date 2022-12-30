package io.debc.nft.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import io.debc.nft.contract.Erc1155Contract;
import io.debc.nft.contract.Erc721Contract;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-28 14:26
 **/

public class CacheConfig {

    private static Erc721Contract erc721Contract = new Erc721Contract();

    private static Erc1155Contract erc1155Contract = new Erc1155Contract();

    public static LoadingCache<String, Integer> get721ContractCache() {
        return Caffeine.newBuilder().maximumSize(500_000L)
                .initialCapacity(1024)
                .build(key -> erc721Contract.supportInterface(key) ? 1 : 0);
    }

    public static LoadingCache<String, Integer> get1155ContractCache() {
        return Caffeine.newBuilder().maximumSize(500_000L)
                .initialCapacity(1024)
                .build(key -> erc1155Contract.supportInterface(key) ? 1 : 0);
    }

    public static Cache<String, Boolean> nftHasHandledCache() {
        return Caffeine.newBuilder().maximumSize(500_000L)
                .initialCapacity(1024)
                .build();
    }

}
