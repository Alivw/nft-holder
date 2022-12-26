package io.debc.nft.thread.entity.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-09 13:54
 **/
@Data
@ToString
public class EsBalance {
    private String address;
    private String balance;
    private String contract;
}
