package io.debc.nft.utils;

import com.esaulpaugh.headlong.abi.Tuple;
import com.esaulpaugh.headlong.abi.TupleType;
import com.esaulpaugh.headlong.util.FastHex;
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

    public static String decodeTransferSingleData(String data) {
        TupleType tupleType = TupleType.parse("(uint256,uint256)");
        Tuple decode = tupleType.decode(FastHex.decode(data));
        return decode.get(0).toString();
    }


    public static String convertTooLongAddress(String address) {
        return address.length() == 66 ? "0x" + address.substring(26) : address;
    }
}
