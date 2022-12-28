package io.debc.nft.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.debc.nft.config.EsConfig;
import io.debc.nft.entity.EsBalance;
import io.debc.nft.entity.EsContract;
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

public class EsQueryUtils {

    private static final RestHighLevelClient client = EsConfig.newClientInstance(true);
    private static final ConcurrentHashMap<String, EsContract> cache = new ConcurrentHashMap(6000000);

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
        SearchRequest searchRequest = new SearchRequest("nft_balance_log");
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

    public static EsContract getContractByAddress(String tokenAddress) {
        EsContract esContract = cache.get(tokenAddress);
        if (esContract != null) {
            return esContract;
        }

        SearchRequest request = new SearchRequest("erc_contract");
        SearchSourceBuilder builder = new SearchSourceBuilder();

        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("address", tokenAddress);
        builder.query(queryBuilder);
        request.source(builder);
        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();
            if (hits.getHits().length != 1) {
                return null;
            }
            SearchHit at = hits.getAt(0);
            esContract = objectMapper.readValue(at.getSourceAsString(), EsContract.class);
            cache.put(tokenAddress, esContract);
            return esContract;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<EsContract> getContractByAddresses(Set<String> contractAddresses) {
        List<EsContract> ans = new ArrayList<>(contractAddresses.size());
        for (String contractAddress : contractAddresses) {
            EsContract esContract = cache.get(contractAddress);
            if (esContract != null) {
                ans.add(esContract);
            }
        }
        if (ans.size() == contractAddresses.size()) {
            return ans;
        }

        SearchRequest request = new SearchRequest("erc_contract");
        SearchSourceBuilder builder = new SearchSourceBuilder();

        IdsQueryBuilder queryBuilder = QueryBuilders.idsQuery();
        queryBuilder.addIds(contractAddresses.toArray(new String[contractAddresses.size()]));
        builder.size(contractAddresses.size());
        builder.query(queryBuilder);
        request.source(builder);
        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();
            for (SearchHit hit : hits) {
                ans.add(objectMapper.readValue(hit.getSourceAsString(), EsContract.class));
            }

            for (EsContract an : ans) {
                cache.put(an.getAddress(), an);
            }
            return ans;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void saveContractBatch(List<EsContract> saveList) {
        BulkRequest request = new BulkRequest();
        for (EsContract object : saveList) {
            try {
                IndexRequest indexRequest = new IndexRequest("erc_contract");
                indexRequest.id(object.getAddress());
                indexRequest.source(objectMapper.writeValueAsString(object), XContentType.JSON);
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

    public static void saveBalanceBatch(List<EsBalance> esBalances) {
        BulkRequest request = new BulkRequest();
        for (EsBalance object : esBalances) {
            try {
                IndexRequest indexRequest = new IndexRequest("erc20_balance");
                indexRequest.id(object.getAddress() + object.getContract());
                indexRequest.source(objectMapper.writeValueAsString(object), XContentType.JSON);
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

    public static void putCache(Set<String> contractAddresses) {
        EsContract esContract;
        for (String contractAddress : contractAddresses) {
            esContract = new EsContract();
            esContract.setAddress(contractAddress);
            esContract.setStandard(Collections.singletonList("unknown"));
            cache.put(contractAddress, esContract);
        }
    }
}
