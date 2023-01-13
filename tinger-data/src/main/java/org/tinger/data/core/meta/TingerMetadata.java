package org.tinger.data.core.meta;

import lombok.*;

import java.lang.reflect.Constructor;
import java.util.List;

@Data
@Builder
public class TingerMetadata {
    private Class<?> type;
    private Constructor<?> constructor;
    private TingerProperty primaryKey;
    private List<TingerProperty> properties;
}