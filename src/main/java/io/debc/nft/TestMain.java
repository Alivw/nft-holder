package io.debc.nft;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.elasticsearch.script.mustache.SearchTemplateAction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-12 13:25
 **/
public class TestMain {
    static CacheLoader cacheLoader = new CacheLoader();

    public static void main(String[] args) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(10);

        Cache<String, Integer> cache = Caffeine.newBuilder().build();
        for (int i = 0; i < 20; i++) {
            final String j =""+ i % 5;
            pool.execute(()->{
                String intern = j.intern();
                synchronized (j) {
                    Integer c = cache.getIfPresent(j);
                    if (c == null) {
                        System.out.println("not hit cache");
                        cache.put("" + j, 4);
                    }else {
                        System.out.println(c);
                    }
                }


            });
        }
    }


    static class CacheLoader implements Function<String, Integer> {
        @Override
        public Integer apply(String s) {
            System.out.println("not hit cache");
            return Integer.parseInt(s) * 2;
        }
    }

}
