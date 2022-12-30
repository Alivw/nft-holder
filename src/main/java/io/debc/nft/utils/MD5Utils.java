package io.debc.nft.utils;

import com.esaulpaugh.headlong.util.FastHex;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jcajce.provider.digest.MD5;

import javax.swing.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-30 10:17
 **/
@Slf4j
public class MD5Utils {

    public static String encrypt(String s) {
        MessageDigest md5 = new MD5.Digest();
        return FastHex.encodeToString(md5.digest(s.getBytes(StandardCharsets.UTF_8)));
    }
}
