package io.debc.nft.thread.entity.thread.utils;

import io.debc.nft.thread.entity.thread.config.RedisConfig;
import redis.clients.jedis.Jedis;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-12 15:59
 **/
public class RedisUtils {

    public static long getLastExecNumber() {
        Jedis jedis = RedisConfig.pool.getResource();
        String balance = jedis.get("erc20_balance");
        if ("".equals(balance) || balance == null) {
            return 0L;
        }
        jedis.close();
        return Long.parseLong(balance);
    }

    public static void setLastExecNumber(long blockNumber) {
        Jedis jedis = RedisConfig.pool.getResource();
        jedis.set("erc20_balance", String.valueOf(blockNumber));
        jedis.close();
    }

    public static void addExecBlock(long blockNumber) {
        Jedis jedis = RedisConfig.pool.getResource();
        jedis.setbit("erc_balance", blockNumber, true);
        jedis.close();
    }

    public static long getMin0bit(long ethBlockNumber) {
        long i = ethBlockNumber;
        Jedis jedis = RedisConfig.pool.getResource();
        while (!jedis.getbit("erc_balance", --i)) {
        }
        return i;
    }
}
