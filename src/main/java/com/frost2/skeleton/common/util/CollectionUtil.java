package com.frost2.skeleton.common.util;

import java.util.Collection;

/**
 * @author 陈伟平
 * @date 2020-10-13 17:21:13
 */
public class CollectionUtil {

    /**
     * @param coll 集合
     * @return 是否为空
     */
    public static boolean isEmpty(final Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isNotEmpty(final Collection<?> coll) {
        return !isEmpty(coll);
    }

}
