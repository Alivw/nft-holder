package io.debc.nft.thread.entity.thread.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.web3j.protocol.core.methods.response.Log;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-08 15:55
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EsResp {
    private Log _source;

    public Log get() {
        return _source;
    }

}
