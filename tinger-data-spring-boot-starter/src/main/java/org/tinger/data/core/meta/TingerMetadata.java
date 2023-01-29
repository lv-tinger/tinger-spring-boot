package org.tinger.data.core.meta;

import lombok.*;
import org.tinger.common.utils.StringUtils;

import java.lang.reflect.Constructor;
import java.util.List;

@Data
@Builder
public class TingerMetadata<T> {
    private Class<T> type;
    private Constructor<T> constructor;
    private TingerProperty primaryKey;
    private List<TingerProperty> properties;

    public TingerProperty getPropertyByName(String name) {
        if (StringUtils.equals(name, primaryKey.getName())) {
            return this.primaryKey;
        }
        return properties.stream().filter(x -> StringUtils.equals(x.getName(), name)).findFirst().orElse(null);
    }
}