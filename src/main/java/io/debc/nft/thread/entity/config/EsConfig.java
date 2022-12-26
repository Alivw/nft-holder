package io.debc.nft.thread.entity.config;

import io.debc.nft.thread.entity.utils.SysUtils;
import lombok.Data;
import org.apache.http.HttpHost;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.concurrent.TimeUnit;

@Data
public class EsConfig {

    private static final String[] ES_HOST = SysUtils.getSystemEnv("ES_HOST", "192.168.31.55").split(",");
    private static final String[] ES_PORT = SysUtils.getSystemEnv("ES_PORT", "9200").split(",");
    private static final String ES_SCHEME = SysUtils.getSystemEnv("ES_SCHEME", "http");

    public static final String ES_CLIENT_IO_THREAD_NUMBER = SysUtils.getSystemEnv("ES_CLIENT_IO_THREAD_NUMBER", "1");

    public static RestHighLevelClient newClientInstance(boolean singleton) {
        if (ES_HOST.length != ES_PORT.length) {
            throw new RuntimeException("es_host.length(" + ES_HOST.length + ") not match es port.length" + ES_PORT.length);
        }
        if (singleton)
            return RestHighLevelClientHandler.instance;
        else {
            HttpHost[] hosts = new HttpHost[ES_HOST.length];
            for (int i = 0; i < hosts.length; i++) {
                hosts[i] = new HttpHost(ES_HOST[i], Integer.parseInt(ES_PORT[i]), ES_SCHEME);
            }
            return new RestHighLevelClient(RestClient.builder(hosts));
        }

    }

    public static class RestHighLevelClientHandler {


        private static RestHighLevelClient instance = init();

        private static RestHighLevelClient init() {
            HttpHost[] hosts = new HttpHost[ES_HOST.length];
            for (int i = 0; i < hosts.length; i++) {
                hosts[i] = new HttpHost(ES_HOST[i], Integer.parseInt(ES_PORT[i]), ES_SCHEME);
            }
            RestClientBuilder builder = RestClient.builder(hosts);
            //异步链接延时配置
            builder.setRequestConfigCallback(requestConfigBuilder ->
                    requestConfigBuilder
                            .setConnectTimeout(5000) //5秒
                            .setSocketTimeout(5000)
                            .setConnectionRequestTimeout(5000));

            //异步链接数配置
            builder.setHttpClientConfigCallback(httpClientBuilder -> {
                //最大连接数100个
                httpClientBuilder.setMaxConnTotal(100);
                //最大路由连接数
                httpClientBuilder.setMaxConnPerRoute(100);
                // 设置KeepAlive为5分钟的时间，不设置默认为-1，也就是持续连接，然而这会受到外界的影响比如Firewall，会将TCP连接单方面断开，从而会导致Connection reset by peer的报错
                // 参考github解决方案：https://github.com/TFdream/Elasticsearch-learning/issues/30
                httpClientBuilder.setKeepAliveStrategy((response, context) -> TimeUnit.MINUTES.toMillis(3))
                        .setDefaultIOReactorConfig(IOReactorConfig.custom().setIoThreadCount(Integer.parseInt(ES_CLIENT_IO_THREAD_NUMBER)).setSoKeepAlive(true).build());
                return httpClientBuilder;
            });
            return new RestHighLevelClient(builder);
        }
    }

}