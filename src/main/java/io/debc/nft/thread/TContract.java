package io.debc.nft.thread;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.debc.nft.config.EsConfig;
import io.debc.nft.entity.EsContract;
import io.debc.nft.utils.SysUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-09 13:58
 **/
@Slf4j
public class TContract extends Thread {
    private RestHighLevelClient client = EsConfig.newClientInstance(true);

    private EsContract esContract;

    public TContract(EsContract esContract) {
        this.esContract = esContract;
    }

    @Override
    public void run() {
        long s = System.currentTimeMillis();
        IndexRequest request = new IndexRequest("erc_contract");
        try {
            request.id(esContract.getAddress());
            esContract.setStd(null);
            request.source(SysUtils.objectMapper.writeValueAsString(esContract), XContentType.JSON);
            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            log.debug("write contract {} to es takes time {} ms ,result:{}", esContract, System.currentTimeMillis() - s, response.getResult());
        } catch (JsonProcessingException e) {
            log.error("json write error,esContract:{}", esContract, e);
        } catch (IOException e) {
            log.error("write to es error", e);
        }
    }
}
