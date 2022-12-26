package io.debc.nft.thread.entity.thread.utils;

import java.util.Collection;

import static io.debc.nft.utils.StringUtils.isBlank;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-09 10:29
 **/
public abstract class Assert {
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object object) {
        notNull(object,
                "[Assertion failed] - this argument is required; it must not be null");
    }

    public static void notEmpty(Collection<?> collection) {
        notEmpty(collection,
                "[Assertion failed] - this collection must not be empty: it must contain at least 1 element");
    }

    public static void notEmpty(Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isTrue(boolean b) {
        if (!b) {
            throw new NullPointerException("[Assertion failed] - this expression must be true");
        }
    }

    public static void notBlank(CharSequence str) {
        if (isBlank(str)) {
            throw new NullPointerException("[Assertion failed] - this expression must be not blank");
        }
    }

}
