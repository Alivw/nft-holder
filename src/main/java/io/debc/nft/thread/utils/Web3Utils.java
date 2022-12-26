package io.debc.nft.thread.utils;

import io.debc.nft.thread.product.Web3;
import org.web3j.protocol.Web3j;

import java.io.IOException;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-12 16:03
 **/
public class Web3Utils {
    private static Web3j web3j = new Web3().get();

    public static long getEthBlockNumber() {
        try {
            return web3j.ethBlockNumber().send().getBlockNumber().longValue();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
