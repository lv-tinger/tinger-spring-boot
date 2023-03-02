package org.tinger.common.utils;

import org.tinger.common.serialize.JsonSerializer;

public class JsonUtils {
    public static <T> T fromJson(String json, Class<T> type) {
        return JsonSerializer.getInstance().fromJson(json, type);
    }
}