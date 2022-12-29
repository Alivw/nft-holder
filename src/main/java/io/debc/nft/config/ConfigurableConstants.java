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


    public static final String ENV = SysUtils.getSystemEnv("RUN_ENV", "DOCKER");

    public static final String DATASOURCE = SysUtils.getSystemEnv("DATASOURCE", "geth");
    public static final int MAIN_SLEEP_SECONDS = Integer.parseInt(SysUtils.getSystemEnv("MAIN_SLEEP_SECONDS", "4"));
    public static final int REPEAT = Integer.parseInt(SysUtils.getSystemEnv("REPEAT", "2000"));

}
