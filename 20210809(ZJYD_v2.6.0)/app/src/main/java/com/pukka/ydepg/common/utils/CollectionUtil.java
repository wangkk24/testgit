package com.pukka.ydepg.common.utils;

import java.util.Collection;

/**
 * CollectionUtil
 */
public final class CollectionUtil {
    public static boolean isEmpty(Collection collection) {
        return null == collection || collection.isEmpty();
    }
}