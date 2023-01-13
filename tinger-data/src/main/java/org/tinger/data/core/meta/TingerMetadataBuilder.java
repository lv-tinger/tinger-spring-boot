package org.tinger.data.core.meta;

import org.tinger.common.utils.ConstructorUtils;
import org.tinger.common.utils.FieldUtils;
import org.tinger.data.core.anno.TingerDataColumn;
import org.tinger.data.core.anno.TingerDataColumnIgnore;
import org.tinger.data.core.anno.TingerDataPrimaryKey;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TingerMetadataBuilder {
    public TingerMetadata build(Class<?> type) {
        Field[] fields = FieldUtils.getMemberField(type);
        List<TingerProperty> properties = Arrays.stream(fields).map(this::property).filter(Objects::nonNull).collect(Collectors.toList());
        TingerProperty primaryKey = properties.stream().filter(x -> x.getField().getAnnotation(TingerDataPrimaryKey.class) != null).findFirst().orElse(null);
        Constructor<?> constructor = ConstructorUtils.getDeclaredConstructor(type);
        return TingerMetadata.builder().type(type).constructor(constructor).primaryKey(primaryKey).properties(properties).build();
    }

    private TingerProperty property(Field field) {
        Annotation ignore = FieldUtils.getAnnotation(field, TingerDataColumnIgnore.class);
        if (ignore != null) {
            return null;
        }
        field.setAccessible(true);

        String column = field.getName();
        TingerDataColumn tingerDataColumn = FieldUtils.getAnnotation(field, TingerDataColumn.class);
        if (tingerDataColumn != null) {
            column = tingerDataColumn.name();
        }

        return TingerProperty.builder().name(field.getName()).column(column).type(field.getType()).field(field).build();
    }
}