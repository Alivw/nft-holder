package io.debc.nft.thread.entity.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.debc.nft.thread.entity.config.EsConfig;
import io.debc.nft.thread.entity.entity.EsLog;
import io.debc.nft.thread.entity.inter.Produce;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.web3j.protocol.core.methods.response.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.debc.nft.utils.SysUtils.objectMapper;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-08 14:02
 **/
public class ES implements Produce {
    private RestHighLevelClient client;

    public ES() {
        client = EsConfig.newClientInstance(true);
    }

    // TODO initEs()

    @Override
    public List<Log> getEthLogs(Long blockNumber) {
        SearchRequest request = new SearchRequest("log");
        request.source(new SearchSourceBuilder().query(QueryBuilders.matchQuery("blockNumber", blockNumber)));
        try {
            SearchResponse resp = client.search(request, RequestOptions.DEFAULT);
            return Arrays.stream(resp.getHits().getHits()).map(e -> {
                try {
                    Log ethLog = new Log();
                    EsLog esLog = objectMapper.readValue(e.getSourceAsString(), EsLog.class);
                    ethLog.setBlockNumber(esLog.getBlockNumber());
                    ethLog.setAddress(esLog.getContractAddress());
                    ethLog.setBlockHash(esLog.getBlockHash());
                    List<String> topics = new ArrayList<>(4);
                    String topic1 = esLog.getTopic1();
                    if (topic1 != null && !"".equals(topic1)) {
                        topics.add(topic1);
                    }
                    String topic2 = esLog.getTopic2();
                    if (topic2 != null && !"".equals(topic2)) {
                        topics.add(topic2);
                    }
                    String topic3 = esLog.getTopic3();
                    if (topic3 != null && !"".equals(topic3)) {
                        topics.add(topic3);
                    }
                    String topic4 = esLog.getTopic4();
                    if (topic4 != null && !"".equals(topic4)) {
                        topics.add(topic4);
                    }
                    ethLog.setTopics(topics);
                    return ethLog;
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }
            }).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
