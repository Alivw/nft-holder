package io.debc.nft.thread.entity.thread.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.reflections.Reflections;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-08 11:35
 **/
public class SysUtils {

    public static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    /**
     * 获取系统环境变量
     *
     * @param key          UpperCase if not will be converted to UpperCase
     * @param defaultValue Return intact
     * @return
     */
    public static String getSystemEnv(String key, String defaultValue) {
        key = key.toUpperCase();
        String value = System.getenv(key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public static Set componentScan(String packageName, Class clazz) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> types = reflections.getTypesAnnotatedWith(clazz);
        Set ans = Collections.emptySet();
        if (!types.isEmpty()) {
            ans = new HashSet<>(types.size());
            for (Class<?> type : types) {
                try {
                    ans.add(type.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return ans;
    }
}
