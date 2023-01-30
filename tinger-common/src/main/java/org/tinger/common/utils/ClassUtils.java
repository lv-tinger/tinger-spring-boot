package org.tinger.common.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by tinger on 2022-10-01
 */
public class ClassUtils {

    public static Class<?> loadClass(String name) {
        try {
            return getDefaultClassLoader().loadClass(name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader classLoader = null;
        try {
            classLoader = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ignore) {
        }
        if (classLoader == null) {
            classLoader = ClassUtils.class.getClassLoader();
            if (classLoader == null) {
                try {
                    classLoader = ClassLoader.getSystemClassLoader();
                } catch (Throwable ignore) {
                }
            }
        }
        return classLoader;
    }

    public static String getSimpleName(Class<?> type) {
        if (type == null) {
            return null;
        }

        return type.getSimpleName();
    }

    public static String getClassName(Class<?> type) {
        if (type == null) {
            return null;
        }

        return type.getName();
    }

    public static List<Class<?>> getGenericSuperclass(Object object) {
        Class<?> type = object.getClass();
        if (Object.class.equals(type)) {
            throw new RuntimeException();
        }
        Type superclass = type.getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) superclass;
        return Arrays.stream(parameterizedType.getActualTypeArguments()).map(x -> (Class<?>) x).collect(Collectors.toList());
    }

    public static Object newInstance(String type){
        Class<?> clazz = ClassUtils.loadClass(type);
        Constructor<?> constructor = ConstructorUtils.getDeclaredConstructor(clazz);
        try {
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object newInstance(Class<?> type, Class<?>[] parameterTypes, Object[] parameterValues) {
        try {
            Constructor<?> constructor = ConstructorUtils.getDeclaredConstructor(type, parameterTypes);
            return constructor.newInstance(parameterValues);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
