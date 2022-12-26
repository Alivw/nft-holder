package io.debc.nft.thread.entity.config;

import io.debc.nft.thread.entity.utils.SysUtils;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-12 15:54
 **/
public class RedisConfig {
    public static final String host = SysUtils.getSystemEnv("redisHost", "192.168.31.192");
    public static final String password = SysUtils.getSystemEnv("redisPassword", "");
    public static final int port = Integer.parseInt(SysUtils.getSystemEnv("redisPort", "6379"));

    public static final JedisPool pool = init();

    private static JedisPool init() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(8);
        config.setMaxTotal(18);
        return new JedisPool(config, host, port, 2000);
    }
}
