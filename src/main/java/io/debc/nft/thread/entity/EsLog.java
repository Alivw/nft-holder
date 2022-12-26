package io.debc.nft.thread.entity;

import lombok.Data;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-08 16:05
 **/
@Data
public class EsLog {
    private String blockHash;
    private String blockNumber;
    private long blockTime;
    private String contractAddress;
    private String topic1;
    private String topic2;
    private String topic3;
    private String topic4;
    private String data;
    private String txHash;
    private int txIndex;
    private int index;
}
