package org.tinger.common.utils;

import org.tinger.common.buffer.TingerMapBuffer;
import org.tinger.common.core.Initial;
import org.tinger.common.core.Named;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;

public class ServiceLoaderUtils {
    private static final TingerMapBuffer<Class<?>, List<?>> BUFFER = new TingerMapBuffer<>();

    public static <T> T load(Class<T> typed) {
        List<T> list = loadAll(typed);
        if (list.size() != 1) {
            throw new RuntimeException();
        }
        return list.get(0);
    }

    public static <T> T load(Class<T> typed, String named) {
        Objects.requireNonNull(named);
        if (!typed.isAssignableFrom(Named.class)) {
            throw new RuntimeException();
        }
        List<T> list = loadAll(typed).stream().filter(x -> StringUtils.equals(((Named) x).getName(), named)).toList();
        if (list.size() != 1) {
            throw new RuntimeException();
        }
        return list.get(0);
    }

    public static <T> List<T> scan(Class<T> typed) {
        return loadAll(typed);
    }

    @SuppressWarnings("unchecked")
    private static <T> List<T> loadAll(Class<T> typed) {
        Objects.requireNonNull(typed);
        return (List<T>) BUFFER.get(typed, () -> {
            ServiceLoader<T> serviceLoader = java.util.ServiceLoader.load(typed, ClassUtils.getDefaultClassLoader());
            List<T> list = new LinkedList<>();

            for (T service : serviceLoader) {
                if (service instanceof Initial) {
                    ((Initial) service).init();
                }
                list.add(service);
            }
            if (CollectionUtils.isEmpty(list)) {
                throw new RuntimeException();
            }
            return list;
        });
    }
}