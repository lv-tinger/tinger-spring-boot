package org.tinger.data.core.meta;

import lombok.*;

import java.lang.reflect.Constructor;
import java.util.List;

@Data
@Builder
public class TingerMetadata<T> {
    private Class<T> type;
    private Constructor<T> constructor;
    private TingerProperty primaryKey;
    private List<TingerProperty> properties;
}