package io.debc.nft.thread.entity.thread.inter;

import org.web3j.protocol.core.methods.response.Log;

import java.util.List;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-08 13:58
 **/
public interface Produce {
    List<Log> getEthLogs(Long blockNumber);
}
