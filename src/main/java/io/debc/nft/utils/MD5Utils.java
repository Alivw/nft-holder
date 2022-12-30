package io.debc.nft.utils;

import com.esaulpaugh.headlong.util.FastHex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-30 10:17
 **/
public class MD5Utils {
    public static MessageDigest md5;

    static {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String encrypt(String s) {
        return FastHex.encodeToString(md5.digest(s.getBytes(StandardCharsets.UTF_8)));
    }
}
