package io.debc.nft.config;

import io.debc.nft.utils.SysUtils;
import org.web3j.protocol.core.methods.response.Log;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @description: 可配置的常量
 * @author: Jalivv
 * @create: 2022-12-08 11:34
 **/
public class ConfigurableConstants {

    public static final int PRODUCE_CORE_SIZE = Integer.parseInt(SysUtils.getSystemEnv("PRODUCE_CORE_SIZE", "10"));
    public static final int PRODUCE_QUEUE_SIZE = Integer.parseInt(SysUtils.getSystemEnv("PRODUCE_QUEUE_SIZE", "2000"));

    public static final int BALANCE_CORE_SIZE = Integer.parseInt(SysUtils.getSystemEnv("BALANCE_CORE_SIZE", "10"));
    public static final int BALANCE_QUEUE_SIZE = Integer.parseInt(SysUtils.getSystemEnv("BALANCE_QUEUE_SIZE", "2000"));

    public static final int CONTRACT_CORE_SIZE = Integer.parseInt(SysUtils.getSystemEnv("CONTRACT_CORE_SIZE", "10"));
    public static final int CONTRACT_QUEUE_SIZE = Integer.parseInt(SysUtils.getSystemEnv("CONTRACT_QUEUE_SIZE", "2000"));

   public static final int CALL_CONTRACT_CORE_SIZE = Integer.parseInt(SysUtils.getSystemEnv("CALL_CONTRACT_CORE_SIZE", "10"));
    public static final int CALL_CONTRACT_QUEUE_SIZE = Integer.parseInt(SysUtils.getSystemEnv("CALL_CONTRACT_QUEUE_SIZE", "2000"));


    public static final int POOL_QUEUE_SIZE = Integer.parseInt(SysUtils.getSystemEnv("POOL_QUEUE_SIZE", "2000"));
    public static final int CONSUMER_NUMBER = Integer.parseInt(SysUtils.getSystemEnv("CONSUMER_NUMBER", "4"));

    public static final String ENV = SysUtils.getSystemEnv("RUN_ENV", "DOCKER");

    public static final String DATASOURCE = SysUtils.getSystemEnv("DATASOURCE", "geth");
    public static final int MAIN_SLEEP_SECONDS = Integer.parseInt(SysUtils.getSystemEnv("MAIN_SLEEP_SECONDS", "6000"));
    public static final int REPEAT = Integer.parseInt(SysUtils.getSystemEnv("REPEAT", "2000"));


    public static final LinkedBlockingQueue<List<Log>> TASK_QUEUE = new LinkedBlockingQueue<>(Integer.parseInt(SysUtils.getSystemEnv("TASK_QUEUE_SIZE", "2000")));

}
