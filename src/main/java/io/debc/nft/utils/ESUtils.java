package io.debc.nft.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.debc.nft.config.EsConfig;
import io.debc.nft.entity.NFTBalance;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.ParsedStats;
import org.elasticsearch.search.aggregations.metrics.StatsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static io.debc.nft.utils.SysUtils.objectMapper;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-09 11:05
 **/

public class ESUtils {

    private static final RestHighLevelClient client = EsConfig.newClientInstance(true);

    public static boolean documentExists(String index, String field, String value) {
        CountRequest request = new CountRequest(index);
        QueryBuilder qb = QueryBuilders.matchQuery(field, value);
        request.query(qb);
        try {
            return client.count(request, RequestOptions.DEFAULT).getCount() > 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static long getMaxBlock() {
        SearchRequest searchRequest = new SearchRequest("nft_balance");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        StatsAggregationBuilder statsAggregationBuilder = AggregationBuilders.stats("blockNumber").field("blockNumber");
        searchSourceBuilder.aggregation(statsAggregationBuilder);
        searchRequest.source(searchSourceBuilder);

        SearchResponse response;
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Aggregations aggregations = response.getAggregations();
        ParsedStats blockNumber = aggregations.get("blockNumber");

        return Math.max(Double.valueOf(blockNumber.getMaxAsString()).longValue(), 0);
    }


    public static void put(long blockNumber) {
        IndexRequest request = new IndexRequest("nft_balance_log");
        request.id(String.valueOf(blockNumber));
        String json = "{\"blockNumber\":" + blockNumber + "}";
        request.source(json, XContentType.JSON);
        try {
            client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void saveNFTBalanceBatch(List<NFTBalance> nftBalances, long blockNumber) {
        BulkRequest request = new BulkRequest();
        for (NFTBalance balance : nftBalances) {
            try {
                IndexRequest indexRequest = new IndexRequest("nft_balance");
                //String tokenId = balance.getTokenId();
                //balance.setTokenId(new BigInteger(tokenId.substring(2), 16).toString());
                balance.setAddress(balance.getAddress().toLowerCase());
                balance.setContract(balance.getContract().toLowerCase());
                if (balance.getStd() == 0) {
                    indexRequest.id(MD5Utils.encrypt(balance.getContract() + balance.getTokenId()));
                } else {
                    indexRequest.id(MD5Utils.encrypt(balance.getAddress() + balance.getContract() + balance.getTokenId()));
                }
                balance.setBlockNumber(blockNumber);
                indexRequest.source(objectMapper.writeValueAsString(balance), XContentType.JSON);
                request.add(indexRequest);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
         /*
         设置刷新策略
        */
        //request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        try {
            client.bulk(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
