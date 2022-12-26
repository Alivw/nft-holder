package io.debc.nft.thread.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-09 13:54
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EsContract {
    private String address;
    private String name;
    private String symbol;
    private Integer decimal;
    private String totalSupply;
    private List<String> standard;
    private String std;
}
