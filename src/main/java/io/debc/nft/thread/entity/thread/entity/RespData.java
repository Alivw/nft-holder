package io.debc.nft.thread.entity.thread.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RespData<T> {


    private String id;

    private T result;

}
