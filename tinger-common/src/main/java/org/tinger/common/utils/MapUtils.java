package org.tinger.common.utils;

import java.util.Map;

/**
 * Created by tinger on 2023-02-03
 */
public class MapUtils {
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static Object getValue(Map<?, ?> map, String key) {
        if (isEmpty(map) || StringUtils.isEmpty(key)) {
            return null;
        }

        return map.get(key);
    }

    public static boolean exists(Map<?, ?> map, String key) {
        if (isEmpty(map) || StringUtils.isEmpty(key)) {
            return false;
        }

        return map.containsKey(key);
    }
}
