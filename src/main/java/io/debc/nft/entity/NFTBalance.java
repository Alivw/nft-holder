package io.debc.nft.entity;

import lombok.Data;

import java.math.BigInteger;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-28 10:40
 **/
@Data
public class NFTBalance {
    private String contract;
    private String tokenId;
    private String address;
    private String amount;
}
