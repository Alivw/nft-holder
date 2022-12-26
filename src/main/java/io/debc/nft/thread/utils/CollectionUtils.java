package io.debc.nft.thread.utils;

import java.util.Collection;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-09 10:17
 **/

public class CollectionUtils {

    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }
}
