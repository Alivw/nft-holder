package io.debc.nft.thread.entity.thread.entity;

import lombok.Data;

/**
 * @description: 记录执行到哪个块
 * @author: Jalivv
 * @create: 2022-12-08 13:05
 **/
@Data
public class BlockLog {
    private volatile long lastExecBlock;
}
