package io.debc.nft.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigInteger;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-28 10:40
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class NFTBalance {
    private String contract;
    private String tokenId;
    private String address;
    private String amount;
    // 0:721 1:1155
    private Integer std;

    private Long blockNumber;
}
