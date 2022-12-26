package io.debc.nft.thread.entity.thread;


import com.fasterxml.jackson.core.JsonProcessingException;
import io.debc.nft.thread.entity.config.EsConfig;
import io.debc.nft.thread.entity.entity.EsBalance;
import io.debc.nft.thread.entity.utils.SysUtils;
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
 * @create: 2022-12-09 13:59
 **/
@Slf4j
public class TBalance extends Thread {

    private RestHighLevelClient client = EsConfig.newClientInstance(true);

    private EsBalance esBalance;

    public TBalance(EsBalance esBalance) {
        this.esBalance = esBalance;
    }

    @Override
    public void run() {
        long s = System.currentTimeMillis();
        IndexRequest request = new IndexRequest("erc20_balance");
        try {
            String address = esBalance.getAddress();
            if (address.length() == 66) {
                address = "0x" + address.substring(26);
                esBalance.setAddress(address);
            }
            request.id(address);
            request.source(SysUtils.objectMapper.writeValueAsString(esBalance), XContentType.JSON);
            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            log.debug("write balance {} to es takes time {} ms ,result:{}", esBalance, System.currentTimeMillis() - s, response.getResult());
        } catch (JsonProcessingException e) {
            log.error("json write error,esBalance:{}", esBalance.toString(), e);
        } catch (IOException e) {
            log.error("write to es error", e);
        }
    }
}
