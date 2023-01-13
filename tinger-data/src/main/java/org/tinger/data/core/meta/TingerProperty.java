package org.tinger.data.core.meta;

import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Field;

@Data
@Builder
public class TingerProperty {
    private String name;
    private Class<?> type;
    private String column;
    private Field field;
}